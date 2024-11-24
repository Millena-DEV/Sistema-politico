package com.ub.org.demo.enums;

public enum Funcao  {
    ADMINISTRADOR("Administrador"),
    CONSULTOR_NACIONAL("Consultor Nacional"),
    CONSULTOR_ESTADUAL("Consultor Estadual"),
    CONSULTOR_MUNICIPAL("Consultor Municipal"),
    OPERADOR_NACIONAL("Operador Nacional"),
    OPERADOR_ESTATUAL("Operador Estadual"),
    OPERADOR_MUNICIPAL("Operador Municipal");

    private String descricao;

    Funcao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}