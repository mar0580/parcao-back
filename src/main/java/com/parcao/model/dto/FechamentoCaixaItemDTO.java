package com.parcao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FechamentoCaixaItemDTO {
    private Long id; //id = idProduto
    private int inicio;
    private int entrada;
    private int perda;
    private int quantidadeFinal;

    private int saida;

    public FechamentoCaixaItemDTO() {

    }
}
