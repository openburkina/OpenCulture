package com.openculture.org.web.rest;

import com.openculture.org.OpencultureApp;
import com.openculture.org.domain.InformationCivil;
import com.openculture.org.repository.InformationCivilRepository;
import com.openculture.org.service.InformationCivilService;
import com.openculture.org.service.dto.InformationCivilDTO;
import com.openculture.org.service.mapper.InformationCivilMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link InformationCivilResource} REST controller.
 */
@SpringBootTest(classes = OpencultureApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class InformationCivilResourceIT {

    private static final String DEFAULT_NATIONALITE = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIEU_NAISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISSANCE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_P = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_P = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_S = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_S = "BBBBBBBBBB";

    @Autowired
    private InformationCivilRepository informationCivilRepository;

    @Autowired
    private InformationCivilMapper informationCivilMapper;

    @Autowired
    private InformationCivilService informationCivilService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInformationCivilMockMvc;

    private InformationCivil informationCivil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InformationCivil createEntity(EntityManager em) {
        InformationCivil informationCivil = new InformationCivil()
            .nationalite(DEFAULT_NATIONALITE)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .lieuNaissance(DEFAULT_LIEU_NAISSANCE)
            .numeroP(DEFAULT_NUMERO_P)
            .numeroS(DEFAULT_NUMERO_S);
        return informationCivil;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InformationCivil createUpdatedEntity(EntityManager em) {
        InformationCivil informationCivil = new InformationCivil()
            .nationalite(UPDATED_NATIONALITE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .numeroP(UPDATED_NUMERO_P)
            .numeroS(UPDATED_NUMERO_S);
        return informationCivil;
    }

    @BeforeEach
    public void initTest() {
        informationCivil = createEntity(em);
    }

    @Test
    @Transactional
    public void createInformationCivil() throws Exception {
        int databaseSizeBeforeCreate = informationCivilRepository.findAll().size();
        // Create the InformationCivil
        InformationCivilDTO informationCivilDTO = informationCivilMapper.toDto(informationCivil);
        restInformationCivilMockMvc.perform(post("/api/information-civils")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(informationCivilDTO)))
            .andExpect(status().isCreated());

        // Validate the InformationCivil in the database
        List<InformationCivil> informationCivilList = informationCivilRepository.findAll();
        assertThat(informationCivilList).hasSize(databaseSizeBeforeCreate + 1);
        InformationCivil testInformationCivil = informationCivilList.get(informationCivilList.size() - 1);
        assertThat(testInformationCivil.getNationalite()).isEqualTo(DEFAULT_NATIONALITE);
        assertThat(testInformationCivil.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testInformationCivil.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
        assertThat(testInformationCivil.getNumeroP()).isEqualTo(DEFAULT_NUMERO_P);
        assertThat(testInformationCivil.getNumeroS()).isEqualTo(DEFAULT_NUMERO_S);
    }

    @Test
    @Transactional
    public void createInformationCivilWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = informationCivilRepository.findAll().size();

        // Create the InformationCivil with an existing ID
        informationCivil.setId(1L);
        InformationCivilDTO informationCivilDTO = informationCivilMapper.toDto(informationCivil);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInformationCivilMockMvc.perform(post("/api/information-civils")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(informationCivilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InformationCivil in the database
        List<InformationCivil> informationCivilList = informationCivilRepository.findAll();
        assertThat(informationCivilList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInformationCivils() throws Exception {
        // Initialize the database
        informationCivilRepository.saveAndFlush(informationCivil);

        // Get all the informationCivilList
        restInformationCivilMockMvc.perform(get("/api/information-civils?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(informationCivil.getId().intValue())))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE)))
            .andExpect(jsonPath("$.[*].numeroP").value(hasItem(DEFAULT_NUMERO_P)))
            .andExpect(jsonPath("$.[*].numeroS").value(hasItem(DEFAULT_NUMERO_S)));
    }
    
    @Test
    @Transactional
    public void getInformationCivil() throws Exception {
        // Initialize the database
        informationCivilRepository.saveAndFlush(informationCivil);

        // Get the informationCivil
        restInformationCivilMockMvc.perform(get("/api/information-civils/{id}", informationCivil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(informationCivil.getId().intValue()))
            .andExpect(jsonPath("$.nationalite").value(DEFAULT_NATIONALITE))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.lieuNaissance").value(DEFAULT_LIEU_NAISSANCE))
            .andExpect(jsonPath("$.numeroP").value(DEFAULT_NUMERO_P))
            .andExpect(jsonPath("$.numeroS").value(DEFAULT_NUMERO_S));
    }
    @Test
    @Transactional
    public void getNonExistingInformationCivil() throws Exception {
        // Get the informationCivil
        restInformationCivilMockMvc.perform(get("/api/information-civils/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInformationCivil() throws Exception {
        // Initialize the database
        informationCivilRepository.saveAndFlush(informationCivil);

        int databaseSizeBeforeUpdate = informationCivilRepository.findAll().size();

        // Update the informationCivil
        InformationCivil updatedInformationCivil = informationCivilRepository.findById(informationCivil.getId()).get();
        // Disconnect from session so that the updates on updatedInformationCivil are not directly saved in db
        em.detach(updatedInformationCivil);
        updatedInformationCivil
            .nationalite(UPDATED_NATIONALITE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .numeroP(UPDATED_NUMERO_P)
            .numeroS(UPDATED_NUMERO_S);
        InformationCivilDTO informationCivilDTO = informationCivilMapper.toDto(updatedInformationCivil);

        restInformationCivilMockMvc.perform(put("/api/information-civils")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(informationCivilDTO)))
            .andExpect(status().isOk());

        // Validate the InformationCivil in the database
        List<InformationCivil> informationCivilList = informationCivilRepository.findAll();
        assertThat(informationCivilList).hasSize(databaseSizeBeforeUpdate);
        InformationCivil testInformationCivil = informationCivilList.get(informationCivilList.size() - 1);
        assertThat(testInformationCivil.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testInformationCivil.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testInformationCivil.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testInformationCivil.getNumeroP()).isEqualTo(UPDATED_NUMERO_P);
        assertThat(testInformationCivil.getNumeroS()).isEqualTo(UPDATED_NUMERO_S);
    }

    @Test
    @Transactional
    public void updateNonExistingInformationCivil() throws Exception {
        int databaseSizeBeforeUpdate = informationCivilRepository.findAll().size();

        // Create the InformationCivil
        InformationCivilDTO informationCivilDTO = informationCivilMapper.toDto(informationCivil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformationCivilMockMvc.perform(put("/api/information-civils")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(informationCivilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InformationCivil in the database
        List<InformationCivil> informationCivilList = informationCivilRepository.findAll();
        assertThat(informationCivilList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInformationCivil() throws Exception {
        // Initialize the database
        informationCivilRepository.saveAndFlush(informationCivil);

        int databaseSizeBeforeDelete = informationCivilRepository.findAll().size();

        // Delete the informationCivil
        restInformationCivilMockMvc.perform(delete("/api/information-civils/{id}", informationCivil.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InformationCivil> informationCivilList = informationCivilRepository.findAll();
        assertThat(informationCivilList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
