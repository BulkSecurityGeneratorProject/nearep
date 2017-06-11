package cn.gov.nea.sc.web.rest;

import com.codahale.metrics.annotation.Timed;
import cn.gov.nea.sc.domain.Cities;

import cn.gov.nea.sc.repository.CitiesRepository;
import cn.gov.nea.sc.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Cities.
 */
@RestController
@RequestMapping("/api")
public class CitiesResource {

    private final Logger log = LoggerFactory.getLogger(CitiesResource.class);

    private static final String ENTITY_NAME = "cities";

    private final CitiesRepository citiesRepository;

    public CitiesResource(CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }

    /**
     * POST  /cities : Create a new cities.
     *
     * @param cities the cities to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cities, or with status 400 (Bad Request) if the cities has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cities")
    @Timed
    public ResponseEntity<Cities> createCities(@Valid @RequestBody Cities cities) throws URISyntaxException {
        log.debug("REST request to save Cities : {}", cities);
        if (cities.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cities cannot already have an ID")).body(null);
        }
        Cities result = citiesRepository.save(cities);
        return ResponseEntity.created(new URI("/api/cities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cities : Updates an existing cities.
     *
     * @param cities the cities to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cities,
     * or with status 400 (Bad Request) if the cities is not valid,
     * or with status 500 (Internal Server Error) if the cities couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cities")
    @Timed
    public ResponseEntity<Cities> updateCities(@Valid @RequestBody Cities cities) throws URISyntaxException {
        log.debug("REST request to update Cities : {}", cities);
        if (cities.getId() == null) {
            return createCities(cities);
        }
        Cities result = citiesRepository.save(cities);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cities.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cities : get all the cities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cities in body
     */
    @GetMapping("/cities")
    @Timed
    public List<Cities> getAllCities() {
        log.debug("REST request to get all Cities");
        return citiesRepository.findAll();
    }

    /**
     * GET  /cities/:id : get the "id" cities.
     *
     * @param id the id of the cities to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cities, or with status 404 (Not Found)
     */
    @GetMapping("/cities/{id}")
    @Timed
    public ResponseEntity<Cities> getCities(@PathVariable Long id) {
        log.debug("REST request to get Cities : {}", id);
        Cities cities = citiesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cities));
    }

    /**
     * DELETE  /cities/:id : delete the "id" cities.
     *
     * @param id the id of the cities to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cities/{id}")
    @Timed
    public ResponseEntity<Void> deleteCities(@PathVariable Long id) {
        log.debug("REST request to delete Cities : {}", id);
        citiesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
