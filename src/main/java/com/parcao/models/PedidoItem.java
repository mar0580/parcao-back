package com.parcao.models;

import com.parcao.dto.PedidoItemDto;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItem {
    private Long idProduto;
    private String descricaoProduto;
    private int quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    public PedidoItem(PedidoItemDto produto) {
        this.idProduto = produto.getIdProduto();
        this.descricaoProduto = produto.getDescricaoProduto();
        this.quantidade = produto.getQuantidade();
        this.valorTotal = produto.getValorTotal();
        this.valorUnitario = produto.getValorUnitario();
    }
}
