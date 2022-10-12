package com.parcao.dto;

import com.parcao.models.Cliente;
import com.parcao.models.Filial;
import com.parcao.models.TaxaVenda;
import com.parcao.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDto {
    private Long id;
    private BigDecimal valorTotal;
    private String tpPagamento;
    private Filial idFilial;
    private Cliente idCliente;
    private TaxaVenda idTaxaVenda;
    private User idUser;
}
