package com.parcao.security.services;

import com.parcao.models.Produto;
import com.parcao.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoServiceImpl implements ProdutoService{

    final ProdutoRepository produtoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public boolean existsByDescricaoProduto(String descricaoProduto) {
        return false;
    }

    @Override
    public List<Produto> findAll() {
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public void deleleById(Long id) {

    }

    @Override
    public Optional<Produto> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Produto save(Produto produto) {
        return null;
    }
}
