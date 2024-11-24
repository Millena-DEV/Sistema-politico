package com.ub.org.demo.Specifications;
import com.ub.org.demo.view.FiliadoView; // Ajuste para o pacote correto
import org.springframework.data.jpa.domain.Specification;
public class FiliadoSpecifications {
    
    public static Specification<FiliadoView> hasNome(String nome) {
        return (root, query, criteriaBuilder) -> {
            if (nome == null || nome.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("nome"), "%" + nome + "%");
        };
    }

    public static Specification<FiliadoView> hasCpf(String cpf) {
        return (root, query, criteriaBuilder) -> {
            if (cpf == null || cpf.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("cpf"), cpf);
        };
    }

    public static Specification<FiliadoView> hasTitulo(String titulo) {
        return (root, query, criteriaBuilder) -> {
            if (titulo == null || titulo.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("titulo"), titulo);
        };
    }

    public static Specification<FiliadoView> hasUf(String uf) {
        return (root, query, criteriaBuilder) -> {
            if (uf == null || uf.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("uf"), uf);
        };
    }

    public static Specification<FiliadoView> hasMunicipio(String municipio) {
        return (root, query, criteriaBuilder) -> {
            if (municipio == null || municipio.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("municipio"), municipio);
        };
    }

    public static Specification<FiliadoView> hasDataFiliacao(String dataFiliacao) {
        return (root, query, criteriaBuilder) -> {
            if (dataFiliacao == null || dataFiliacao.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("dataFiliacao"), dataFiliacao);
        };
    }
}
