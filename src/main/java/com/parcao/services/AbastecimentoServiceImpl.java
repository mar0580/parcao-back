package com.parcao.services;

import com.parcao.model.entity.Abastecimento;
import com.parcao.repository.AbastecimentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbastecimentoServiceImpl implements AbastecimentoService{
    final AbastecimentoRepository abastecimentoRepository;

    public AbastecimentoServiceImpl(AbastecimentoRepository abastecimentoRepository) {this.abastecimentoRepository = abastecimentoRepository;}

    @Override
    public Abastecimento save(Abastecimento abastecimento) {
        return abastecimentoRepository.save(abastecimento);
    }

    /**
     * Subtrai a quantidade vendida do produto no pedido ao estoque da filial
     * @param qtd
     * @param idFilial
     * @param idProduto
     * @return
     */
    @Override
    public int updateAbastecimento(int qtd, Long idFilial, Long idProduto) {
        return abastecimentoRepository.reduzirQuantidadeProduto(qtd, idFilial, idProduto);
    }

    @Override
    public List<Abastecimento> getRowCountAbastecimento(Long idFilial, Long idProduto) { return abastecimentoRepository.buscarAbastecimentosPorProduto(idFilial, idProduto); }

    @Override
    public void adicionaQuantidadeProdutoAbastecimento(int qtd, Long idFilial, Long idProduto){ abastecimentoRepository.adicionaQuantidadeProdutoAbastecimento(qtd, idFilial, idProduto); }

    @Override
    public List<Abastecimento> getRowCountQuantidadeAbastecimento(Long idFilial, Long idProduto, int qtd) { return abastecimentoRepository.buscarAbastecimentosComQuantidadeMenor(idFilial, idProduto, qtd); }
}
