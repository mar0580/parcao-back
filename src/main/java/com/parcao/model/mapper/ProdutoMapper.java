package com.parcao.model.mapper;

import com.parcao.model.dto.ProdutoDTO;
import com.parcao.model.entity.Produto;


public class ProdutoMapper {
    public static Produto toEntity(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setDescricaoProduto(dto.getDescricaoProduto());
        produto.setValorUnitario(dto.getValorUnitario());
        produto.setValorCustoUnitario(dto.getValorCustoUnitario());
        produto.setQuantidade(dto.getQuantidade());
        return produto;
    }
}
