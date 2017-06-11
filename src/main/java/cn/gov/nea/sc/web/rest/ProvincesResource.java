package cn.gov.nea.sc.web.rest;

import com.codahale.metrics.annotation.Timed;
import cn.gov.nea.sc.domain.Provinces;

import cn.gov.nea.sc.repository.ProvincesRepository;
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
 * REST controller for managing Provinces.
 */
@RestController
@RequestMapping("/api")
public class ProvincesResource {

    private final Logger log = LoggerFactory.getLogger(ProvincesResource.class);

    private static final String ENTITY_NAME = "provinces";

    private final ProvincesRepository provincesRepository;

    public ProvincesResource(ProvincesRepository provincesRepository) {
        this.provincesRepository = provincesRepository;
    }

    /**
     * POST  /provinces : Create a new provinces.
     *
     * @param provinces the provinces to create
     * @return the ResponseEntity with status 201 (Created) and with body the new provinces, or with status 400 (Bad Request) if the provinces has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/provinces")
    @Timed
    public ResponseEntity<Provinces> createProvinces(@Valid @RequestBody Provinces provinces) throws URISyntaxException {
        log.debug("REST request to save Provinces : {}", provinces);
        if (provinces.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new provinces cannot already have an ID")).body(null);
        }
        Provinces result = provincesRepository.save(provinces);
        return ResponseEntity.created(new URI("/api/provinces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /provinces : Updates an existing provinces.
     *
     * @param provinces the provinces to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated provinces,
     * or with status 400 (Bad Request) if the provinces is not valid,
     * or with status 500 (Internal Server Error) if the provinces couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/provinces")
    @Timed
    public ResponseEntity<Provinces> updateProvinces(@Valid @RequestBody Provinces provinces) throws URISyntaxException {
        log.debug("REST request to update Provinces : {}", provinces);
        if (provinces.getId() == null) {
            return createProvinces(provinces);
        }
        Provinces result = provincesRepository.save(provinces);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, provinces.getId().toString()))
            .body(result);
    }

    /**
     * GET  /provinces : get all the provinces.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of provinces in body
     */
    @GetMapping("/provinces")
    @Timed
    public List<Provinces> getAllProvinces() {
        log.debug("REST request to get all Provinces");
        return provincesRepository.findAll();
    }

    /**
     * GET  /provinces/:id : get the "id" provinces.
     *
     * @param id the id of the provinces to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the provinces, or with status 404 (Not Found)
     */
    @GetMapping("/provinces/{id}")
    @Timed
    public ResponseEntity<Provinces> getProvinces(@PathVariable Long id) {
        log.debug("REST request to get Provinces : {}", id);
        Provinces provinces = provincesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(provinces));
    }

    /**
     * DELETE  /provinces/:id : delete the "id" provinces.
     *
     * @param id the id of the provinces to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/provinces/{id}")
    @Timed
    public ResponseEntity<Void> deleteProvinces(@PathVariable Long id) {
        log.debug("REST request to delete Provinces : {}", id);
        provincesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
