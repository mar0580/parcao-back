package com.parcao.services.impl;

import com.parcao.model.entity.FechamentoCaixa;
import com.parcao.repository.FechamentoCaixaRepository;
import com.parcao.services.FechamentoCaixaService;
import org.springframework.stereotype.Service;

@Service
public class FechamentoCaixaServiceImpl implements FechamentoCaixaService {
    final FechamentoCaixaRepository fechamentoCaixaRepository;

    public FechamentoCaixaServiceImpl(FechamentoCaixaRepository fechamentoCaixaRepository) {
        this.fechamentoCaixaRepository = fechamentoCaixaRepository;
    }

    @Override
    public FechamentoCaixa save(FechamentoCaixa fechamentoCaixa) { return fechamentoCaixaRepository.save(fechamentoCaixa); }
}
