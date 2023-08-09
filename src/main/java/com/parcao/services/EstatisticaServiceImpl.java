package com.parcao.services;

import com.parcao.dao.EstatisticaRepository;
import com.parcao.dto.EstatisticaDto;
import com.parcao.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
@Service
public class EstatisticaServiceImpl implements EstatisticaService {
    @Autowired
    private EstatisticaRepository estatisticaRepository;

    public List<EstatisticaDto> selectEstatisticaPorTipoPagamento(Long idFilial, String dataInicial, String dataFinal) throws ParseException {
        List<Object[]> estatisticaPorTipoPagamento = estatisticaRepository.selectEstatisticaPorTipoPagamento(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDto> listEstatisticas = new ArrayList<>();
        for(Object[] item : estatisticaPorTipoPagamento){
            EstatisticaDto estatisticaDto = new EstatisticaDto();
            estatisticaDto.setEstatisticaNomeTaxa(item[0].toString());
            estatisticaDto.setEstatisticaQuantidadeVendaTipoPagamento((int)item[1]);
            estatisticaDto.setEstatisticaValorTotalVendaTipoPagamento((int)item[2]);
            listEstatisticas.add(estatisticaDto);
        }
        return listEstatisticas;
    }

    @Override
    public List<EstatisticaDto> selectPerdasPorMes(Long idFilial, String dataInicial, String dataFinal) throws ParseException {
        List<Object[]> perdasPorMes = estatisticaRepository.selectPerdasPorMes(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDto> listEstatisticas = new ArrayList<>();
        for(Object[] item : perdasPorMes){
            EstatisticaDto estatisticaDto = new EstatisticaDto();
            estatisticaDto.setMes(item[0].toString());
            estatisticaDto.setQuantidadePerda((int)item[1]);
            listEstatisticas.add(estatisticaDto);
        }
        return listEstatisticas;
    }

    @Override
    public List<EstatisticaDto> selectPerdasPorProduto(Long idFilial, String dataInicial, String dataFinal) throws ParseException {
        List<Object[]> perdasPorProduto = estatisticaRepository.selectPerdasPorProduto(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDto> listEstatisticas = new ArrayList<>();
        for(Object[] item : perdasPorProduto){
            EstatisticaDto estatisticaDto = new EstatisticaDto();
            estatisticaDto.setNomeProduto(item[0].toString());
            estatisticaDto.setQuantidadePerda((int)item[1]);
            listEstatisticas.add(estatisticaDto);
        }
        return listEstatisticas;
    }
/*
    @Override
    public List<Object[]> selectTotalVendasDiaria(Long idFilial, Timestamp dataInicial, Timestamp dataFinal) {
        return null;
    }

    @Override
    public List<Object[]> selectTotalVendasMensais(Long idFilial, Timestamp dataInicial, Timestamp dataFinal) {
        return null;
    }*/
}
