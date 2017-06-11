package cn.gov.nea.sc.web.rest;

import com.codahale.metrics.annotation.Timed;
import cn.gov.nea.sc.domain.PostAddress;

import cn.gov.nea.sc.repository.PostAddressRepository;
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
 * REST controller for managing PostAddress.
 */
@RestController
@RequestMapping("/api")
public class PostAddressResource {

    private final Logger log = LoggerFactory.getLogger(PostAddressResource.class);

    private static final String ENTITY_NAME = "postAddress";

    private final PostAddressRepository postAddressRepository;

    public PostAddressResource(PostAddressRepository postAddressRepository) {
        this.postAddressRepository = postAddressRepository;
    }

    /**
     * POST  /post-addresses : Create a new postAddress.
     *
     * @param postAddress the postAddress to create
     * @return the ResponseEntity with status 201 (Created) and with body the new postAddress, or with status 400 (Bad Request) if the postAddress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/post-addresses")
    @Timed
    public ResponseEntity<PostAddress> createPostAddress(@Valid @RequestBody PostAddress postAddress) throws URISyntaxException {
        log.debug("REST request to save PostAddress : {}", postAddress);
        if (postAddress.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new postAddress cannot already have an ID")).body(null);
        }
        PostAddress result = postAddressRepository.save(postAddress);
        return ResponseEntity.created(new URI("/api/post-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /post-addresses : Updates an existing postAddress.
     *
     * @param postAddress the postAddress to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated postAddress,
     * or with status 400 (Bad Request) if the postAddress is not valid,
     * or with status 500 (Internal Server Error) if the postAddress couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/post-addresses")
    @Timed
    public ResponseEntity<PostAddress> updatePostAddress(@Valid @RequestBody PostAddress postAddress) throws URISyntaxException {
        log.debug("REST request to update PostAddress : {}", postAddress);
        if (postAddress.getId() == null) {
            return createPostAddress(postAddress);
        }
        PostAddress result = postAddressRepository.save(postAddress);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postAddress.getId().toString()))
            .body(result);
    }

    /**
     * GET  /post-addresses : get all the postAddresses.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of postAddresses in body
     */
    @GetMapping("/post-addresses")
    @Timed
    public List<PostAddress> getAllPostAddresses() {
        log.debug("REST request to get all PostAddresses");
        return postAddressRepository.findAll();
    }

    /**
     * GET  /post-addresses/:id : get the "id" postAddress.
     *
     * @param id the id of the postAddress to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the postAddress, or with status 404 (Not Found)
     */
    @GetMapping("/post-addresses/{id}")
    @Timed
    public ResponseEntity<PostAddress> getPostAddress(@PathVariable Long id) {
        log.debug("REST request to get PostAddress : {}", id);
        PostAddress postAddress = postAddressRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(postAddress));
    }

    /**
     * DELETE  /post-addresses/:id : delete the "id" postAddress.
     *
     * @param id the id of the postAddress to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/post-addresses/{id}")
    @Timed
    public ResponseEntity<Void> deletePostAddress(@PathVariable Long id) {
        log.debug("REST request to delete PostAddress : {}", id);
        postAddressRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
