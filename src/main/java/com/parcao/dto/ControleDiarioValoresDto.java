package com.parcao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ControleDiarioValoresDto {
    private Long id; //id = idProduto
    private int inicio;
    private int entrada;
    private int perda;
    private int quantidadeFinal;
    private String observacao;

    private int saida;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotalCustoUnitario;
    private BigDecimal valorTotalBruto;
    private BigDecimal valorTotalLiquido;
    private BigDecimal valorTotalBrutoPeriodo;
    private BigDecimal valorTotaLiquidoPeriodo;
}
