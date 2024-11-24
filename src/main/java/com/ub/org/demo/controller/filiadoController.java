package com.ub.org.demo.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.ub.org.demo.dto.FiliadoDTO;
import com.ub.org.demo.repository.FiliadoViewRepository;
import com.ub.org.demo.service.FiliadoService;
import com.ub.org.demo.view.FiliadoView;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Controller
public class filiadoController {

    @Autowired
    private FiliadoService filiadoService;
    @Autowired 
    FiliadoViewRepository filiadoViewRepository;


     @GetMapping("/filiados") // Mapeia para a URL /filiados
    public String getFiliados(Model model, 
                           @RequestParam(defaultValue = "1") int page, 
                           @RequestParam(defaultValue = "10") int size, 
                           @RequestParam(required = false) String nome,
                           @RequestParam(required = false) String cpf,
                           @RequestParam(required = false) String titulo,
                           @RequestParam(required = false) String uf,
                           @RequestParam(required = false) String municipio,
                           @RequestParam(required = false) String dataFiliacao) {

        // Cria um objeto Pageable para a paginação
        Pageable pageable = PageRequest.of(page - 1, size);

        // Verifica se há filtros aplicados
        Page<FiliadoView> pagedResult;
        if (nome != null || cpf != null || titulo != null || uf != null || municipio != null || dataFiliacao != null) {
            pagedResult = filiadoService.filtrarFiliados(nome, cpf, titulo, uf, municipio, dataFiliacao, pageable);
        } else {
            pagedResult = filiadoService.findAll(pageable); // Use um método no serviço para buscar todos
        }
                        
        List<FiliadoDTO> filiados = pagedResult.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        model.addAttribute("filiados", filiados);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagedResult.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("nome", nome);
        model.addAttribute("titulo", titulo);
        model.addAttribute("cpf", cpf);
        model.addAttribute("uf", uf);
        model.addAttribute("municipio", municipio);

        return "tb_filiados"; // Nome do arquivo HTML
    }

    private FiliadoDTO convertToDTO(FiliadoView filiado) {
        FiliadoDTO dto = new FiliadoDTO();
        dto.setNome(filiado.getNome());
        dto.setCpf(filiado.getCpf());
        dto.setTitulo(filiado.getTitulo());
        dto.setDataNascimento(filiado.getDataNascimento());
        dto.setUf(filiado.getUf());
        dto.setMunicipio(filiado.getMunicipio());
        dto.setDataFiliacao(filiado.getDataFiliacao());
        dto.setNomePai(filiado.getNomePai());
        dto.setNomeMae(filiado.getNomeMae());
        dto.setLogradouro(filiado.getLogradouro());
        dto.setComplemento(filiado.getComplemento());
        dto.setNumero(filiado.getNumero());
        dto.setBairro(filiado.getBairro());
        dto.setCep(filiado.getCep());
        dto.setNumeroTelefone1(filiado.getNumeroTelefone1());
        dto.setNumeroTelefone2(filiado.getNumeroTelefone2());
        dto.setEmail(filiado.getEmail());
        dto.setGenero(filiado.getGenero());
        return dto;
    }
    @GetMapping("api/filiados")
    @ResponseBody
    public List<FiliadoView> getFiliados() {
        return filiadoViewRepository.findAll();
    }

    // Método para exibir os detalhes de um filiado
    @GetMapping("/detalhar/{id}")
    public String detalharFiliado(@PathVariable("id") String id, Model model) {
        FiliadoView filiado = filiadoService.buscarPorId(id)
        .orElseThrow(() -> new IllegalArgumentException("Filiado não encontrado com ID: " + id)); // Suponha que esse método retorne um filiado por ID
        model.addAttribute("filiado", filiado);  // Passa o objeto filiado para a página
        return "detalhar-filiado";  // Nome da view onde você vai exibir os detalhes
    }


     // Método para exibir os detalhes de um filiado
     @GetMapping("/documentos/{id}")
     public String documentoFiliado(@PathVariable("id") String id, Model model) {
        FiliadoView filiado = filiadoService.buscarPorId(id)
        .orElseThrow(() -> new IllegalArgumentException("Filiado não encontrado com ID: " + id));
         model.addAttribute("filiado", filiado);  // Passa o objeto filiado para a página
         return "documentos-filiado";  // Nome da view onde você vai exibir os detalhes
     }
    }