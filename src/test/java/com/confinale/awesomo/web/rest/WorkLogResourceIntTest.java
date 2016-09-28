package com.confinale.awesomo.web.rest;

import com.confinale.awesomo.AwesomoApp;

import com.confinale.awesomo.domain.WorkLog;
import com.confinale.awesomo.domain.User;
import com.confinale.awesomo.domain.Project;
import com.confinale.awesomo.repository.WorkLogRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WorkLogResource REST controller.
 *
 * @see WorkLogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AwesomoApp.class)
public class WorkLogResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Boolean DEFAULT_APPROVED = false;
    private static final Boolean UPDATED_APPROVED = true;

    private static final ZonedDateTime DEFAULT_WORK_FROM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_WORK_FROM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_WORK_FROM_STR = dateTimeFormatter.format(DEFAULT_WORK_FROM);

    private static final ZonedDateTime DEFAULT_WORK_TO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_WORK_TO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_WORK_TO_STR = dateTimeFormatter.format(DEFAULT_WORK_TO);

    @Inject
    private WorkLogRepository workLogRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restWorkLogMockMvc;

    private WorkLog workLog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WorkLogResource workLogResource = new WorkLogResource();
        ReflectionTestUtils.setField(workLogResource, "workLogRepository", workLogRepository);
        this.restWorkLogMockMvc = MockMvcBuilders.standaloneSetup(workLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkLog createEntity(EntityManager em) {
        WorkLog workLog = new WorkLog()
                .approved(DEFAULT_APPROVED)
                .workFrom(DEFAULT_WORK_FROM)
                .workTo(DEFAULT_WORK_TO);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        workLog.setUser(user);
        // Add required entity
        Project project = ProjectResourceIntTest.createEntity(em);
        em.persist(project);
        em.flush();
        workLog.setProject(project);
        return workLog;
    }

    @Before
    public void initTest() {
        workLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkLog() throws Exception {
        int databaseSizeBeforeCreate = workLogRepository.findAll().size();

        // Create the WorkLog

        restWorkLogMockMvc.perform(post("/api/work-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workLog)))
                .andExpect(status().isCreated());

        // Validate the WorkLog in the database
        List<WorkLog> workLogs = workLogRepository.findAll();
        assertThat(workLogs).hasSize(databaseSizeBeforeCreate + 1);
        WorkLog testWorkLog = workLogs.get(workLogs.size() - 1);
        assertThat(testWorkLog.isApproved()).isEqualTo(DEFAULT_APPROVED);
        assertThat(testWorkLog.getWorkFrom()).isEqualTo(DEFAULT_WORK_FROM);
        assertThat(testWorkLog.getWorkTo()).isEqualTo(DEFAULT_WORK_TO);
    }

    @Test
    @Transactional
    public void checkApprovedIsRequired() throws Exception {
        int databaseSizeBeforeTest = workLogRepository.findAll().size();
        // set the field null
        workLog.setApproved(null);

        // Create the WorkLog, which fails.

        restWorkLogMockMvc.perform(post("/api/work-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workLog)))
                .andExpect(status().isBadRequest());

        List<WorkLog> workLogs = workLogRepository.findAll();
        assertThat(workLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWorkFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = workLogRepository.findAll().size();
        // set the field null
        workLog.setWorkFrom(null);

        // Create the WorkLog, which fails.

        restWorkLogMockMvc.perform(post("/api/work-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workLog)))
                .andExpect(status().isBadRequest());

        List<WorkLog> workLogs = workLogRepository.findAll();
        assertThat(workLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWorkToIsRequired() throws Exception {
        int databaseSizeBeforeTest = workLogRepository.findAll().size();
        // set the field null
        workLog.setWorkTo(null);

        // Create the WorkLog, which fails.

        restWorkLogMockMvc.perform(post("/api/work-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workLog)))
                .andExpect(status().isBadRequest());

        List<WorkLog> workLogs = workLogRepository.findAll();
        assertThat(workLogs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWorkLogs() throws Exception {
        // Initialize the database
        workLogRepository.saveAndFlush(workLog);

        // Get all the workLogs
        restWorkLogMockMvc.perform(get("/api/work-logs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(workLog.getId().intValue())))
                .andExpect(jsonPath("$.[*].approved").value(hasItem(DEFAULT_APPROVED.booleanValue())))
                .andExpect(jsonPath("$.[*].workFrom").value(hasItem(DEFAULT_WORK_FROM_STR)))
                .andExpect(jsonPath("$.[*].workTo").value(hasItem(DEFAULT_WORK_TO_STR)));
    }

    @Test
    @Transactional
    public void getWorkLog() throws Exception {
        // Initialize the database
        workLogRepository.saveAndFlush(workLog);

        // Get the workLog
        restWorkLogMockMvc.perform(get("/api/work-logs/{id}", workLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workLog.getId().intValue()))
            .andExpect(jsonPath("$.approved").value(DEFAULT_APPROVED.booleanValue()))
            .andExpect(jsonPath("$.workFrom").value(DEFAULT_WORK_FROM_STR))
            .andExpect(jsonPath("$.workTo").value(DEFAULT_WORK_TO_STR));
    }

    @Test
    @Transactional
    public void getNonExistingWorkLog() throws Exception {
        // Get the workLog
        restWorkLogMockMvc.perform(get("/api/work-logs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkLog() throws Exception {
        // Initialize the database
        workLogRepository.saveAndFlush(workLog);
        int databaseSizeBeforeUpdate = workLogRepository.findAll().size();

        // Update the workLog
        WorkLog updatedWorkLog = workLogRepository.findOne(workLog.getId());
        updatedWorkLog
                .approved(UPDATED_APPROVED)
                .workFrom(UPDATED_WORK_FROM)
                .workTo(UPDATED_WORK_TO);

        restWorkLogMockMvc.perform(put("/api/work-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedWorkLog)))
                .andExpect(status().isOk());

        // Validate the WorkLog in the database
        List<WorkLog> workLogs = workLogRepository.findAll();
        assertThat(workLogs).hasSize(databaseSizeBeforeUpdate);
        WorkLog testWorkLog = workLogs.get(workLogs.size() - 1);
        assertThat(testWorkLog.isApproved()).isEqualTo(UPDATED_APPROVED);
        assertThat(testWorkLog.getWorkFrom()).isEqualTo(UPDATED_WORK_FROM);
        assertThat(testWorkLog.getWorkTo()).isEqualTo(UPDATED_WORK_TO);
    }

    @Test
    @Transactional
    public void deleteWorkLog() throws Exception {
        // Initialize the database
        workLogRepository.saveAndFlush(workLog);
        int databaseSizeBeforeDelete = workLogRepository.findAll().size();

        // Get the workLog
        restWorkLogMockMvc.perform(delete("/api/work-logs/{id}", workLog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkLog> workLogs = workLogRepository.findAll();
        assertThat(workLogs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
