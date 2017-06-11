package cn.gov.nea.sc.web.rest;

import cn.gov.nea.sc.NearepApp;

import cn.gov.nea.sc.domain.Districts;
import cn.gov.nea.sc.domain.Cities;
import cn.gov.nea.sc.repository.DistrictsRepository;
import cn.gov.nea.sc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DistrictsResource REST controller.
 *
 * @see DistrictsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NearepApp.class)
public class DistrictsResourceIntTest {

    private static final String DEFAULT_DISTRICT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POST_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POST_CODE = "BBBBBBBBBB";

    @Autowired
    private DistrictsRepository districtsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDistrictsMockMvc;

    private Districts districts;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DistrictsResource districtsResource = new DistrictsResource(districtsRepository);
        this.restDistrictsMockMvc = MockMvcBuilders.standaloneSetup(districtsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Districts createEntity(EntityManager em) {
        Districts districts = new Districts()
            .districtName(DEFAULT_DISTRICT_NAME)
            .postCode(DEFAULT_POST_CODE);
        // Add required entity
        Cities cityName = CitiesResourceIntTest.createEntity(em);
        em.persist(cityName);
        em.flush();
        districts.setCityName(cityName);
        return districts;
    }

    @Before
    public void initTest() {
        districts = createEntity(em);
    }

    @Test
    @Transactional
    public void createDistricts() throws Exception {
        int databaseSizeBeforeCreate = districtsRepository.findAll().size();

        // Create the Districts
        restDistrictsMockMvc.perform(post("/api/districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(districts)))
            .andExpect(status().isCreated());

        // Validate the Districts in the database
        List<Districts> districtsList = districtsRepository.findAll();
        assertThat(districtsList).hasSize(databaseSizeBeforeCreate + 1);
        Districts testDistricts = districtsList.get(districtsList.size() - 1);
        assertThat(testDistricts.getDistrictName()).isEqualTo(DEFAULT_DISTRICT_NAME);
        assertThat(testDistricts.getPostCode()).isEqualTo(DEFAULT_POST_CODE);
    }

    @Test
    @Transactional
    public void createDistrictsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = districtsRepository.findAll().size();

        // Create the Districts with an existing ID
        districts.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDistrictsMockMvc.perform(post("/api/districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(districts)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Districts> districtsList = districtsRepository.findAll();
        assertThat(districtsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDistrictNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtsRepository.findAll().size();
        // set the field null
        districts.setDistrictName(null);

        // Create the Districts, which fails.

        restDistrictsMockMvc.perform(post("/api/districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(districts)))
            .andExpect(status().isBadRequest());

        List<Districts> districtsList = districtsRepository.findAll();
        assertThat(districtsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = districtsRepository.findAll().size();
        // set the field null
        districts.setPostCode(null);

        // Create the Districts, which fails.

        restDistrictsMockMvc.perform(post("/api/districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(districts)))
            .andExpect(status().isBadRequest());

        List<Districts> districtsList = districtsRepository.findAll();
        assertThat(districtsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDistricts() throws Exception {
        // Initialize the database
        districtsRepository.saveAndFlush(districts);

        // Get all the districtsList
        restDistrictsMockMvc.perform(get("/api/districts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(districts.getId().intValue())))
            .andExpect(jsonPath("$.[*].districtName").value(hasItem(DEFAULT_DISTRICT_NAME.toString())))
            .andExpect(jsonPath("$.[*].postCode").value(hasItem(DEFAULT_POST_CODE.toString())));
    }

    @Test
    @Transactional
    public void getDistricts() throws Exception {
        // Initialize the database
        districtsRepository.saveAndFlush(districts);

        // Get the districts
        restDistrictsMockMvc.perform(get("/api/districts/{id}", districts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(districts.getId().intValue()))
            .andExpect(jsonPath("$.districtName").value(DEFAULT_DISTRICT_NAME.toString()))
            .andExpect(jsonPath("$.postCode").value(DEFAULT_POST_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDistricts() throws Exception {
        // Get the districts
        restDistrictsMockMvc.perform(get("/api/districts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDistricts() throws Exception {
        // Initialize the database
        districtsRepository.saveAndFlush(districts);
        int databaseSizeBeforeUpdate = districtsRepository.findAll().size();

        // Update the districts
        Districts updatedDistricts = districtsRepository.findOne(districts.getId());
        updatedDistricts
            .districtName(UPDATED_DISTRICT_NAME)
            .postCode(UPDATED_POST_CODE);

        restDistrictsMockMvc.perform(put("/api/districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDistricts)))
            .andExpect(status().isOk());

        // Validate the Districts in the database
        List<Districts> districtsList = districtsRepository.findAll();
        assertThat(districtsList).hasSize(databaseSizeBeforeUpdate);
        Districts testDistricts = districtsList.get(districtsList.size() - 1);
        assertThat(testDistricts.getDistrictName()).isEqualTo(UPDATED_DISTRICT_NAME);
        assertThat(testDistricts.getPostCode()).isEqualTo(UPDATED_POST_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingDistricts() throws Exception {
        int databaseSizeBeforeUpdate = districtsRepository.findAll().size();

        // Create the Districts

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDistrictsMockMvc.perform(put("/api/districts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(districts)))
            .andExpect(status().isCreated());

        // Validate the Districts in the database
        List<Districts> districtsList = districtsRepository.findAll();
        assertThat(districtsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDistricts() throws Exception {
        // Initialize the database
        districtsRepository.saveAndFlush(districts);
        int databaseSizeBeforeDelete = districtsRepository.findAll().size();

        // Get the districts
        restDistrictsMockMvc.perform(delete("/api/districts/{id}", districts.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Districts> districtsList = districtsRepository.findAll();
        assertThat(districtsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Districts.class);
        Districts districts1 = new Districts();
        districts1.setId(1L);
        Districts districts2 = new Districts();
        districts2.setId(districts1.getId());
        assertThat(districts1).isEqualTo(districts2);
        districts2.setId(2L);
        assertThat(districts1).isNotEqualTo(districts2);
        districts1.setId(null);
        assertThat(districts1).isNotEqualTo(districts2);
    }
}
