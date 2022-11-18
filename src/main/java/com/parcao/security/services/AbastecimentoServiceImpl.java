package com.parcao.security.services;

import com.parcao.models.Abastecimento;
import com.parcao.repository.AbastecimentoRepository;
import org.springframework.stereotype.Service;

@Service
public class AbastecimentoServiceImpl implements AbastecimentoService{
    final AbastecimentoRepository abastecimentoRepository;

    public AbastecimentoServiceImpl(AbastecimentoRepository abastecimentoRepository) {this.abastecimentoRepository = abastecimentoRepository;}

    @Override
    public Abastecimento save(Abastecimento abastecimento) {
        return abastecimentoRepository.save(abastecimento);
    }
}
