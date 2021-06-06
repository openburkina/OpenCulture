package com.openculture.org.service;

import com.openculture.org.domain.Abonnement;
import com.openculture.org.domain.User;
import com.openculture.org.repository.AbonnementRepository;
import com.openculture.org.repository.UserRepository;
import com.openculture.org.security.SecurityUtils;
import com.openculture.org.service.dto.AbonnementDTO;
import com.openculture.org.service.mapper.AbonnementMapper;
import com.openculture.org.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

/**
 * Service Implementation for managing {@link Abonnement}.
 */
@Service
@Transactional
public class AbonnementService {

    private final Logger log = LoggerFactory.getLogger(AbonnementService.class);

    private final AbonnementRepository abonnementRepository;

    private final AbonnementMapper abonnementMapper;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MailService mailService;

    public AbonnementService(AbonnementRepository abonnementRepository, AbonnementMapper abonnementMapper, UserService userService ,UserRepository userRepository,MailService mailService) {
        this.abonnementRepository = abonnementRepository;
        this.abonnementMapper = abonnementMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    /**
     * Save a abonnement.
     *
     * @param abonnementDTO the entity to save.
     * @return the persisted entity.
     */
    public AbonnementDTO save(AbonnementDTO abonnementDTO) {
        log.debug("Request to save Abonnement : {}", abonnementDTO);

      Optional<User>  user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
      if (user.isPresent()){
          Optional<Abonnement> allAbon = abonnementRepository.findByUserIdAndStatutIsTrue(user.get().getId());
          if (allAbon.isPresent()){
              allAbon.get().setStatut(false);
              abonnementRepository.save(allAbon.get());
          }
          abonnementDTO.setDateAbonnement(Instant.now());
          abonnementDTO.setType("test");
          abonnementDTO.setUser(user.get());
          Abonnement abonnement = abonnementMapper.toEntity(abonnementDTO);
          abonnement.setStatut(true);
          abonnement = abonnementRepository.save(abonnement);
          return abonnementMapper.toDto(abonnement);
      }
      return null;
    }

    /**
     * Get all the abonnements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AbonnementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Abonnements");
        return abonnementRepository.findAll(pageable)
            .map(abonnementMapper::toDto);
    }


    /**
     * Get one abonnement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AbonnementDTO> findOne(Long id) {
        log.debug("Request to get Abonnement : {}", id);
        return abonnementRepository.findById(id)
            .map(abonnementMapper::toDto);
    }

    /**
     * Delete the abonnement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Abonnement : {}", id);
        abonnementRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public AbonnementDTO findByUserId(Long id) {
        Optional<Abonnement> abonnement;
        log.debug("Request to get Abonnement : {}", id);
        System.out.println("-------USER ID------ "+id);
        abonnement = abonnementRepository.findByUserIdAndStatutIsTrue(id);
        System.out.println("-------ABONNEMENT------ "+abonnement);
        return abonnementMapper.toDto(abonnement.get());
    }

    @Transactional
    public User sendEmail() {
        Random rnd = new Random();
        int n = rnd.nextInt(900000);
        Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()){
            mailService.sendEmail(user.get().getLogin(),"OpenBurkina"," <!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "    <head>\n" +
                "        <title>Valider votre paiement </title>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "        <link rel=\"icon\" href=\"http://127.0.0.1:4200/favicon.ico\" />\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <p>Cher "+user.get().getLogin() +" </p>\n" +
                "        <p>Votre code de validation est: "+n+"</p>\n" +
                "        <p>\n" +
                "        </p>\n" +
                "        <p>\n" +
                "            <span>Regards,</span>\n" +
                "            <br/>\n" +
                "            <em>openculture.</em>\n" +
                "        </p>\n" +
                "    </body>\n" +
                "</html>",false,true);
        }
        return user.get();
    }
}
