package cn.gov.nea.sc.web.rest;

import cn.gov.nea.sc.NearepApp;

import cn.gov.nea.sc.domain.Provinces;
import cn.gov.nea.sc.domain.Countries;
import cn.gov.nea.sc.repository.ProvincesRepository;
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
 * Test class for the ProvincesResource REST controller.
 *
 * @see ProvincesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NearepApp.class)
public class ProvincesResourceIntTest {

    private static final String DEFAULT_PROVINCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE_NAME = "BBBBBBBBBB";

    @Autowired
    private ProvincesRepository provincesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProvincesMockMvc;

    private Provinces provinces;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProvincesResource provincesResource = new ProvincesResource(provincesRepository);
        this.restProvincesMockMvc = MockMvcBuilders.standaloneSetup(provincesResource)
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
    public static Provinces createEntity(EntityManager em) {
        Provinces provinces = new Provinces()
            .provinceName(DEFAULT_PROVINCE_NAME);
        // Add required entity
        Countries countryName = CountriesResourceIntTest.createEntity(em);
        em.persist(countryName);
        em.flush();
        provinces.setCountryName(countryName);
        return provinces;
    }

    @Before
    public void initTest() {
        provinces = createEntity(em);
    }

    @Test
    @Transactional
    public void createProvinces() throws Exception {
        int databaseSizeBeforeCreate = provincesRepository.findAll().size();

        // Create the Provinces
        restProvincesMockMvc.perform(post("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinces)))
            .andExpect(status().isCreated());

        // Validate the Provinces in the database
        List<Provinces> provincesList = provincesRepository.findAll();
        assertThat(provincesList).hasSize(databaseSizeBeforeCreate + 1);
        Provinces testProvinces = provincesList.get(provincesList.size() - 1);
        assertThat(testProvinces.getProvinceName()).isEqualTo(DEFAULT_PROVINCE_NAME);
    }

    @Test
    @Transactional
    public void createProvincesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = provincesRepository.findAll().size();

        // Create the Provinces with an existing ID
        provinces.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProvincesMockMvc.perform(post("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinces)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Provinces> provincesList = provincesRepository.findAll();
        assertThat(provincesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProvinceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = provincesRepository.findAll().size();
        // set the field null
        provinces.setProvinceName(null);

        // Create the Provinces, which fails.

        restProvincesMockMvc.perform(post("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinces)))
            .andExpect(status().isBadRequest());

        List<Provinces> provincesList = provincesRepository.findAll();
        assertThat(provincesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProvinces() throws Exception {
        // Initialize the database
        provincesRepository.saveAndFlush(provinces);

        // Get all the provincesList
        restProvincesMockMvc.perform(get("/api/provinces?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(provinces.getId().intValue())))
            .andExpect(jsonPath("$.[*].provinceName").value(hasItem(DEFAULT_PROVINCE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getProvinces() throws Exception {
        // Initialize the database
        provincesRepository.saveAndFlush(provinces);

        // Get the provinces
        restProvincesMockMvc.perform(get("/api/provinces/{id}", provinces.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(provinces.getId().intValue()))
            .andExpect(jsonPath("$.provinceName").value(DEFAULT_PROVINCE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProvinces() throws Exception {
        // Get the provinces
        restProvincesMockMvc.perform(get("/api/provinces/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProvinces() throws Exception {
        // Initialize the database
        provincesRepository.saveAndFlush(provinces);
        int databaseSizeBeforeUpdate = provincesRepository.findAll().size();

        // Update the provinces
        Provinces updatedProvinces = provincesRepository.findOne(provinces.getId());
        updatedProvinces
            .provinceName(UPDATED_PROVINCE_NAME);

        restProvincesMockMvc.perform(put("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProvinces)))
            .andExpect(status().isOk());

        // Validate the Provinces in the database
        List<Provinces> provincesList = provincesRepository.findAll();
        assertThat(provincesList).hasSize(databaseSizeBeforeUpdate);
        Provinces testProvinces = provincesList.get(provincesList.size() - 1);
        assertThat(testProvinces.getProvinceName()).isEqualTo(UPDATED_PROVINCE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingProvinces() throws Exception {
        int databaseSizeBeforeUpdate = provincesRepository.findAll().size();

        // Create the Provinces

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProvincesMockMvc.perform(put("/api/provinces")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(provinces)))
            .andExpect(status().isCreated());

        // Validate the Provinces in the database
        List<Provinces> provincesList = provincesRepository.findAll();
        assertThat(provincesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProvinces() throws Exception {
        // Initialize the database
        provincesRepository.saveAndFlush(provinces);
        int databaseSizeBeforeDelete = provincesRepository.findAll().size();

        // Get the provinces
        restProvincesMockMvc.perform(delete("/api/provinces/{id}", provinces.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Provinces> provincesList = provincesRepository.findAll();
        assertThat(provincesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Provinces.class);
        Provinces provinces1 = new Provinces();
        provinces1.setId(1L);
        Provinces provinces2 = new Provinces();
        provinces2.setId(provinces1.getId());
        assertThat(provinces1).isEqualTo(provinces2);
        provinces2.setId(2L);
        assertThat(provinces1).isNotEqualTo(provinces2);
        provinces1.setId(null);
        assertThat(provinces1).isNotEqualTo(provinces2);
    }
}
