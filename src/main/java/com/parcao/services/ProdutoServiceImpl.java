package com.parcao.services;

import com.parcao.exception.ProdutoJaCadastradoException;
import com.parcao.exception.ResourceNotFoundException;
import com.parcao.model.dto.ProdutoDto;
import com.parcao.model.entity.Produto;
import com.parcao.model.mapper.ProdutoMapper;
import com.parcao.repository.ProdutoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

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
        return produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public boolean existsById(Long id) {
        return produtoRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        produtoRepository.deleteById(id);
    }

    @Override
    public Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PRODUTO_NAO_ENCONTRADO"));
    }

    @Override
    @Transactional
    public Produto atualizarProduto(Long id, ProdutoDto produtoDto) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PRODUTO_NAO_ENCONTRADO"));

        // Usando um mapper ou manualmente para copiar propriedades
        Produto produtoAtualizado = ProdutoMapper.toEntity(produtoDto);
        produtoAtualizado.setId(produtoExistente.getId());

        return produtoRepository.save(produtoAtualizado);
    }

    @Transactional
    public Produto save(ProdutoDto produtoDto) throws ProdutoJaCadastradoException {
        if (produtoRepository.existsByDescricaoProduto(produtoDto.getDescricaoProduto())) {
            throw new ProdutoJaCadastradoException("PRODUTO_JA_CADASTRADO");
        }

        Produto produto = new Produto();
        BeanUtils.copyProperties(produtoDto, produto);

        return produtoRepository.save(produto);
    }

    @Override
    public int updateProdutoEstoque(Long id, int quantidade) {
       return produtoRepository.atualizarEstoqueProduto(id, quantidade);
    }
    @Override
    public BigDecimal findCustoProdutoById(Long id){
        return produtoRepository.buscarCustoProdutoPorId(id);
    }
}
