package com.parcao.model.dto;

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
    private int estatisticaQuantidadeVendaTipoPagamento;
    private String mes;
    private int quantidadePerda;
    private String nomeProduto;
    private int estatisticaValorTotalVendaTipoPagamento;
    private String diaMes;
}
