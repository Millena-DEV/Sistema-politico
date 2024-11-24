package com.ub.org.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.ub.org.demo.Specifications.CandidatoSpecifications;
import com.ub.org.demo.repository.CandidatoViewRepository;
import com.ub.org.demo.view.CandidatoView;
import com.ub.org.demo.view.FiliadoView;

import jakarta.persistence.EntityNotFoundException;



@Service
public class CandidatoService {

    @Autowired
    private CandidatoViewRepository candidatoViewRepository;

    public long contarCandidatosPorPartidoECargo(String partido, String cargo, Integer ano) {
        System.out.println("Partido: " + partido + ", Cargo: " + cargo + ", Ano: " + ano);
        return candidatoViewRepository.countByPartidoAndCargoAndAno(partido, cargo, ano);
    }

    public long contarCandidatos() {
        return candidatoViewRepository.count(); // Retorna o número total de Candidatos
    }
    // Método para filtrar candidatos com base nas especificações
    public Page<CandidatoView> filtrarCandidatos(Integer ano , String partido, String nome, String cpf, String titulo, String uf, String municipio, String cargo, Pageable pageable) {
        Specification<CandidatoView> spec = Specification.where(CandidatoSpecifications.hasNome(nome))
                                                       .and(CandidatoSpecifications.hasTitulo(titulo))
                                                       .and(CandidatoSpecifications.hasUf(uf))
                                                       .and(CandidatoSpecifications.hasMunicipio(municipio))
                                                       .and(CandidatoSpecifications.hasAno(ano))
                                                       .and(CandidatoSpecifications.hasPartido(partido))
                                                       .and(CandidatoSpecifications.hasCargo(cargo));
                                                       
                                                     

        return candidatoViewRepository.findAll(spec,pageable);
    }
     // Método para buscar todos os Candidatos com paginação
    public Page<CandidatoView> findAll(Pageable pageable) {
        return candidatoViewRepository.findAll(pageable);
    }
// Método para contar candidatos por ano, cargo e partido
public long contarCandidatosPorAnoCargoEPartido(Integer ano, String cargo, String partido) {
    return candidatoViewRepository.countByAnoAndCargoAndPartido(ano, cargo, partido);
}
    
public CandidatoView buscarPorId(Long id) {
    return candidatoViewRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Candidato não encontrado com o ID: " + id));
}



}
