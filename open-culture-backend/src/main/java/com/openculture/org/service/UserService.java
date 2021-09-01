package com.openculture.org.service;

import com.openculture.org.config.Constants;
import com.openculture.org.domain.Authority;
import com.openculture.org.domain.User;
import com.openculture.org.repository.AuthorityRepository;
import com.openculture.org.repository.UserRepository;
import com.openculture.org.security.AuthoritiesConstants;
import com.openculture.org.security.SecurityUtils;
import com.openculture.org.service.dto.UserDTO;

import com.openculture.org.web.rest.errors.BadRequestAlertException;
import com.sendgrid.*;
import io.github.jhipster.security.RandomUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {
   private static final String apiKey = "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";
   private static final String from = "sender@openburkina.bf";
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final MailService mailService;
    private User newUser;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.mailService = mailService;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setActivationKey(RandomUtil.generateActivationKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password)throws IOException {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new UsernameAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
       // newUser.setLangKey(userDTO.getLangKey());
        newUser.setLangKey(Constants.DEFAULT_LANGUAGE);
        newUser.setTelephone(userDTO.getTelephone());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        User saveUser = userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        this.sendTextEmail(saveUser);
        return saveUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
             return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        Authority authority;
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setTelephone(userDTO.getTelephone());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        /*String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());*/
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        } else {
            Set<Authority> authoritie = new HashSet<>();
            authority = authorityRepository.getOne(AuthoritiesConstants.USER);
            authoritie.add(authority);
            user.setAuthorities(authoritie);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                if (userDTO.getEmail() != null) {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null) {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }


    @Transactional
    public User changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                newUser = user;
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
        return newUser;
    }

    @Transactional
    public User changeUserPassword(String key, String newPassword) {

        Optional<User> user = userRepository.findOneByActivationKey(key);
        if (!user.isPresent()){
            throw new BadRequestAlertException("Votre Email est incorrect","","");
        }
        if (user.isPresent()){
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.get().setPassword(encryptedPassword);
            //user.get().setActivated(false);
            user.get().setActivationKey(null);
            this.clearUserCaches(user.get());
        }
        log.debug("Changed password for User: {}", user);
        return userRepository.save(user.get());
    }

    @Transactional
    public User sendEmail(String login) {
        Optional<User> user = userRepository.findOneByLogin(login);
        if (!user.isPresent()){
            throw new BadRequestAlertException("Votre Email est incorrect","","");
        }
        if (user.isPresent()){
            mailService.sendEmail(user.get().getLogin(),"Création de compte sur OpenBurkina"," <!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "    <head>\n" +
                "        <title>activation du compte sur  openculture</title>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "        <link rel=\"icon\" href=\"http://127.0.0.1:4200/favicon.ico\" />\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <p>Cher "+user.get().getLogin() +" </p>\n" +
                "        <p>Votre compte sur openculture a été créé, veuillez cliquer sur le lien ci-dessous pour l'activer:</p>\n" +
                "        <p>\n" +
                "            <a href=\"http://127.0.0.1:4200/account?key=" +user.get().getActivationKey()+
                "\">http://127.0.0.1:4200/account?key=" +user.get().getActivationKey()+
                "</a>\n" +
                "        </p>\n" +
                "        <p>\n" +
                "            <span>Regards,</span>\n" +
                "            <br/>\n" +
                "            <em>openculture.</em>\n" +
                "        </p>\n" +
                "    </body>\n" +
                "</html>",false,true);
        }
        log.debug("Changed password for User: {}", user);
        log.info("-----------------: {}");
        log.info(user.get().getActivationKey());
        log.info("-----------------: {}");
        return user.get();
    }

    @Transactional
    public User sendPasswordEmail(String login) {
        Optional<User> user = userRepository.findOneByLogin(login);
        if (!user.isPresent()){
            throw new BadRequestAlertException("Votre Email est incorrect","","");
        }
        if (user.isPresent()){
            mailService.sendEmail(user.get().getLogin(),"Changer votre mot de passe"," <!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "    <head>\n" +
                "        <title>Changer votre mot de passe sur  openculture</title>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "        <link rel=\"icon\" href=\"http://127.0.0.1:4200/favicon.ico\" />\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <p>Cher "+user.get().getLogin() +" </p>\n" +
                "        <p>veuillez cliquer sur le lien ci-dessous pour changer votre mot de passe:</p>\n" +
                "        <p>\n" +
                "            <a href=\"http://127.0.0.1:4200/password?passwordkey=" +user.get().getActivationKey()+
                "\">http://127.0.0.1:4200/password?passwordkey=" +user.get().getActivationKey()+
                "</a>\n" +
                "        </p>\n" +
                "        <p>\n" +
                "            <span>Regards,</span>\n" +
                "            <br/>\n" +
                "            <em>openculture.</em>\n" +
                "        </p>\n" +
                "    </body>\n" +
                "</html>",false,true);
        }
        log.debug("Changed password for User: {}", user);
        return user.get();
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }


    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }

    public String sendTextEmail(User user) throws IOException {
        // the sender email should be the same as we used to Create a Single Sender Verification
        Email from = new Email(this.from);
        String subject = "The subject";
        Email to = new Email(user.getEmail());
        Content content = new Content("text/plain", "This is a test email");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(this.apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.info("-------EMAIL BODY ----------"+response.getBody());
            return response.getBody();
        } catch (IOException ex) {
            throw ex;
        }
    }
}
