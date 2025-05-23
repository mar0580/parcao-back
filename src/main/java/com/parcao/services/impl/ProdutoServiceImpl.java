package com.parcao.services.impl;

import com.parcao.exception.ProdutoJaCadastradoException;
import com.parcao.exception.ResourceNotFoundException;
import com.parcao.model.dto.ProdutoDTO;
import com.parcao.model.entity.Produto;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.model.mapper.ProdutoMapper;
import com.parcao.repository.ProdutoRepository;
import com.parcao.services.IProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoServiceImpl implements IProdutoService {

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
        verificarExistenciaProduto(id);
        return produtoRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        verificarExistenciaProduto(id);
        produtoRepository.deleteById(id);
    }

    @Override
    public Produto findById(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MensagemEnum.PRODUTO_NAO_ENCONTRADO.getMensagem()));
    }

    @Override
    @Transactional
    public Produto atualizarProduto(Long id, ProdutoDTO ProdutoDTO) {
        verificarExistenciaProduto(id);
        Produto produtoAtualizado = ProdutoMapper.toEntity(ProdutoDTO);
        produtoAtualizado.setId(id);

        return produtoRepository.save(produtoAtualizado);
    }

    @Transactional
    public Produto save(ProdutoDTO ProdutoDTO) throws ProdutoJaCadastradoException {
        if (produtoRepository.existsByDescricaoProduto(ProdutoDTO.getDescricaoProduto())) {
            throw new ProdutoJaCadastradoException(MensagemEnum.PRODUTO_JA_CADASTRADO.getMensagem());
        }

        Produto produto = new Produto();
        BeanUtils.copyProperties(ProdutoDTO, produto);

        return produtoRepository.save(produto);
    }

    @Override
    public int updateProdutoEstoque(Long id, int quantidade) {
        verificarExistenciaProduto(id);
        return produtoRepository.atualizarEstoqueProduto(id, quantidade);
    }
    @Override
    public BigDecimal findCustoProdutoById(Long id){
        return produtoRepository.buscarCustoProdutoPorId(id);
    }

    private void verificarExistenciaProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException(MensagemEnum.PRODUTO_NAO_ENCONTRADO.getMensagem());
        }
    }
}
