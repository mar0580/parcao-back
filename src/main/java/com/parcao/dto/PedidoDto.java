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
    private BigDecimal valorTotal;
    private String tpPagamento;
    private Long idFilial;
    private Long idCliente;
    private Long idTaxaVenda;
    private Long idUser;
    private Set<ProdutoDto> produtos;
}
