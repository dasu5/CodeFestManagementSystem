package com.csse.codefest.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.csse.codefest.service.CompetitionService;
import com.csse.codefest.web.rest.util.HeaderUtil;
import com.csse.codefest.web.rest.util.PaginationUtil;
import com.csse.codefest.service.dto.CompetitionDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Competition.
 */
@RestController
@RequestMapping("/api")
public class CompetitionResource {

    private final Logger log = LoggerFactory.getLogger(CompetitionResource.class);

    private static final String ENTITY_NAME = "competition";

    private final CompetitionService competitionService;

    public CompetitionResource(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    /**
     * POST  /competitions : Create a new competition.
     *
     * @param competitionDTO the competitionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new competitionDTO, or with status 400 (Bad Request) if the competition has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/competitions")
    @Timed
    public ResponseEntity<CompetitionDTO> createCompetition(@Valid @RequestBody CompetitionDTO competitionDTO) throws URISyntaxException {
        log.debug("REST request to save Competition : {}", competitionDTO);
        if (competitionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new competition cannot already have an ID")).body(null);
        }
        CompetitionDTO result = competitionService.save(competitionDTO);
        return ResponseEntity.created(new URI("/api/competitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /competitions : Updates an existing competition.
     *
     * @param competitionDTO the competitionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated competitionDTO,
     * or with status 400 (Bad Request) if the competitionDTO is not valid,
     * or with status 500 (Internal Server Error) if the competitionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/competitions")
    @Timed
    public ResponseEntity<CompetitionDTO> updateCompetition(@Valid @RequestBody CompetitionDTO competitionDTO) throws URISyntaxException {
        log.debug("REST request to update Competition : {}", competitionDTO);
        if (competitionDTO.getId() == null) {
            return createCompetition(competitionDTO);
        }
        CompetitionDTO result = competitionService.save(competitionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, competitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /competitions : get all the competitions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of competitions in body
     */
    @GetMapping("/competitions")
    @Timed
    public ResponseEntity<List<CompetitionDTO>> getAllCompetitions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Competitions");
        Page<CompetitionDTO> page = competitionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/competitions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /competitions/:id : get the "id" competition.
     *
     * @param id the id of the competitionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the competitionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/competitions/{id}")
    @Timed
    public ResponseEntity<CompetitionDTO> getCompetition(@PathVariable Long id) {
        log.debug("REST request to get Competition : {}", id);
        CompetitionDTO competitionDTO = competitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(competitionDTO));
    }

    /**
     * DELETE  /competitions/:id : delete the "id" competition.
     *
     * @param id the id of the competitionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/competitions/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompetition(@PathVariable Long id) {
        log.debug("REST request to delete Competition : {}", id);
        competitionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/competitions?query=:query : search for the competition corresponding
     * to the query.
     *
     * @param query the query of the competition search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/competitions")
    @Timed
    public ResponseEntity<List<CompetitionDTO>> searchCompetitions(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Competitions for query {}", query);
        Page<CompetitionDTO> page = competitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/competitions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
