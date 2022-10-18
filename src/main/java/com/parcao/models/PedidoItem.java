package com.parcao.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItem {

    private static final long serialVersionUID = 1L;

    private Long idProduto;

    private String descricaoProduto;

    private int quantidade;

    private BigDecimal valorUnitario;

    private BigDecimal valorTotal;



}
