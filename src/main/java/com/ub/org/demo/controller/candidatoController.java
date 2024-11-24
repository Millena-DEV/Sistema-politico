package com.ub.org.demo.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ub.org.demo.dto.CandidatoDTO;
import com.ub.org.demo.repository.CandidatoViewRepository;
import com.ub.org.demo.service.CandidatoService;
import com.ub.org.demo.view.CandidatoView;


@Controller
public class candidatoController {
    @Autowired
    CandidatoViewRepository candidatoViewRepository;
    @Autowired
    private CandidatoService  candidatoService;

     @GetMapping("/candidatos") // Mapeia para a URL /candidatos
    public String getcandidatos(Model model, 
                           @RequestParam(defaultValue = "1") int page, 
                           @RequestParam(defaultValue = "10") int size, 
                           @RequestParam(required = false) String nome,
                           @RequestParam(required = false) String cpf,
                           @RequestParam(required = false) String titulo,
                           @RequestParam(required = false) String uf,
                           @RequestParam(required = false) String municipio,
                           @RequestParam(required = false) Integer  ano_filtro,
                           @RequestParam(required = false) String partido,
                           @RequestParam(required = false) String cargo) {

        // Cria um objeto Pageable para a paginação
        Pageable pageable = PageRequest.of(page - 1, size);

        // Verifica se há filtros aplicados
        Page<CandidatoView> pagedResult;
        if (ano_filtro != null || partido != null || cargo!= null || nome != null || cpf != null || titulo != null || uf != null || municipio != null ) {
            pagedResult = candidatoService.filtrarCandidatos(ano_filtro, partido, nome, cpf, titulo, uf, municipio, cargo, pageable);
        } else {
            pagedResult = candidatoService.findAll(pageable); // Use um método no serviço para buscar todos
        }
                        
        List<CandidatoDTO> candidatos = pagedResult.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        model.addAttribute("candidatos", candidatos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagedResult.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("ano_filtro", ano_filtro);
        model.addAttribute("partido", partido);
        model.addAttribute("cargo", cargo);
        model.addAttribute("nome", nome);
        model.addAttribute("titulo", titulo);
        model.addAttribute("cpf", cpf);
        model.addAttribute("uf", uf);
        model.addAttribute("municipio", municipio);

        return "tb_candidatos"; // Nome do arquivo HTML
    }

    private CandidatoDTO convertToDTO(CandidatoView candidato) {
        CandidatoDTO dto = new CandidatoDTO();
        dto.setId(candidato.getId());
        dto.setCpf(candidato.getCpf());
        dto.setNome(candidato.getNome());
        dto.setUf(candidato.getUf());
        dto.setMunicipio(candidato.getMunicipio());
        dto.setTitulo(candidato.getTitulo());
        dto.setCargo(candidato.getCargo());
        dto.setAno_filtro(candidato.getAno_filtro());
        dto.setPartido(candidato.getPartido());
        dto.setSitu_filiacao(candidato.getSitu_filiacao());
        dto.setEtinia(candidato.getEtinia());
        dto.setGenero(candidato.getGenero());
        dto.setDespesa(candidato.getDespesa());
        dto.setSitu_cand(candidato.getSitu_cand());
        dto.setSitu_filiacao(candidato.getSitu_filiacao());
        dto.setNascimento(candidato.getNascimento());
        return dto;
    }
   @GetMapping("api/candidatos")
    @ResponseBody
    public List<CandidatoView> getCandidatos() {
        return candidatoViewRepository.findAll();
    }

     // Método para exibir os detalhes de um candidato
    @GetMapping("/detalhar/candidato/{id}")
    public String documentoCandidato(@PathVariable("id") long id, Model model) {
        CandidatoView candidato = candidatoService.buscarPorId(id);  
        model.addAttribute("candidato", candidato);  
        return "detalhar-candidato"; 
    }

}