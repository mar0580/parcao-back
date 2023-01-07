package com.parcao.security.services;

import com.parcao.models.FechamantoCaixaItemTela;
import com.parcao.models.FechamentoCaixa;
import com.parcao.repository.FechamentoCaixaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FechamentoCaixaServiceImpl implements FechamentoCaixaService{
    final FechamentoCaixaRepository fechamentoCaixaRepository;

    public FechamentoCaixaServiceImpl(FechamentoCaixaRepository fechamentoCaixaRepository) {
        this.fechamentoCaixaRepository = fechamentoCaixaRepository;
    }

    @Override
    public FechamentoCaixa save(FechamentoCaixa fechamentoCaixa) { return fechamentoCaixaRepository.save(fechamentoCaixa); }

    @Override
    public List<FechamantoCaixaItemTela> selectFechamentoCaixaProduto(Long filialId, Long produtoId, String dataInicial, String dataFinal) {
        return fechamentoCaixaRepository.selectFechamentoCaixaProduto(filialId, produtoId, dataInicial, dataFinal);
    }
}
