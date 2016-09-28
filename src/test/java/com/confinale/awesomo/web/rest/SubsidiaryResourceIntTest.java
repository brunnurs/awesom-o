package com.confinale.awesomo.web.rest;

import com.confinale.awesomo.AwesomoApp;

import com.confinale.awesomo.domain.Subsidiary;
import com.confinale.awesomo.repository.SubsidiaryRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SubsidiaryResource REST controller.
 *
 * @see SubsidiaryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AwesomoApp.class)
public class SubsidiaryResourceIntTest {


    @Inject
    private SubsidiaryRepository subsidiaryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSubsidiaryMockMvc;

    private Subsidiary subsidiary;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SubsidiaryResource subsidiaryResource = new SubsidiaryResource();
        ReflectionTestUtils.setField(subsidiaryResource, "subsidiaryRepository", subsidiaryRepository);
        this.restSubsidiaryMockMvc = MockMvcBuilders.standaloneSetup(subsidiaryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subsidiary createEntity(EntityManager em) {
        Subsidiary subsidiary = new Subsidiary();
        return subsidiary;
    }

    @Before
    public void initTest() {
        subsidiary = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubsidiary() throws Exception {
        int databaseSizeBeforeCreate = subsidiaryRepository.findAll().size();

        // Create the Subsidiary

        restSubsidiaryMockMvc.perform(post("/api/subsidiaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subsidiary)))
                .andExpect(status().isCreated());

        // Validate the Subsidiary in the database
        List<Subsidiary> subsidiaries = subsidiaryRepository.findAll();
        assertThat(subsidiaries).hasSize(databaseSizeBeforeCreate + 1);
        Subsidiary testSubsidiary = subsidiaries.get(subsidiaries.size() - 1);
    }

    @Test
    @Transactional
    public void getAllSubsidiaries() throws Exception {
        // Initialize the database
        subsidiaryRepository.saveAndFlush(subsidiary);

        // Get all the subsidiaries
        restSubsidiaryMockMvc.perform(get("/api/subsidiaries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(subsidiary.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSubsidiary() throws Exception {
        // Initialize the database
        subsidiaryRepository.saveAndFlush(subsidiary);

        // Get the subsidiary
        restSubsidiaryMockMvc.perform(get("/api/subsidiaries/{id}", subsidiary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subsidiary.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSubsidiary() throws Exception {
        // Get the subsidiary
        restSubsidiaryMockMvc.perform(get("/api/subsidiaries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubsidiary() throws Exception {
        // Initialize the database
        subsidiaryRepository.saveAndFlush(subsidiary);
        int databaseSizeBeforeUpdate = subsidiaryRepository.findAll().size();

        // Update the subsidiary
        Subsidiary updatedSubsidiary = subsidiaryRepository.findOne(subsidiary.getId());

        restSubsidiaryMockMvc.perform(put("/api/subsidiaries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSubsidiary)))
                .andExpect(status().isOk());

        // Validate the Subsidiary in the database
        List<Subsidiary> subsidiaries = subsidiaryRepository.findAll();
        assertThat(subsidiaries).hasSize(databaseSizeBeforeUpdate);
        Subsidiary testSubsidiary = subsidiaries.get(subsidiaries.size() - 1);
    }

    @Test
    @Transactional
    public void deleteSubsidiary() throws Exception {
        // Initialize the database
        subsidiaryRepository.saveAndFlush(subsidiary);
        int databaseSizeBeforeDelete = subsidiaryRepository.findAll().size();

        // Get the subsidiary
        restSubsidiaryMockMvc.perform(delete("/api/subsidiaries/{id}", subsidiary.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Subsidiary> subsidiaries = subsidiaryRepository.findAll();
        assertThat(subsidiaries).hasSize(databaseSizeBeforeDelete - 1);
    }
}
