package com.parcao.model.entity;

import com.parcao.model.dto.PedidoItemDto;
import com.parcao.services.ProdutoService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.BigInteger;

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
    private BigDecimal custoTotal;

    @Autowired
    public PedidoItem(PedidoItemDto produto, ProdutoService produtoService) {
        this.id = produto.getId();
        this.descricaoProduto = produto.getDescricaoProduto();
        this.quantidade = produto.getQuantidade();
        this.valorTotal = this.valorTotalItem(produto.getQuantidade(),produto.getValorUnitario());
        this.valorUnitario = produto.getValorUnitario();
        this.custoTotal = this.valorTotalItem(produto.getQuantidade(),produtoService.findCustoProdutoById(produto.getId()));
    }

    private BigDecimal valorTotalItem(int quantidade, BigDecimal valorUnitario){
        BigDecimal itemCost  = new BigDecimal(BigInteger.ZERO,  2);

        BigDecimal totalCost = new BigDecimal(BigInteger.ZERO,  2);

        itemCost = valorUnitario.multiply(new BigDecimal(quantidade));
        return totalCost.add(itemCost);
    }
}
