package com.parcao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaxaVendaDto {
    private Long id;
    private String nomeTaxa;
    private BigDecimal valorTaxa;
    private BigDecimal percentualTaxa;
}
