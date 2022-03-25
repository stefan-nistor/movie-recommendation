package ro.info.uaic.movierecommendation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.info.uaic.movierecommendation.dtoresponses.HealthStatusResponse;

@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public ResponseEntity<?> healthCheck(){
        return new ResponseEntity<>(new HealthStatusResponse("E_OK"), HttpStatus.OK);
    }
}
