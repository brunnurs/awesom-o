package com.confinale.awesomo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.confinale.awesomo.domain.Subsidiary;

import com.confinale.awesomo.repository.SubsidiaryRepository;
import com.confinale.awesomo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Subsidiary.
 */
@RestController
@RequestMapping("/api")
public class SubsidiaryResource {

    private final Logger log = LoggerFactory.getLogger(SubsidiaryResource.class);

    @Inject
    private SubsidiaryRepository subsidiaryRepository;

    /**
     * POST  /subsidiaries : Create a new subsidiary.
     *
     * @param subsidiary the subsidiary to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subsidiary, or with status 400 (Bad Request) if the subsidiary has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subsidiaries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Subsidiary> createSubsidiary(@RequestBody Subsidiary subsidiary) throws URISyntaxException {
        log.debug("REST request to save Subsidiary : {}", subsidiary);
        if (subsidiary.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("subsidiary", "idexists", "A new subsidiary cannot already have an ID")).body(null);
        }
        Subsidiary result = subsidiaryRepository.save(subsidiary);
        return ResponseEntity.created(new URI("/api/subsidiaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("subsidiary", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subsidiaries : Updates an existing subsidiary.
     *
     * @param subsidiary the subsidiary to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subsidiary,
     * or with status 400 (Bad Request) if the subsidiary is not valid,
     * or with status 500 (Internal Server Error) if the subsidiary couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subsidiaries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Subsidiary> updateSubsidiary(@RequestBody Subsidiary subsidiary) throws URISyntaxException {
        log.debug("REST request to update Subsidiary : {}", subsidiary);
        if (subsidiary.getId() == null) {
            return createSubsidiary(subsidiary);
        }
        Subsidiary result = subsidiaryRepository.save(subsidiary);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("subsidiary", subsidiary.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subsidiaries : get all the subsidiaries.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of subsidiaries in body
     */
    @RequestMapping(value = "/subsidiaries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Subsidiary> getAllSubsidiaries() {
        log.debug("REST request to get all Subsidiaries");
        List<Subsidiary> subsidiaries = subsidiaryRepository.findAll();
        return subsidiaries;
    }


    /**
     * GET  GET  /subsidiaries/parent/{userId} : Get a subsidiary for a certain parent user if exist.
     *
     * @param userId the id of the parent user
     * @return the ResponseEntity with status 200 (OK) and with body the subsidiary, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/subsidiaries/parent/{userId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Subsidiary> getSubsidiaryByParentUser(@PathVariable Long userId) {
        log.debug("REST request to get Subsidiary with parent user Id : {}", userId);
        Optional<Subsidiary> subsidiary = subsidiaryRepository.findOneByParentId(userId);

        if (subsidiary.isPresent()) {
            return new ResponseEntity<>(
                subsidiary.get(),
                HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * GET  /subsidiaries/:id : get the "id" subsidiary.
     *
     * @param id the id of the subsidiary to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subsidiary, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/subsidiaries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Subsidiary> getSubsidiary(@PathVariable Long id) {
        log.debug("REST request to get Subsidiary : {}", id);
        Subsidiary subsidiary = subsidiaryRepository.findOne(id);
        return Optional.ofNullable(subsidiary)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /subsidiaries/:id : delete the "id" subsidiary.
     *
     * @param id the id of the subsidiary to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/subsidiaries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSubsidiary(@PathVariable Long id) {
        log.debug("REST request to delete Subsidiary : {}", id);
        subsidiaryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("subsidiary", id.toString())).build();
    }

}
