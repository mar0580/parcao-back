package com.parcao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDto {
    private Long id;
    private Long idFilial;
    private Long idCliente;
    private Long idUser;
    private Long idTaxaVenda;
    private BigDecimal valorTotal;
    private String tpPagamento;
    private Set<PedidoItemDto> produtos;
}
