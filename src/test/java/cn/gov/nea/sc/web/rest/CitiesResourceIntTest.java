package cn.gov.nea.sc.web.rest;

import cn.gov.nea.sc.NearepApp;

import cn.gov.nea.sc.domain.Cities;
import cn.gov.nea.sc.domain.Provinces;
import cn.gov.nea.sc.repository.CitiesRepository;
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
 * Test class for the CitiesResource REST controller.
 *
 * @see CitiesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NearepApp.class)
public class CitiesResourceIntTest {

    private static final String DEFAULT_CITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CITY_NAME = "BBBBBBBBBB";

    @Autowired
    private CitiesRepository citiesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCitiesMockMvc;

    private Cities cities;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CitiesResource citiesResource = new CitiesResource(citiesRepository);
        this.restCitiesMockMvc = MockMvcBuilders.standaloneSetup(citiesResource)
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
    public static Cities createEntity(EntityManager em) {
        Cities cities = new Cities()
            .cityName(DEFAULT_CITY_NAME);
        // Add required entity
        Provinces provinceName = ProvincesResourceIntTest.createEntity(em);
        em.persist(provinceName);
        em.flush();
        cities.setProvinceName(provinceName);
        return cities;
    }

    @Before
    public void initTest() {
        cities = createEntity(em);
    }

    @Test
    @Transactional
    public void createCities() throws Exception {
        int databaseSizeBeforeCreate = citiesRepository.findAll().size();

        // Create the Cities
        restCitiesMockMvc.perform(post("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cities)))
            .andExpect(status().isCreated());

        // Validate the Cities in the database
        List<Cities> citiesList = citiesRepository.findAll();
        assertThat(citiesList).hasSize(databaseSizeBeforeCreate + 1);
        Cities testCities = citiesList.get(citiesList.size() - 1);
        assertThat(testCities.getCityName()).isEqualTo(DEFAULT_CITY_NAME);
    }

    @Test
    @Transactional
    public void createCitiesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = citiesRepository.findAll().size();

        // Create the Cities with an existing ID
        cities.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCitiesMockMvc.perform(post("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cities)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Cities> citiesList = citiesRepository.findAll();
        assertThat(citiesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = citiesRepository.findAll().size();
        // set the field null
        cities.setCityName(null);

        // Create the Cities, which fails.

        restCitiesMockMvc.perform(post("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cities)))
            .andExpect(status().isBadRequest());

        List<Cities> citiesList = citiesRepository.findAll();
        assertThat(citiesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCities() throws Exception {
        // Initialize the database
        citiesRepository.saveAndFlush(cities);

        // Get all the citiesList
        restCitiesMockMvc.perform(get("/api/cities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cities.getId().intValue())))
            .andExpect(jsonPath("$.[*].cityName").value(hasItem(DEFAULT_CITY_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCities() throws Exception {
        // Initialize the database
        citiesRepository.saveAndFlush(cities);

        // Get the cities
        restCitiesMockMvc.perform(get("/api/cities/{id}", cities.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cities.getId().intValue()))
            .andExpect(jsonPath("$.cityName").value(DEFAULT_CITY_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCities() throws Exception {
        // Get the cities
        restCitiesMockMvc.perform(get("/api/cities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCities() throws Exception {
        // Initialize the database
        citiesRepository.saveAndFlush(cities);
        int databaseSizeBeforeUpdate = citiesRepository.findAll().size();

        // Update the cities
        Cities updatedCities = citiesRepository.findOne(cities.getId());
        updatedCities
            .cityName(UPDATED_CITY_NAME);

        restCitiesMockMvc.perform(put("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCities)))
            .andExpect(status().isOk());

        // Validate the Cities in the database
        List<Cities> citiesList = citiesRepository.findAll();
        assertThat(citiesList).hasSize(databaseSizeBeforeUpdate);
        Cities testCities = citiesList.get(citiesList.size() - 1);
        assertThat(testCities.getCityName()).isEqualTo(UPDATED_CITY_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCities() throws Exception {
        int databaseSizeBeforeUpdate = citiesRepository.findAll().size();

        // Create the Cities

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCitiesMockMvc.perform(put("/api/cities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cities)))
            .andExpect(status().isCreated());

        // Validate the Cities in the database
        List<Cities> citiesList = citiesRepository.findAll();
        assertThat(citiesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCities() throws Exception {
        // Initialize the database
        citiesRepository.saveAndFlush(cities);
        int databaseSizeBeforeDelete = citiesRepository.findAll().size();

        // Get the cities
        restCitiesMockMvc.perform(delete("/api/cities/{id}", cities.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cities> citiesList = citiesRepository.findAll();
        assertThat(citiesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cities.class);
        Cities cities1 = new Cities();
        cities1.setId(1L);
        Cities cities2 = new Cities();
        cities2.setId(cities1.getId());
        assertThat(cities1).isEqualTo(cities2);
        cities2.setId(2L);
        assertThat(cities1).isNotEqualTo(cities2);
        cities1.setId(null);
        assertThat(cities1).isNotEqualTo(cities2);
    }
}
