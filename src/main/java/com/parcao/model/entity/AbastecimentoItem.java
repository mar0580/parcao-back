package com.parcao.model.entity;

import com.parcao.model.dto.AbastecimentoItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbastecimentoItem {
    private Long id;
    private String descricaoProduto;
    private int quantidade;

    public AbastecimentoItem(AbastecimentoItemDTO produto) {
        this.id = produto.getId();
        this.descricaoProduto = produto.getDescricaoProduto();
        this.quantidade = produto.getQuantidade();
    }
}
