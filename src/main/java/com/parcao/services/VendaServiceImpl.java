package com.parcao.services;

import com.parcao.dto.ControleDiarioEstoqueDto;
import com.parcao.repository.FechamentoCaixaItemRepository;
import com.parcao.repository.VendaRepository;
import com.parcao.utils.Util;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//@Service
public class VendaServiceImpl implements VendaService{
    final VendaRepository vendaRepository;
    final FechamentoCaixaItemRepository fechamentoCaixaItemRepository;

    public VendaServiceImpl(VendaRepository vendaRepository, FechamentoCaixaItemRepository fechamentoCaixaItemRepository) {
        this.vendaRepository = vendaRepository;
        this.fechamentoCaixaItemRepository = fechamentoCaixaItemRepository;
    }

    @Override
    public List<Object[]> selectSomatorioVendaProduto(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal) {
        return vendaRepository.selectSomatorioVendaProduto(idFilial, idProduto, dataInicial, dataFinal);
    }

    @Override
    public Object selectValorBrutoPeriodo(Long idFilial, Timestamp dataInicial, Timestamp dataFinal){
        return vendaRepository.selectValorBrutoPeriodo(idFilial, dataInicial, dataFinal);
    }
}
