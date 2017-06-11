package cn.gov.nea.sc.web.rest;

import cn.gov.nea.sc.NearepApp;

import cn.gov.nea.sc.domain.PostAddress;
import cn.gov.nea.sc.domain.Districts;
import cn.gov.nea.sc.repository.PostAddressRepository;
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
 * Test class for the PostAddressResource REST controller.
 *
 * @see PostAddressResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NearepApp.class)
public class PostAddressResourceIntTest {

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_TEL = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_FAX = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_OFFICE_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_OFFICE_EMAIL = "BBBBBBBBBB";

    @Autowired
    private PostAddressRepository postAddressRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPostAddressMockMvc;

    private PostAddress postAddress;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PostAddressResource postAddressResource = new PostAddressResource(postAddressRepository);
        this.restPostAddressMockMvc = MockMvcBuilders.standaloneSetup(postAddressResource)
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
    public static PostAddress createEntity(EntityManager em) {
        PostAddress postAddress = new PostAddress()
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .officeTel(DEFAULT_OFFICE_TEL)
            .officeFax(DEFAULT_OFFICE_FAX)
            .officeEmail(DEFAULT_OFFICE_EMAIL);
        // Add required entity
        Districts districtName = DistrictsResourceIntTest.createEntity(em);
        em.persist(districtName);
        em.flush();
        postAddress.setDistrictName(districtName);
        return postAddress;
    }

    @Before
    public void initTest() {
        postAddress = createEntity(em);
    }

    @Test
    @Transactional
    public void createPostAddress() throws Exception {
        int databaseSizeBeforeCreate = postAddressRepository.findAll().size();

        // Create the PostAddress
        restPostAddressMockMvc.perform(post("/api/post-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postAddress)))
            .andExpect(status().isCreated());

        // Validate the PostAddress in the database
        List<PostAddress> postAddressList = postAddressRepository.findAll();
        assertThat(postAddressList).hasSize(databaseSizeBeforeCreate + 1);
        PostAddress testPostAddress = postAddressList.get(postAddressList.size() - 1);
        assertThat(testPostAddress.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testPostAddress.getOfficeTel()).isEqualTo(DEFAULT_OFFICE_TEL);
        assertThat(testPostAddress.getOfficeFax()).isEqualTo(DEFAULT_OFFICE_FAX);
        assertThat(testPostAddress.getOfficeEmail()).isEqualTo(DEFAULT_OFFICE_EMAIL);
    }

    @Test
    @Transactional
    public void createPostAddressWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postAddressRepository.findAll().size();

        // Create the PostAddress with an existing ID
        postAddress.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostAddressMockMvc.perform(post("/api/post-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postAddress)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PostAddress> postAddressList = postAddressRepository.findAll();
        assertThat(postAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStreetAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = postAddressRepository.findAll().size();
        // set the field null
        postAddress.setStreetAddress(null);

        // Create the PostAddress, which fails.

        restPostAddressMockMvc.perform(post("/api/post-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postAddress)))
            .andExpect(status().isBadRequest());

        List<PostAddress> postAddressList = postAddressRepository.findAll();
        assertThat(postAddressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPostAddresses() throws Exception {
        // Initialize the database
        postAddressRepository.saveAndFlush(postAddress);

        // Get all the postAddressList
        restPostAddressMockMvc.perform(get("/api/post-addresses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].officeTel").value(hasItem(DEFAULT_OFFICE_TEL.toString())))
            .andExpect(jsonPath("$.[*].officeFax").value(hasItem(DEFAULT_OFFICE_FAX.toString())))
            .andExpect(jsonPath("$.[*].officeEmail").value(hasItem(DEFAULT_OFFICE_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getPostAddress() throws Exception {
        // Initialize the database
        postAddressRepository.saveAndFlush(postAddress);

        // Get the postAddress
        restPostAddressMockMvc.perform(get("/api/post-addresses/{id}", postAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(postAddress.getId().intValue()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS.toString()))
            .andExpect(jsonPath("$.officeTel").value(DEFAULT_OFFICE_TEL.toString()))
            .andExpect(jsonPath("$.officeFax").value(DEFAULT_OFFICE_FAX.toString()))
            .andExpect(jsonPath("$.officeEmail").value(DEFAULT_OFFICE_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPostAddress() throws Exception {
        // Get the postAddress
        restPostAddressMockMvc.perform(get("/api/post-addresses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostAddress() throws Exception {
        // Initialize the database
        postAddressRepository.saveAndFlush(postAddress);
        int databaseSizeBeforeUpdate = postAddressRepository.findAll().size();

        // Update the postAddress
        PostAddress updatedPostAddress = postAddressRepository.findOne(postAddress.getId());
        updatedPostAddress
            .streetAddress(UPDATED_STREET_ADDRESS)
            .officeTel(UPDATED_OFFICE_TEL)
            .officeFax(UPDATED_OFFICE_FAX)
            .officeEmail(UPDATED_OFFICE_EMAIL);

        restPostAddressMockMvc.perform(put("/api/post-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPostAddress)))
            .andExpect(status().isOk());

        // Validate the PostAddress in the database
        List<PostAddress> postAddressList = postAddressRepository.findAll();
        assertThat(postAddressList).hasSize(databaseSizeBeforeUpdate);
        PostAddress testPostAddress = postAddressList.get(postAddressList.size() - 1);
        assertThat(testPostAddress.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testPostAddress.getOfficeTel()).isEqualTo(UPDATED_OFFICE_TEL);
        assertThat(testPostAddress.getOfficeFax()).isEqualTo(UPDATED_OFFICE_FAX);
        assertThat(testPostAddress.getOfficeEmail()).isEqualTo(UPDATED_OFFICE_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingPostAddress() throws Exception {
        int databaseSizeBeforeUpdate = postAddressRepository.findAll().size();

        // Create the PostAddress

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPostAddressMockMvc.perform(put("/api/post-addresses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postAddress)))
            .andExpect(status().isCreated());

        // Validate the PostAddress in the database
        List<PostAddress> postAddressList = postAddressRepository.findAll();
        assertThat(postAddressList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePostAddress() throws Exception {
        // Initialize the database
        postAddressRepository.saveAndFlush(postAddress);
        int databaseSizeBeforeDelete = postAddressRepository.findAll().size();

        // Get the postAddress
        restPostAddressMockMvc.perform(delete("/api/post-addresses/{id}", postAddress.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PostAddress> postAddressList = postAddressRepository.findAll();
        assertThat(postAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostAddress.class);
        PostAddress postAddress1 = new PostAddress();
        postAddress1.setId(1L);
        PostAddress postAddress2 = new PostAddress();
        postAddress2.setId(postAddress1.getId());
        assertThat(postAddress1).isEqualTo(postAddress2);
        postAddress2.setId(2L);
        assertThat(postAddress1).isNotEqualTo(postAddress2);
        postAddress1.setId(null);
        assertThat(postAddress1).isNotEqualTo(postAddress2);
    }
}
