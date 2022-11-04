package com.parcao.models;

import com.parcao.dto.PedidoItemDto;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItem {
    private Long id;
    private String descricaoProduto;
    private int quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    public PedidoItem(PedidoItemDto produto) {
        this.id = produto.getId();
        this.descricaoProduto = produto.getDescricaoProduto();
        this.quantidade = produto.getQuantidade();
        this.valorTotal = this.valorTotalItem(produto.getQuantidade(),produto.getValorUnitario());
        this.valorUnitario = produto.getValorUnitario();
    }

    private BigDecimal valorTotalItem(int quantidade, BigDecimal valorUnitario){
        BigDecimal itemCost  = new BigDecimal(BigInteger.ZERO,  2);

        BigDecimal totalCost = new BigDecimal(BigInteger.ZERO,  2);

        itemCost = valorUnitario.multiply(new BigDecimal(quantidade));
        return totalCost.add(itemCost);
    }
}
