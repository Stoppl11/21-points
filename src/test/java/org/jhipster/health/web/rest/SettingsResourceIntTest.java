package org.jhipster.health.web.rest;

import org.jhipster.health.Application;

import org.jhipster.health.domain.Settings;
import org.jhipster.health.repository.SettingsRepository;
import org.jhipster.health.repository.search.SettingsSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.jhipster.health.domain.enumeration.Units;
/**
 * Test class for the SettingsResource REST controller.
 *
 * @see SettingsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SettingsResourceIntTest {

    private static final Integer DEFAULT_WEEKLYGOAL = 1;
    private static final Integer UPDATED_WEEKLYGOAL = 2;

    private static final Units DEFAULT_WEIGHTUNTITS = Units.kg;
    private static final Units UPDATED_WEIGHTUNTITS = Units.lb;

    @Inject
    private SettingsRepository settingsRepository;

    @Inject
    private SettingsSearchRepository settingsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSettingsMockMvc;

    private Settings settings;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SettingsResource settingsResource = new SettingsResource();
        ReflectionTestUtils.setField(settingsResource, "settingsSearchRepository", settingsSearchRepository);
        ReflectionTestUtils.setField(settingsResource, "settingsRepository", settingsRepository);
        this.restSettingsMockMvc = MockMvcBuilders.standaloneSetup(settingsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Settings createEntity(EntityManager em) {
        Settings settings = new Settings()
                .weeklygoal(DEFAULT_WEEKLYGOAL)
                .weightuntits(DEFAULT_WEIGHTUNTITS);
        return settings;
    }

    @Before
    public void initTest() {
        settingsSearchRepository.deleteAll();
        settings = createEntity(em);
    }

    @Test
    @Transactional
    public void createSettings() throws Exception {
        int databaseSizeBeforeCreate = settingsRepository.findAll().size();

        // Create the Settings

        restSettingsMockMvc.perform(post("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(settings)))
            .andExpect(status().isCreated());

        // Validate the Settings in the database
        List<Settings> settingsList = settingsRepository.findAll();
        assertThat(settingsList).hasSize(databaseSizeBeforeCreate + 1);
        Settings testSettings = settingsList.get(settingsList.size() - 1);
        assertThat(testSettings.getWeeklygoal()).isEqualTo(DEFAULT_WEEKLYGOAL);
        assertThat(testSettings.getWeightuntits()).isEqualTo(DEFAULT_WEIGHTUNTITS);

        // Validate the Settings in ElasticSearch
        Settings settingsEs = settingsSearchRepository.findOne(testSettings.getId());
        assertThat(settingsEs).isEqualToComparingFieldByField(testSettings);
    }

    @Test
    @Transactional
    public void createSettingsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = settingsRepository.findAll().size();

        // Create the Settings with an existing ID
        Settings existingSettings = new Settings();
        existingSettings.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSettingsMockMvc.perform(post("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingSettings)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Settings> settingsList = settingsRepository.findAll();
        assertThat(settingsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkWeightuntitsIsRequired() throws Exception {
        int databaseSizeBeforeTest = settingsRepository.findAll().size();
        // set the field null
        settings.setWeightuntits(null);

        // Create the Settings, which fails.

        restSettingsMockMvc.perform(post("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(settings)))
            .andExpect(status().isBadRequest());

        List<Settings> settingsList = settingsRepository.findAll();
        assertThat(settingsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSettings() throws Exception {
        // Initialize the database
        settingsRepository.saveAndFlush(settings);

        // Get all the settingsList
        restSettingsMockMvc.perform(get("/api/settings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settings.getId().intValue())))
            .andExpect(jsonPath("$.[*].weeklygoal").value(hasItem(DEFAULT_WEEKLYGOAL)))
            .andExpect(jsonPath("$.[*].weightuntits").value(hasItem(DEFAULT_WEIGHTUNTITS.toString())));
    }

    @Test
    @Transactional
    public void getSettings() throws Exception {
        // Initialize the database
        settingsRepository.saveAndFlush(settings);

        // Get the settings
        restSettingsMockMvc.perform(get("/api/settings/{id}", settings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(settings.getId().intValue()))
            .andExpect(jsonPath("$.weeklygoal").value(DEFAULT_WEEKLYGOAL))
            .andExpect(jsonPath("$.weightuntits").value(DEFAULT_WEIGHTUNTITS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSettings() throws Exception {
        // Get the settings
        restSettingsMockMvc.perform(get("/api/settings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSettings() throws Exception {
        // Initialize the database
        settingsRepository.saveAndFlush(settings);
        settingsSearchRepository.save(settings);
        int databaseSizeBeforeUpdate = settingsRepository.findAll().size();

        // Update the settings
        Settings updatedSettings = settingsRepository.findOne(settings.getId());
        updatedSettings
                .weeklygoal(UPDATED_WEEKLYGOAL)
                .weightuntits(UPDATED_WEIGHTUNTITS);

        restSettingsMockMvc.perform(put("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSettings)))
            .andExpect(status().isOk());

        // Validate the Settings in the database
        List<Settings> settingsList = settingsRepository.findAll();
        assertThat(settingsList).hasSize(databaseSizeBeforeUpdate);
        Settings testSettings = settingsList.get(settingsList.size() - 1);
        assertThat(testSettings.getWeeklygoal()).isEqualTo(UPDATED_WEEKLYGOAL);
        assertThat(testSettings.getWeightuntits()).isEqualTo(UPDATED_WEIGHTUNTITS);

        // Validate the Settings in ElasticSearch
        Settings settingsEs = settingsSearchRepository.findOne(testSettings.getId());
        assertThat(settingsEs).isEqualToComparingFieldByField(testSettings);
    }

    @Test
    @Transactional
    public void updateNonExistingSettings() throws Exception {
        int databaseSizeBeforeUpdate = settingsRepository.findAll().size();

        // Create the Settings

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSettingsMockMvc.perform(put("/api/settings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(settings)))
            .andExpect(status().isCreated());

        // Validate the Settings in the database
        List<Settings> settingsList = settingsRepository.findAll();
        assertThat(settingsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSettings() throws Exception {
        // Initialize the database
        settingsRepository.saveAndFlush(settings);
        settingsSearchRepository.save(settings);
        int databaseSizeBeforeDelete = settingsRepository.findAll().size();

        // Get the settings
        restSettingsMockMvc.perform(delete("/api/settings/{id}", settings.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean settingsExistsInEs = settingsSearchRepository.exists(settings.getId());
        assertThat(settingsExistsInEs).isFalse();

        // Validate the database is empty
        List<Settings> settingsList = settingsRepository.findAll();
        assertThat(settingsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSettings() throws Exception {
        // Initialize the database
        settingsRepository.saveAndFlush(settings);
        settingsSearchRepository.save(settings);

        // Search the settings
        restSettingsMockMvc.perform(get("/api/_search/settings?query=id:" + settings.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(settings.getId().intValue())))
            .andExpect(jsonPath("$.[*].weeklygoal").value(hasItem(DEFAULT_WEEKLYGOAL)))
            .andExpect(jsonPath("$.[*].weightuntits").value(hasItem(DEFAULT_WEIGHTUNTITS.toString())));
    }
}
