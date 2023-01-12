package com.parcao.models;

import com.parcao.dto.FechamentoCaixaItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;


@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FechamentoCaixaItem {

    private Long id;
    private int inicio;
    private int entrada;
    private int perda;
    private int quantidadeFinal;

    public FechamentoCaixaItem(FechamentoCaixaItemDto fechamentoCaixa) {
        this.id = fechamentoCaixa.getId();
        this.inicio = fechamentoCaixa.getInicio();
        this.entrada = fechamentoCaixa.getEntrada();
        this.perda = fechamentoCaixa.getPerda();
        this.quantidadeFinal = fechamentoCaixa.getQuantidadeFinal();
    }
}
