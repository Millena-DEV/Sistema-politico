package com.ub.org.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ub.org.demo.repository.MunicipioRepository;
import com.ub.org.demo.view.municipio;
@Controller
public class MunicipioController {

    @Autowired
    private MunicipioRepository municipioRepository;


     @GetMapping("/api/municipios/{uf}")
    public ResponseEntity<List<municipio>> getMunicipiosByUf(@PathVariable String uf) {
        List<municipio> municipios = municipioRepository.findByUf(uf);
        return ResponseEntity.ok(municipios);

    }

}
