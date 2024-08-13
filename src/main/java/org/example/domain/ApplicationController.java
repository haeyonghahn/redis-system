package org.example.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);
    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/document/{resolution}")
    public ResponseEntity<List<Resolution>> getData(@PathVariable String resolution) throws Exception {
        try {
            return ResponseEntity.ok().body(applicationService.getData(resolution));
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
