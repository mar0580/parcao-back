package com.parcao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FechamentoCaixaItemDto {
    private Long id; //id = idProduto
    private int inicio;
    private int entrada;
    private int perda;
    private int quantidadeFinal;
}
