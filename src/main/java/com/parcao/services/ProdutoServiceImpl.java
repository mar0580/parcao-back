package com.parcao.services;

import com.parcao.models.Produto;
import com.parcao.repository.ProdutoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    final ProdutoRepository produtoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public boolean existsByDescricaoProduto(String descricaoProduto) {
        return produtoRepository.existsByDescricaoProduto(descricaoProduto);
    }

    @Override
    public List<Produto> findAll() {
        return produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "descricaoProduto"));
    }

    @Override
    public boolean existsById(Long id) {
        return produtoRepository.existsById(id);
    }

    @Override
    public void deleleById(Long id) {
        produtoRepository.deleteById(id);
    }

    @Override
    public Optional<Produto> findById(Long id) {
        return produtoRepository.findById(id);
    }

    @Override
    public Produto save(Produto produto) {
        return produtoRepository.save(produto);
    }
    @Override
    public int updateProdutoEstoque(Long id, int quantidade) {
       return produtoRepository.updateProdutoEstoque(id, quantidade);
    }
    @Override
    public BigDecimal findCustoProdutoById(Long id){
        return produtoRepository.findCustoProdutoById(id);
    }
}
