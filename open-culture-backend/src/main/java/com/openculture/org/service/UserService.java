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
   String apiKey ;
   String from;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final MailService mailService;
    private User newUser;

    public UserService(@Value("${sendgrid.key}") String apiKey,@Value("${sendgrid.from}") String from, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager, MailService mailService) {
        this.apiKey=apiKey;
        this.from=from;
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
        this.sendTextEmail(saveUser,"Activer votre compte","Creation de compte sur OpenBurkina","account","key");
        this.clearUserCaches(newUser);
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
    public User sendEmail(String login) throws IOException {
        Optional<User> user = userRepository.findOneByLogin(login);
        if (!user.isPresent()){
            throw new BadRequestAlertException("Votre Email est incorrect","","");
        }
        if (user.isPresent()){
            this.sendTextEmail(user.get(),"Activer votre compte","Creation de compte sur OpenBurkina","account","key");
            /*<a href=\"http://127.0.0.1:4200/account?key="*/
        }
        log.debug("Changed password for User: {}", user);
        log.info("-----------------: {}");
        log.info(user.get().getActivationKey());
        log.info("-----------------: {}");
        return user.get();
    }

    @Transactional
    public User sendPasswordEmail(String login) throws IOException {
        Optional<User> user = userRepository.findOneByLogin(login);
        if (!user.isPresent()){
            throw new BadRequestAlertException("Votre Email est incorrect","","");
        }
        if (user.isPresent()){
            this.sendTextEmail(user.get(),"Changer votre mot de passe","Changer votre mot de passe sur  openculture","password","passwordkey");
/*
                http://127.0.0.1:4200/password?passwordkey=" +user.get().getActivationKey()+
*/
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

    public String sendTextEmail(User user,String action,String objet,String param,String key) throws IOException {
        // the sender email should be the same as we used to Create a Single Sender Verification
        Email from = new Email(this.from);
        String subject = objet;
        Email to = new Email(user.getEmail());

        Content content = new Content("text/html","<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
            "<html data-editor-version=\"2\" class=\"sg-campaigns\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1\">\n" +
            "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\">\n" +
            "  <style type=\"text/css\">\n" +
            "    body, p, div {\n" +
            "      font-family: inherit;\n" +
            "      font-size: 14px;\n" +
            "    }\n" +
            "    body {\n" +
            "      color: #000000;\n" +
            "    }\n" +
            "    body a {\n" +
            "      color: #1188E6;\n" +
            "      text-decoration: none;\n" +
            "    }\n" +
            "    p { margin: 0; padding: 0; }\n" +
            "    table.wrapper {\n" +
            "      width:100% !important;\n" +
            "      table-layout: fixed;\n" +
            "      -webkit-font-smoothing: antialiased;\n" +
            "      -webkit-text-size-adjust: 100%;\n" +
            "      -moz-text-size-adjust: 100%;\n" +
            "      -ms-text-size-adjust: 100%;\n" +
            "    }\n" +
            "    img.max-width {\n" +
            "      max-width: 100% !important;\n" +
            "    }\n" +
            "    .column.of-2 {\n" +
            "      width: 50%;\n" +
            "    }\n" +
            "    .column.of-3 {\n" +
            "      width: 33.333%;\n" +
            "    }\n" +
            "    .column.of-4 {\n" +
            "      width: 25%;\n" +
            "    }\n" +
            "    ul ul ul ul  {\n" +
            "      list-style-type: disc !important;\n" +
            "    }\n" +
            "    ol ol {\n" +
            "      list-style-type: lower-roman !important;\n" +
            "    }\n" +
            "    ol ol ol {\n" +
            "      list-style-type: lower-latin !important;\n" +
            "    }\n" +
            "    ol ol ol ol {\n" +
            "      list-style-type: decimal !important;\n" +
            "    }\n" +
            "    @media screen and (max-width:480px) {\n" +
            "      .preheader .rightColumnContent,\n" +
            "      .footer .rightColumnContent {\n" +
            "        text-align: left !important;\n" +
            "      }\n" +
            "      .preheader .rightColumnContent div,\n" +
            "      .preheader .rightColumnContent span,\n" +
            "      .footer .rightColumnContent div,\n" +
            "      .footer .rightColumnContent span {\n" +
            "        text-align: left !important;\n" +
            "      }\n" +
            "      .preheader .rightColumnContent,\n" +
            "      .preheader .leftColumnContent {\n" +
            "        font-size: 80% !important;\n" +
            "        padding: 5px 0;\n" +
            "      }\n" +
            "      table.wrapper-mobile {\n" +
            "        width: 100% !important;\n" +
            "        table-layout: fixed;\n" +
            "      }\n" +
            "      img.max-width {\n" +
            "        height: auto !important;\n" +
            "        max-width: 100% !important;\n" +
            "      }\n" +
            "      a.bulletproof-button {\n" +
            "        display: block !important;\n" +
            "        width: auto !important;\n" +
            "        font-size: 80%;\n" +
            "        padding-left: 0 !important;\n" +
            "        padding-right: 0 !important;\n" +
            "      }\n" +
            "      .columns {\n" +
            "        width: 100% !important;\n" +
            "      }\n" +
            "      .column {\n" +
            "        display: block !important;\n" +
            "        width: 100% !important;\n" +
            "        padding-left: 0 !important;\n" +
            "        padding-right: 0 !important;\n" +
            "        margin-left: 0 !important;\n" +
            "        margin-right: 0 !important;\n" +
            "      }\n" +
            "      .social-icon-column {\n" +
            "        display: inline-block !important;\n" +
            "      }\n" +
            "    }\n" +
            "  </style>\n" +
            " <link href=\"https://fonts.googleapis.com/css?family=Muli&display=swap\" rel=\"stylesheet\"><style>\n" +
            "  body {font-family: 'Muli', sans-serif;}\n" +
            "</style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<center class=\"wrapper\" data-link-color=\"#1188E6\" data-body-style=\"font-size:14px; font-family:inherit; color:#000000; background-color:#FFFFFF;\">\n" +
            "  <div class=\"webkit\">\n" +
            "    <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" class=\"wrapper\" bgcolor=\"#FFFFFF\">\n" +
            "      <tr>\n" +
            "        <td valign=\"top\" bgcolor=\"#FFFFFF\" width=\"100%\">\n" +
            "          <table width=\"100%\" role=\"content-container\" class=\"outer\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
            "            <tr>\n" +
            "              <td width=\"100%\">\n" +
            "                <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
            "                  <tr>\n" +
            "                    <td>\n" +
            "                      <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:100%; max-width:600px;\" align=\"center\">\n" +
            "                        <tr>\n" +
            "                          <td role=\"modules-container\" style=\"padding:0px 0px 0px 0px; color:#000000; text-align:left;\" bgcolor=\"#FFFFFF\" width=\"100%\" align=\"left\"><table class=\"module preheader preheader-hide\" role=\"module\" data-type=\"preheader\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"display: none !important; mso-hide: all; visibility: hidden; opacity: 0; color: transparent; height: 0; width: 0;\">\n" +
            "                            <tr>\n" +
            "                              <td role=\"module-content\">\n" +
            "                                <p></p>\n" +
            "                              </td>\n" +
            "                            </tr>\n" +
            "                          </table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" role=\"module\" data-type=\"columns\" style=\"padding:30px 20px 30px 20px;\" bgcolor=\"#f6f6f6\" data-distribution=\"1\">\n" +
            "                            <tbody>\n" +
            "                            <tr role=\"module-content\">\n" +
            "                              <td height=\"100%\" valign=\"top\"><table width=\"540\" style=\"width:540px; border-spacing:0; border-collapse:collapse; margin:0px 10px 0px 10px;\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" border=\"0\" bgcolor=\"\" class=\"column column-0\">\n" +
            "                                <tbody>\n" +
            "                                <tr>\n" +
            "                                  <td style=\"padding:0px;margin:0px;border-spacing:0;\">\n" +
            "                                \n" +
            "                                  <table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"948e3f3f-5214-4721-a90e-625a47b1c957\" data-mc-module-version=\"2019-10-22\">\n" +
            "                                    <tbody>\n" +
            "                                    <tr>\n" +
            "                                      <td style=\"padding:50px 30px 18px 30px; line-height:36px; text-align:inherit; background-color:#ffffff;\" height=\"100%\" valign=\"top\" bgcolor=\"#ffffff\" role=\"module-content\"><div><div style=\"font-family: inherit; text-align: center\"><span style=\"font-size: 43px\">Bienvenue sur OpenCulture</span></div><div></div></div></td>\n" +
            "                                    </tr>\n" +
            "                                    </tbody>\n" +
            "                                  </table>\n" +
            "                                  <table class=\"module\" role=\"module\" data-type=\"text\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"a10dcb57-ad22-4f4d-b765-1d427dfddb4e\" data-mc-module-version=\"2019-10-22\">\n" +
            "                                    <tbody>\n" +
            "                                    <tr>\n" +
            "                                      <td style=\"padding:18px 30px 18px 30px; line-height:22px; text-align:inherit; background-color:#ffffff;\" height=\"100%\" valign=\"top\" bgcolor=\"#ffffff\" role=\"module-content\"><div><div style=\"font-family: inherit; text-align: center\"><span style=\"font-size: 18px\"> veuillez cliquer </span><span style=\"color: #000000; font-size: 18px; font-family: arial, helvetica, sans-serif\">sur le lien ci-dessous</span><span style=\"font-size: 18px\">.</span></div>\n" +
            "                                        <div style=\"font-family: inherit; text-align: center\"><span style=\"color: #ffbe00; font-size: 18px\"><strong>Merci!</strong></span></div><div></div></div></td>\n" +
            "                                    </tr>\n" +
            "                                    </tbody>\n" +
            "                                  </table><table class=\"module\" role=\"module\" data-type=\"spacer\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout: fixed;\" data-muid=\"7770fdab-634a-4f62-a277-1c66b2646d8d\">\n" +
            "                                    <tbody>\n" +
            "                                    <tr>\n" +
            "                                      <td style=\"padding:0px 0px 20px 0px;\" role=\"module-content\" bgcolor=\"#ffffff\">\n" +
            "                                      </td>\n" +
            "                                    </tr>\n" +
            "                                    </tbody>\n" +
            "                                  </table><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"module\" data-role=\"module-button\" data-type=\"button\" role=\"module\" style=\"table-layout:fixed;\" width=\"100%\" data-muid=\"d050540f-4672-4f31-80d9-b395dc08abe1\">\n" +
            "                                    <tbody>\n" +
            "                                    <tr>\n" +
            "                                      <td align=\"center\" bgcolor=\"#ffffff\" class=\"outer-td\" style=\"padding:0px 0px 0px 0px; background-color:#ffffff;\">\n" +
            "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"wrapper-mobile\" style=\"text-align:center;\">\n" +
            "                                          <tbody>\n" +
            "                                          <tr>\n" +
            "                                            <td align=\"center\" bgcolor=\"#ffbe00\" class=\"inner-td\" style=\"border-radius:6px; font-size:16px; text-align:center; background-color:inherit;\">\n" +
            "                                              <a href=\"http://127.0.0.1:4200/"+param+"?"+key+"=" +user.getActivationKey()+ "\"style=\"background-color:#ffbe00; border:1px solid #ffbe00; border-color:#ffbe00; border-radius:0px; border-width:1px; color:#000000; display:inline-block; font-size:14px; font-weight:normal; letter-spacing:0px; line-height:normal; padding:12px 40px 12px 40px; text-align:center; text-decoration:none; border-style:solid; font-family:inherit;\" target=\"_blank\">"+action+"</a>\n" +
            "                                            </td>\n" +
            "                                          </tr>\n" +
            "                                          </tbody>\n" +
            "                                        </table>\n" +
            "                                      </td>\n" +
            "                                    </tr>\n" +
            "                                    </tbody>\n" +
            "                                  </table>\n" +
            "                                  </td>\n" +
            "                                </tr>\n" +
            "                                </tbody>\n" +
            "                              </table></td>\n" +
            "                            </tr>\n" +
            "                            </tbody>\n" +
            "                          </table>\n" +
            "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"module\" data-role=\"module-button\" data-type=\"button\" role=\"module\" style=\"table-layout:fixed;\" width=\"100%\" data-muid=\"550f60a9-c478-496c-b705-077cf7b1ba9a\">\n" +
            "                            <tbody>\n" +
            "                            <tr>\n" +
            "                              <td align=\"center\" bgcolor=\"\" class=\"outer-td\" style=\"padding:0px 0px 20px 0px;\">\n" +
            "                                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"wrapper-mobile\" style=\"text-align:center;\">\n" +
            "                                  <tbody>\n" +
            "                                  <tr>\n" +
            "                                    <td align=\"center\" bgcolor=\"#f5f8fd\" class=\"inner-td\" style=\"border-radius:6px; font-size:16px; text-align:center; background-color:inherit;\"><a style=\"background-color:#f5f8fd; border:1px solid #f5f8fd; border-color:#f5f8fd; border-radius:25px; border-width:1px; color:#a8b9d5; display:inline-block; font-size:10px; font-weight:normal; letter-spacing:0px; line-height:normal; padding:5px 18px 5px 18px; text-align:center; text-decoration:none; border-style:solid; font-family:helvetica,sans-serif;\">@OpenBurkina</a></td>\n" +
            "                                  </tr>\n" +
            "                                  </tbody>\n" +
            "                                </table>\n" +
            "                              </td>\n" +
            "                            </tr>\n" +
            "                            </tbody>\n" +
            "                          </table></td>\n" +
            "                        </tr>\n" +
            "                      </table>\n" +
            "                    </td>\n" +
            "                  </tr>\n" +
            "                </table>\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "          </table>\n" +
            "        </td>\n" +
            "      </tr>\n" +
            "    </table>\n" +
            "  </div>\n" +
            "</center>\n" +
            "</body>\n" +
            "</html>\n");
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
