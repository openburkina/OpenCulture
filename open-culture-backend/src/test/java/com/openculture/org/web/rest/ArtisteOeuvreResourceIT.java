package com.openculture.org.web.rest;

import com.openculture.org.OpencultureApp;
import com.openculture.org.domain.ArtisteOeuvre;
import com.openculture.org.repository.ArtisteOeuvreRepository;
import com.openculture.org.service.ArtisteOeuvreService;
import com.openculture.org.service.dto.ArtisteOeuvreDTO;
import com.openculture.org.service.mapper.ArtisteOeuvreMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ArtisteOeuvreResource} REST controller.
 */
@SpringBootTest(classes = OpencultureApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ArtisteOeuvreResourceIT {

    private static final String DEFAULT_ROLE = "AAAAAAAAAA";
    private static final String UPDATED_ROLE = "BBBBBBBBBB";

    @Autowired
    private ArtisteOeuvreRepository artisteOeuvreRepository;

    @Autowired
    private ArtisteOeuvreMapper artisteOeuvreMapper;

    @Autowired
    private ArtisteOeuvreService artisteOeuvreService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArtisteOeuvreMockMvc;

    private ArtisteOeuvre artisteOeuvre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArtisteOeuvre createEntity(EntityManager em) {
        ArtisteOeuvre artisteOeuvre = new ArtisteOeuvre()
            .role(DEFAULT_ROLE);
        return artisteOeuvre;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArtisteOeuvre createUpdatedEntity(EntityManager em) {
        ArtisteOeuvre artisteOeuvre = new ArtisteOeuvre()
            .role(UPDATED_ROLE);
        return artisteOeuvre;
    }

    @BeforeEach
    public void initTest() {
        artisteOeuvre = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtisteOeuvre() throws Exception {
        int databaseSizeBeforeCreate = artisteOeuvreRepository.findAll().size();
        // Create the ArtisteOeuvre
        ArtisteOeuvreDTO artisteOeuvreDTO = artisteOeuvreMapper.toDto(artisteOeuvre);
        restArtisteOeuvreMockMvc.perform(post("/api/artiste-oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artisteOeuvreDTO)))
            .andExpect(status().isCreated());

        // Validate the ArtisteOeuvre in the database
        List<ArtisteOeuvre> artisteOeuvreList = artisteOeuvreRepository.findAll();
        assertThat(artisteOeuvreList).hasSize(databaseSizeBeforeCreate + 1);
        ArtisteOeuvre testArtisteOeuvre = artisteOeuvreList.get(artisteOeuvreList.size() - 1);
        assertThat(testArtisteOeuvre.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createArtisteOeuvreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artisteOeuvreRepository.findAll().size();

        // Create the ArtisteOeuvre with an existing ID
        artisteOeuvre.setId(1L);
        ArtisteOeuvreDTO artisteOeuvreDTO = artisteOeuvreMapper.toDto(artisteOeuvre);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtisteOeuvreMockMvc.perform(post("/api/artiste-oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artisteOeuvreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArtisteOeuvre in the database
        List<ArtisteOeuvre> artisteOeuvreList = artisteOeuvreRepository.findAll();
        assertThat(artisteOeuvreList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllArtisteOeuvres() throws Exception {
        // Initialize the database
        artisteOeuvreRepository.saveAndFlush(artisteOeuvre);

        // Get all the artisteOeuvreList
        restArtisteOeuvreMockMvc.perform(get("/api/artiste-oeuvres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artisteOeuvre.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE)));
    }
    
    @Test
    @Transactional
    public void getArtisteOeuvre() throws Exception {
        // Initialize the database
        artisteOeuvreRepository.saveAndFlush(artisteOeuvre);

        // Get the artisteOeuvre
        restArtisteOeuvreMockMvc.perform(get("/api/artiste-oeuvres/{id}", artisteOeuvre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(artisteOeuvre.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE));
    }
    @Test
    @Transactional
    public void getNonExistingArtisteOeuvre() throws Exception {
        // Get the artisteOeuvre
        restArtisteOeuvreMockMvc.perform(get("/api/artiste-oeuvres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtisteOeuvre() throws Exception {
        // Initialize the database
        artisteOeuvreRepository.saveAndFlush(artisteOeuvre);

        int databaseSizeBeforeUpdate = artisteOeuvreRepository.findAll().size();

        // Update the artisteOeuvre
        ArtisteOeuvre updatedArtisteOeuvre = artisteOeuvreRepository.findById(artisteOeuvre.getId()).get();
        // Disconnect from session so that the updates on updatedArtisteOeuvre are not directly saved in db
        em.detach(updatedArtisteOeuvre);
        updatedArtisteOeuvre
            .role(UPDATED_ROLE);
        ArtisteOeuvreDTO artisteOeuvreDTO = artisteOeuvreMapper.toDto(updatedArtisteOeuvre);

        restArtisteOeuvreMockMvc.perform(put("/api/artiste-oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artisteOeuvreDTO)))
            .andExpect(status().isOk());

        // Validate the ArtisteOeuvre in the database
        List<ArtisteOeuvre> artisteOeuvreList = artisteOeuvreRepository.findAll();
        assertThat(artisteOeuvreList).hasSize(databaseSizeBeforeUpdate);
        ArtisteOeuvre testArtisteOeuvre = artisteOeuvreList.get(artisteOeuvreList.size() - 1);
        assertThat(testArtisteOeuvre.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void updateNonExistingArtisteOeuvre() throws Exception {
        int databaseSizeBeforeUpdate = artisteOeuvreRepository.findAll().size();

        // Create the ArtisteOeuvre
        ArtisteOeuvreDTO artisteOeuvreDTO = artisteOeuvreMapper.toDto(artisteOeuvre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtisteOeuvreMockMvc.perform(put("/api/artiste-oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artisteOeuvreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArtisteOeuvre in the database
        List<ArtisteOeuvre> artisteOeuvreList = artisteOeuvreRepository.findAll();
        assertThat(artisteOeuvreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArtisteOeuvre() throws Exception {
        // Initialize the database
        artisteOeuvreRepository.saveAndFlush(artisteOeuvre);

        int databaseSizeBeforeDelete = artisteOeuvreRepository.findAll().size();

        // Delete the artisteOeuvre
        restArtisteOeuvreMockMvc.perform(delete("/api/artiste-oeuvres/{id}", artisteOeuvre.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArtisteOeuvre> artisteOeuvreList = artisteOeuvreRepository.findAll();
        assertThat(artisteOeuvreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
