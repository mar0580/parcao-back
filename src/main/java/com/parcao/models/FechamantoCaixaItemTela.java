package com.parcao.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FechamantoCaixaItemTela implements Serializable {

    private Long id;
    private int inicio;
    private int entrada;
    private int perda;
    private int quantidadeFinal;
    private String observacao;
}
