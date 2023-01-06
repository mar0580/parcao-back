package com.parcao.security.services;

import com.parcao.models.FechamentoCaixa;
import com.parcao.repository.FechamentoCaixaRepository;
import org.springframework.stereotype.Service;

@Service
public class FechamentoCaixaServiceImpl implements FechamentoCaixaService{
    final FechamentoCaixaRepository fechamentoCaixaRepository;

    public FechamentoCaixaServiceImpl(FechamentoCaixaRepository fechamentoCaixaRepository) {
        this.fechamentoCaixaRepository = fechamentoCaixaRepository;
    }

    @Override
    public FechamentoCaixa save(FechamentoCaixa fechamentoCaixa) { return fechamentoCaixaRepository.save(fechamentoCaixa); }
}
