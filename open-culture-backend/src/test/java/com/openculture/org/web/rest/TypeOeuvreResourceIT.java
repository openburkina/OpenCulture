package com.openculture.org.web.rest;

import com.openculture.org.OpencultureApp;
import com.openculture.org.domain.TypeOeuvre;
import com.openculture.org.repository.TypeOeuvreRepository;
import com.openculture.org.service.TypeOeuvreService;
import com.openculture.org.service.dto.TypeOeuvreDTO;
import com.openculture.org.service.mapper.TypeOeuvreMapper;

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
 * Integration tests for the {@link TypeOeuvreResource} REST controller.
 */
@SpringBootTest(classes = OpencultureApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class TypeOeuvreResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    @Autowired
    private TypeOeuvreRepository typeOeuvreRepository;

    @Autowired
    private TypeOeuvreMapper typeOeuvreMapper;

    @Autowired
    private TypeOeuvreService typeOeuvreService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeOeuvreMockMvc;

    private TypeOeuvre typeOeuvre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOeuvre createEntity(EntityManager em) {
        TypeOeuvre typeOeuvre = new TypeOeuvre()
            .intitule(DEFAULT_INTITULE);
        return typeOeuvre;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeOeuvre createUpdatedEntity(EntityManager em) {
        TypeOeuvre typeOeuvre = new TypeOeuvre()
            .intitule(UPDATED_INTITULE);
        return typeOeuvre;
    }

    @BeforeEach
    public void initTest() {
        typeOeuvre = createEntity(em);
    }

    @Test
    @Transactional
    public void createTypeOeuvre() throws Exception {
        int databaseSizeBeforeCreate = typeOeuvreRepository.findAll().size();
        // Create the TypeOeuvre
        TypeOeuvreDTO typeOeuvreDTO = typeOeuvreMapper.toDto(typeOeuvre);
        restTypeOeuvreMockMvc.perform(post("/api/type-oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeOeuvreDTO)))
            .andExpect(status().isCreated());

        // Validate the TypeOeuvre in the database
        List<TypeOeuvre> typeOeuvreList = typeOeuvreRepository.findAll();
        assertThat(typeOeuvreList).hasSize(databaseSizeBeforeCreate + 1);
        TypeOeuvre testTypeOeuvre = typeOeuvreList.get(typeOeuvreList.size() - 1);
        assertThat(testTypeOeuvre.getIntitule()).isEqualTo(DEFAULT_INTITULE);
    }

    @Test
    @Transactional
    public void createTypeOeuvreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = typeOeuvreRepository.findAll().size();

        // Create the TypeOeuvre with an existing ID
        typeOeuvre.setId(1L);
        TypeOeuvreDTO typeOeuvreDTO = typeOeuvreMapper.toDto(typeOeuvre);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeOeuvreMockMvc.perform(post("/api/type-oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeOeuvreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeOeuvre in the database
        List<TypeOeuvre> typeOeuvreList = typeOeuvreRepository.findAll();
        assertThat(typeOeuvreList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTypeOeuvres() throws Exception {
        // Initialize the database
        typeOeuvreRepository.saveAndFlush(typeOeuvre);

        // Get all the typeOeuvreList
        restTypeOeuvreMockMvc.perform(get("/api/type-oeuvres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeOeuvre.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)));
    }
    
    @Test
    @Transactional
    public void getTypeOeuvre() throws Exception {
        // Initialize the database
        typeOeuvreRepository.saveAndFlush(typeOeuvre);

        // Get the typeOeuvre
        restTypeOeuvreMockMvc.perform(get("/api/type-oeuvres/{id}", typeOeuvre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeOeuvre.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE));
    }
    @Test
    @Transactional
    public void getNonExistingTypeOeuvre() throws Exception {
        // Get the typeOeuvre
        restTypeOeuvreMockMvc.perform(get("/api/type-oeuvres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeOeuvre() throws Exception {
        // Initialize the database
        typeOeuvreRepository.saveAndFlush(typeOeuvre);

        int databaseSizeBeforeUpdate = typeOeuvreRepository.findAll().size();

        // Update the typeOeuvre
        TypeOeuvre updatedTypeOeuvre = typeOeuvreRepository.findById(typeOeuvre.getId()).get();
        // Disconnect from session so that the updates on updatedTypeOeuvre are not directly saved in db
        em.detach(updatedTypeOeuvre);
        updatedTypeOeuvre
            .intitule(UPDATED_INTITULE);
        TypeOeuvreDTO typeOeuvreDTO = typeOeuvreMapper.toDto(updatedTypeOeuvre);

        restTypeOeuvreMockMvc.perform(put("/api/type-oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeOeuvreDTO)))
            .andExpect(status().isOk());

        // Validate the TypeOeuvre in the database
        List<TypeOeuvre> typeOeuvreList = typeOeuvreRepository.findAll();
        assertThat(typeOeuvreList).hasSize(databaseSizeBeforeUpdate);
        TypeOeuvre testTypeOeuvre = typeOeuvreList.get(typeOeuvreList.size() - 1);
        assertThat(testTypeOeuvre.getIntitule()).isEqualTo(UPDATED_INTITULE);
    }

    @Test
    @Transactional
    public void updateNonExistingTypeOeuvre() throws Exception {
        int databaseSizeBeforeUpdate = typeOeuvreRepository.findAll().size();

        // Create the TypeOeuvre
        TypeOeuvreDTO typeOeuvreDTO = typeOeuvreMapper.toDto(typeOeuvre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeOeuvreMockMvc.perform(put("/api/type-oeuvres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(typeOeuvreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeOeuvre in the database
        List<TypeOeuvre> typeOeuvreList = typeOeuvreRepository.findAll();
        assertThat(typeOeuvreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTypeOeuvre() throws Exception {
        // Initialize the database
        typeOeuvreRepository.saveAndFlush(typeOeuvre);

        int databaseSizeBeforeDelete = typeOeuvreRepository.findAll().size();

        // Delete the typeOeuvre
        restTypeOeuvreMockMvc.perform(delete("/api/type-oeuvres/{id}", typeOeuvre.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeOeuvre> typeOeuvreList = typeOeuvreRepository.findAll();
        assertThat(typeOeuvreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
