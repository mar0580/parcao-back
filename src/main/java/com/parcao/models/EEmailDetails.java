package com.parcao.models;

public enum EEmailDetails {
    ESTOQUE_BAIXO("Estoque baixo"),
    RELATORIO_POR_TIPO_PAGAMENTO("Relatório por tipo de pagamento"),
    RELATORIO_VENDAS_PARCIAL("Relatório diário parcial de vendas");

    private final String levelCode;

    EEmailDetails(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getEEmailDetails() {
        return this.levelCode;
    }
}


