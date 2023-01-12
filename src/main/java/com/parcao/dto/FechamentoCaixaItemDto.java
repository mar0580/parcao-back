package com.parcao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FechamentoCaixaItemDto {
    private Long id; //id = idProduto
    private int inicio;
    private int entrada;
    private int perda;
    private int quantidadeFinal;

    public FechamentoCaixaItemDto(FechamentoCaixaItemDto fechamentoCaixa) {
        this.id = fechamentoCaixa.getId();
        this.inicio = fechamentoCaixa.getInicio();
        this.entrada = fechamentoCaixa.getEntrada();
        this.perda = fechamentoCaixa.getPerda();
        this.quantidadeFinal = fechamentoCaixa.getQuantidadeFinal();
    }
}
