package com.openculture.org.web.rest;

import com.openculture.org.OpencultureApp;
import com.openculture.org.domain.Artiste;
import com.openculture.org.repository.ArtisteRepository;
import com.openculture.org.service.ArtisteService;
import com.openculture.org.service.dto.ArtisteDTO;
import com.openculture.org.service.mapper.ArtisteMapper;

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
 * Integration tests for the {@link ArtisteResource} REST controller.
 */
@SpringBootTest(classes = OpencultureApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ArtisteResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    @Autowired
    private ArtisteRepository artisteRepository;

    @Autowired
    private ArtisteMapper artisteMapper;

    @Autowired
    private ArtisteService artisteService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArtisteMockMvc;

    private Artiste artiste;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artiste createEntity(EntityManager em) {
        Artiste artiste = new Artiste()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM);
        return artiste;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artiste createUpdatedEntity(EntityManager em) {
        Artiste artiste = new Artiste()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM);
        return artiste;
    }

    @BeforeEach
    public void initTest() {
        artiste = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtiste() throws Exception {
        int databaseSizeBeforeCreate = artisteRepository.findAll().size();
        // Create the Artiste
        ArtisteDTO artisteDTO = artisteMapper.toDto(artiste);
        restArtisteMockMvc.perform(post("/api/artistes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artisteDTO)))
            .andExpect(status().isCreated());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeCreate + 1);
        Artiste testArtiste = artisteList.get(artisteList.size() - 1);
        assertThat(testArtiste.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testArtiste.getPrenom()).isEqualTo(DEFAULT_PRENOM);
    }

    @Test
    @Transactional
    public void createArtisteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artisteRepository.findAll().size();

        // Create the Artiste with an existing ID
        artiste.setId(1L);
        ArtisteDTO artisteDTO = artisteMapper.toDto(artiste);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtisteMockMvc.perform(post("/api/artistes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artisteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllArtistes() throws Exception {
        // Initialize the database
        artisteRepository.saveAndFlush(artiste);

        // Get all the artisteList
        restArtisteMockMvc.perform(get("/api/artistes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artiste.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)));
    }
    
    @Test
    @Transactional
    public void getArtiste() throws Exception {
        // Initialize the database
        artisteRepository.saveAndFlush(artiste);

        // Get the artiste
        restArtisteMockMvc.perform(get("/api/artistes/{id}", artiste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(artiste.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM));
    }
    @Test
    @Transactional
    public void getNonExistingArtiste() throws Exception {
        // Get the artiste
        restArtisteMockMvc.perform(get("/api/artistes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtiste() throws Exception {
        // Initialize the database
        artisteRepository.saveAndFlush(artiste);

        int databaseSizeBeforeUpdate = artisteRepository.findAll().size();

        // Update the artiste
        Artiste updatedArtiste = artisteRepository.findById(artiste.getId()).get();
        // Disconnect from session so that the updates on updatedArtiste are not directly saved in db
        em.detach(updatedArtiste);
        updatedArtiste
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM);
        ArtisteDTO artisteDTO = artisteMapper.toDto(updatedArtiste);

        restArtisteMockMvc.perform(put("/api/artistes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artisteDTO)))
            .andExpect(status().isOk());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeUpdate);
        Artiste testArtiste = artisteList.get(artisteList.size() - 1);
        assertThat(testArtiste.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testArtiste.getPrenom()).isEqualTo(UPDATED_PRENOM);
    }

    @Test
    @Transactional
    public void updateNonExistingArtiste() throws Exception {
        int databaseSizeBeforeUpdate = artisteRepository.findAll().size();

        // Create the Artiste
        ArtisteDTO artisteDTO = artisteMapper.toDto(artiste);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtisteMockMvc.perform(put("/api/artistes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(artisteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArtiste() throws Exception {
        // Initialize the database
        artisteRepository.saveAndFlush(artiste);

        int databaseSizeBeforeDelete = artisteRepository.findAll().size();

        // Delete the artiste
        restArtisteMockMvc.perform(delete("/api/artistes/{id}", artiste.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
