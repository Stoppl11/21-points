package org.jhipster.health.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.jhipster.health.domain.Settings;

import org.jhipster.health.repository.SettingsRepository;
import org.jhipster.health.repository.search.SettingsSearchRepository;
import org.jhipster.health.web.rest.util.HeaderUtil;
import org.jhipster.health.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Settings.
 */
@RestController
@RequestMapping("/api")
public class SettingsResource {

    private final Logger log = LoggerFactory.getLogger(SettingsResource.class);
        
    @Inject
    private SettingsRepository settingsRepository;

    @Inject
    private SettingsSearchRepository settingsSearchRepository;

    /**
     * POST  /settings : Create a new settings.
     *
     * @param settings the settings to create
     * @return the ResponseEntity with status 201 (Created) and with body the new settings, or with status 400 (Bad Request) if the settings has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/settings")
    @Timed
    public ResponseEntity<Settings> createSettings(@Valid @RequestBody Settings settings) throws URISyntaxException {
        log.debug("REST request to save Settings : {}", settings);
        if (settings.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("settings", "idexists", "A new settings cannot already have an ID")).body(null);
        }
        Settings result = settingsRepository.save(settings);
        settingsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("settings", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /settings : Updates an existing settings.
     *
     * @param settings the settings to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated settings,
     * or with status 400 (Bad Request) if the settings is not valid,
     * or with status 500 (Internal Server Error) if the settings couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/settings")
    @Timed
    public ResponseEntity<Settings> updateSettings(@Valid @RequestBody Settings settings) throws URISyntaxException {
        log.debug("REST request to update Settings : {}", settings);
        if (settings.getId() == null) {
            return createSettings(settings);
        }
        Settings result = settingsRepository.save(settings);
        settingsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("settings", settings.getId().toString()))
            .body(result);
    }

    /**
     * GET  /settings : get all the settings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of settings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/settings")
    @Timed
    public ResponseEntity<List<Settings>> getAllSettings(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Settings");
        Page<Settings> page = settingsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/settings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /settings/:id : get the "id" settings.
     *
     * @param id the id of the settings to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the settings, or with status 404 (Not Found)
     */
    @GetMapping("/settings/{id}")
    @Timed
    public ResponseEntity<Settings> getSettings(@PathVariable Long id) {
        log.debug("REST request to get Settings : {}", id);
        Settings settings = settingsRepository.findOne(id);
        return Optional.ofNullable(settings)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /settings/:id : delete the "id" settings.
     *
     * @param id the id of the settings to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/settings/{id}")
    @Timed
    public ResponseEntity<Void> deleteSettings(@PathVariable Long id) {
        log.debug("REST request to delete Settings : {}", id);
        settingsRepository.delete(id);
        settingsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("settings", id.toString())).build();
    }

    /**
     * SEARCH  /_search/settings?query=:query : search for the settings corresponding
     * to the query.
     *
     * @param query the query of the settings search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/settings")
    @Timed
    public ResponseEntity<List<Settings>> searchSettings(@RequestParam String query, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Settings for query {}", query);
        Page<Settings> page = settingsSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/settings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
