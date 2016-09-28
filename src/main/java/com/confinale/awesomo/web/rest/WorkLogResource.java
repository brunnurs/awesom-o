package com.confinale.awesomo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.confinale.awesomo.domain.WorkLog;

import com.confinale.awesomo.repository.WorkLogRepository;
import com.confinale.awesomo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WorkLog.
 */
@RestController
@RequestMapping("/api")
public class WorkLogResource {

    private final Logger log = LoggerFactory.getLogger(WorkLogResource.class);
        
    @Inject
    private WorkLogRepository workLogRepository;

    /**
     * POST  /work-logs : Create a new workLog.
     *
     * @param workLog the workLog to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workLog, or with status 400 (Bad Request) if the workLog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-logs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkLog> createWorkLog(@Valid @RequestBody WorkLog workLog) throws URISyntaxException {
        log.debug("REST request to save WorkLog : {}", workLog);
        if (workLog.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("workLog", "idexists", "A new workLog cannot already have an ID")).body(null);
        }
        WorkLog result = workLogRepository.save(workLog);
        return ResponseEntity.created(new URI("/api/work-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("workLog", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-logs : Updates an existing workLog.
     *
     * @param workLog the workLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workLog,
     * or with status 400 (Bad Request) if the workLog is not valid,
     * or with status 500 (Internal Server Error) if the workLog couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/work-logs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkLog> updateWorkLog(@Valid @RequestBody WorkLog workLog) throws URISyntaxException {
        log.debug("REST request to update WorkLog : {}", workLog);
        if (workLog.getId() == null) {
            return createWorkLog(workLog);
        }
        WorkLog result = workLogRepository.save(workLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("workLog", workLog.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-logs : get all the workLogs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workLogs in body
     */
    @RequestMapping(value = "/work-logs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<WorkLog> getAllWorkLogs() {
        log.debug("REST request to get all WorkLogs");
        List<WorkLog> workLogs = workLogRepository.findAll();
        return workLogs;
    }

    /**
     * GET  /work-logs/:id : get the "id" workLog.
     *
     * @param id the id of the workLog to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workLog, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/work-logs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WorkLog> getWorkLog(@PathVariable Long id) {
        log.debug("REST request to get WorkLog : {}", id);
        WorkLog workLog = workLogRepository.findOne(id);
        return Optional.ofNullable(workLog)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /work-logs/:id : delete the "id" workLog.
     *
     * @param id the id of the workLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/work-logs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorkLog(@PathVariable Long id) {
        log.debug("REST request to delete WorkLog : {}", id);
        workLogRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("workLog", id.toString())).build();
    }

}
