package com.parcao.security.services;

import com.parcao.models.Abastecimento;
import com.parcao.repository.AbastecimentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Optional;

@Service
public class AbastecimentoServiceImpl implements AbastecimentoService{
    final AbastecimentoRepository abastecimentoRepository;

    public AbastecimentoServiceImpl(AbastecimentoRepository abastecimentoRepository) {this.abastecimentoRepository = abastecimentoRepository;}

    @Override
    public Abastecimento save(Abastecimento abastecimento) {
        return abastecimentoRepository.save(abastecimento);
    }

    public Optional<Abastecimento> findAbastecimentoByIdFilial(Long id) {
        return abastecimentoRepository.findAbastecimentoByIdFilial(id);
    }

    @Override
    public Integer updateAbastecimento(int qtd, Long idFilial, Long idProduto) {
    if(abastecimentoRepository.updateAbastecimento(qtd, idFilial, idProduto) != 0) {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
        return abastecimentoRepository.updateAbastecimento(qtd, idFilial, idProduto);
    }

    @Override
    public List<Abastecimento> getRowCountAbastecimento(Long idFilial, Long idProduto) { return abastecimentoRepository.getRowCountAbastecimento(idFilial, idProduto); }
}
