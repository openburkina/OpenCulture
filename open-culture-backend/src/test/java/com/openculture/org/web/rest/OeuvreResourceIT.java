package com.openculture.org.web.rest;

import com.openculture.org.OpencultureApp;
import com.openculture.org.domain.Oeuvre;
import com.openculture.org.repository.OeuvreRepository;
import com.openculture.org.service.OeuvreService;
import com.openculture.org.service.dto.OeuvreDTO;
import com.openculture.org.service.mapper.OeuvreMapper;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OeuvreResource} REST controller.
 */
@SpringBootTest(classes = OpencultureApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class OeuvreResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_SORTIE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_SORTIE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private OeuvreRepository oeuvreRepository;

    @Autowired
    private OeuvreMapper oeuvreMapper;

    @Autowired
    private OeuvreService oeuvreService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOeuvreMockMvc;

    private Oeuvre oeuvre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Oeuvre createEntity(EntityManager em) {
        Oeuvre oeuvre = new Oeuvre()
            .titre(DEFAULT_TITRE)
            .dateSortie(DEFAULT_DATE_SORTIE);
        return oeuvre;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Oeuvre createUpdatedEntity(EntityManager em) {
        Oeuvre oeuvre = new Oeuvre()
            .titre(UPDATED_TITRE)
            .dateSortie(UPDATED_DATE_SORTIE);
        return oeuvre;
    }

    @BeforeEach
    public void initTest() {
        oeuvre = createEntity(em);
    }

    @Test
    @Transactional
    public void createOeuvre() throws Exception {
        int databaseSizeBeforeCreate = oeuvreRepository.findAll().size();
        // Create the Oeuvre
        OeuvreDTO oeuvreDTO = oeuvreMapper.toDto(oeuvre);
        restOeuvreMockMvc.perform(post("/api/oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oeuvreDTO)))
            .andExpect(status().isCreated());

        // Validate the Oeuvre in the database
        List<Oeuvre> oeuvreList = oeuvreRepository.findAll();
        assertThat(oeuvreList).hasSize(databaseSizeBeforeCreate + 1);
        Oeuvre testOeuvre = oeuvreList.get(oeuvreList.size() - 1);
        assertThat(testOeuvre.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testOeuvre.getDateSortie()).isEqualTo(DEFAULT_DATE_SORTIE);
    }

    @Test
    @Transactional
    public void createOeuvreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = oeuvreRepository.findAll().size();

        // Create the Oeuvre with an existing ID
        oeuvre.setId(1L);
        OeuvreDTO oeuvreDTO = oeuvreMapper.toDto(oeuvre);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOeuvreMockMvc.perform(post("/api/oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oeuvreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Oeuvre in the database
        List<Oeuvre> oeuvreList = oeuvreRepository.findAll();
        assertThat(oeuvreList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOeuvres() throws Exception {
        // Initialize the database
        oeuvreRepository.saveAndFlush(oeuvre);

        // Get all the oeuvreList
        restOeuvreMockMvc.perform(get("/api/oeuvres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oeuvre.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].dateSortie").value(hasItem(DEFAULT_DATE_SORTIE.toString())));
    }
    
    @Test
    @Transactional
    public void getOeuvre() throws Exception {
        // Initialize the database
        oeuvreRepository.saveAndFlush(oeuvre);

        // Get the oeuvre
        restOeuvreMockMvc.perform(get("/api/oeuvres/{id}", oeuvre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oeuvre.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.dateSortie").value(DEFAULT_DATE_SORTIE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingOeuvre() throws Exception {
        // Get the oeuvre
        restOeuvreMockMvc.perform(get("/api/oeuvres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOeuvre() throws Exception {
        // Initialize the database
        oeuvreRepository.saveAndFlush(oeuvre);

        int databaseSizeBeforeUpdate = oeuvreRepository.findAll().size();

        // Update the oeuvre
        Oeuvre updatedOeuvre = oeuvreRepository.findById(oeuvre.getId()).get();
        // Disconnect from session so that the updates on updatedOeuvre are not directly saved in db
        em.detach(updatedOeuvre);
        updatedOeuvre
            .titre(UPDATED_TITRE)
            .dateSortie(UPDATED_DATE_SORTIE);
        OeuvreDTO oeuvreDTO = oeuvreMapper.toDto(updatedOeuvre);

        restOeuvreMockMvc.perform(put("/api/oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oeuvreDTO)))
            .andExpect(status().isOk());

        // Validate the Oeuvre in the database
        List<Oeuvre> oeuvreList = oeuvreRepository.findAll();
        assertThat(oeuvreList).hasSize(databaseSizeBeforeUpdate);
        Oeuvre testOeuvre = oeuvreList.get(oeuvreList.size() - 1);
        assertThat(testOeuvre.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testOeuvre.getDateSortie()).isEqualTo(UPDATED_DATE_SORTIE);
    }

    @Test
    @Transactional
    public void updateNonExistingOeuvre() throws Exception {
        int databaseSizeBeforeUpdate = oeuvreRepository.findAll().size();

        // Create the Oeuvre
        OeuvreDTO oeuvreDTO = oeuvreMapper.toDto(oeuvre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOeuvreMockMvc.perform(put("/api/oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(oeuvreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Oeuvre in the database
        List<Oeuvre> oeuvreList = oeuvreRepository.findAll();
        assertThat(oeuvreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOeuvre() throws Exception {
        // Initialize the database
        oeuvreRepository.saveAndFlush(oeuvre);

        int databaseSizeBeforeDelete = oeuvreRepository.findAll().size();

        // Delete the oeuvre
        restOeuvreMockMvc.perform(delete("/api/oeuvres/{id}", oeuvre.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Oeuvre> oeuvreList = oeuvreRepository.findAll();
        assertThat(oeuvreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
