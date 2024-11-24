package com.ub.org.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ub.org.demo.Specifications.FiliadoSpecifications;
import com.ub.org.demo.repository.FiliadoViewRepository;
import com.ub.org.demo.view.FiliadoView;



@Service
public class FiliadoService {

    @Autowired
    private FiliadoViewRepository filiadoRepository;


    public long contarFiliados() {
        return filiadoRepository.count(); // Retorna o número total de Filiados
    }
    // Método para filtrar filiados com base nas especificações
    public Page<FiliadoView> filtrarFiliados(String nome, String cpf, String titulo, String uf, String municipio, String dataFiliacao, Pageable pageable) {
        Specification<FiliadoView> spec = Specification.where(FiliadoSpecifications.hasNome(nome))
                                                       .and(FiliadoSpecifications.hasCpf(cpf))
                                                       .and(FiliadoSpecifications.hasTitulo(titulo))
                                                       .and(FiliadoSpecifications.hasUf(uf))
                                                       .and(FiliadoSpecifications.hasMunicipio(municipio))
                                                       .and(FiliadoSpecifications.hasDataFiliacao(dataFiliacao));

        return filiadoRepository.findAll(spec, pageable);
    }
     // Método para buscar todos os filiados com paginação
    public Page<FiliadoView> findAll(Pageable pageable) {
        return filiadoRepository.findAll(pageable);
    }
    public Optional<FiliadoView> buscarPorId(String id) {
        return filiadoRepository.findByTitulo(id);
    }

   
   


    
    
}
