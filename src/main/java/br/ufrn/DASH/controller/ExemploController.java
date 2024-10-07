package br.ufrn.DASH.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.DASH.model.ExemploModel;
import br.ufrn.DASH.repository.ExemploRepository;


@RestController
@RequestMapping("/exemplo")
public class ExemploController {
    @Autowired
    private ExemploRepository exemploRepository;

    @GetMapping
    public ResponseEntity<ExemploModel> exemplo() {
        ExemploModel exemploModel = new ExemploModel();
        return ResponseEntity.ok(exemploRepository.save(exemploModel));
    }
}