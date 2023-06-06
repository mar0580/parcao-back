package com.parcao.services;

import com.parcao.dao.EstatisticaRepository;
import com.parcao.dto.EstatisticaDto;
import com.parcao.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
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




/*
    @Override
    public List<Object[]> selectPerdasPorMes(Long idFilial, Timestamp dataInicial, Timestamp dataFinal) {
        return null;
    }

    @Override
    public List<Object[]> selectPerdasPorProduto(Long idFilial, Timestamp dataInicial, Timestamp dataFinal) {
        return null;
    }

    @Override
    public List<Object[]> selectTotalVendasDiaria(Long idFilial, Timestamp dataInicial, Timestamp dataFinal) {
        return null;
    }

    @Override
    public List<Object[]> selectTotalVendasMensais(Long idFilial, Timestamp dataInicial, Timestamp dataFinal) {
        return null;
    }*/
}
