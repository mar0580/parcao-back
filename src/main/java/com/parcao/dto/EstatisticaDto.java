package com.parcao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticaDto {

    private String estatisticaNomeTaxa;
    private int estatisticaTotalVendaTipoPagamento;
}
