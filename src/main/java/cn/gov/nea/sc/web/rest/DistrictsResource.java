package cn.gov.nea.sc.web.rest;

import com.codahale.metrics.annotation.Timed;
import cn.gov.nea.sc.domain.Districts;

import cn.gov.nea.sc.repository.DistrictsRepository;
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
 * REST controller for managing Districts.
 */
@RestController
@RequestMapping("/api")
public class DistrictsResource {

    private final Logger log = LoggerFactory.getLogger(DistrictsResource.class);

    private static final String ENTITY_NAME = "districts";

    private final DistrictsRepository districtsRepository;

    public DistrictsResource(DistrictsRepository districtsRepository) {
        this.districtsRepository = districtsRepository;
    }

    /**
     * POST  /districts : Create a new districts.
     *
     * @param districts the districts to create
     * @return the ResponseEntity with status 201 (Created) and with body the new districts, or with status 400 (Bad Request) if the districts has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/districts")
    @Timed
    public ResponseEntity<Districts> createDistricts(@Valid @RequestBody Districts districts) throws URISyntaxException {
        log.debug("REST request to save Districts : {}", districts);
        if (districts.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new districts cannot already have an ID")).body(null);
        }
        Districts result = districtsRepository.save(districts);
        return ResponseEntity.created(new URI("/api/districts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /districts : Updates an existing districts.
     *
     * @param districts the districts to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated districts,
     * or with status 400 (Bad Request) if the districts is not valid,
     * or with status 500 (Internal Server Error) if the districts couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/districts")
    @Timed
    public ResponseEntity<Districts> updateDistricts(@Valid @RequestBody Districts districts) throws URISyntaxException {
        log.debug("REST request to update Districts : {}", districts);
        if (districts.getId() == null) {
            return createDistricts(districts);
        }
        Districts result = districtsRepository.save(districts);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, districts.getId().toString()))
            .body(result);
    }

    /**
     * GET  /districts : get all the districts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of districts in body
     */
    @GetMapping("/districts")
    @Timed
    public List<Districts> getAllDistricts() {
        log.debug("REST request to get all Districts");
        return districtsRepository.findAll();
    }

    /**
     * GET  /districts/:id : get the "id" districts.
     *
     * @param id the id of the districts to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the districts, or with status 404 (Not Found)
     */
    @GetMapping("/districts/{id}")
    @Timed
    public ResponseEntity<Districts> getDistricts(@PathVariable Long id) {
        log.debug("REST request to get Districts : {}", id);
        Districts districts = districtsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(districts));
    }

    /**
     * DELETE  /districts/:id : delete the "id" districts.
     *
     * @param id the id of the districts to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/districts/{id}")
    @Timed
    public ResponseEntity<Void> deleteDistricts(@PathVariable Long id) {
        log.debug("REST request to delete Districts : {}", id);
        districtsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
