package com.parcao.services.impl;

import com.parcao.dao.EstatisticaRepository;
import com.parcao.model.dto.EstatisticaDTO;
import com.parcao.services.EstatisticaService;
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

    public List<EstatisticaDTO> selectEstatisticaPorTipoPagamento(Long idFilial, String dataInicial, String dataFinal) throws ParseException {
        List<Object[]> estatisticaPorTipoPagamento = estatisticaRepository.selectEstatisticaPorTipoPagamento(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDTO> listEstatisticas = new ArrayList<>();
        for(Object[] item : estatisticaPorTipoPagamento){
            EstatisticaDTO EstatisticaDTO = new EstatisticaDTO();
            EstatisticaDTO.setEstatisticaNomeTaxa(item[0].toString());
            EstatisticaDTO.setEstatisticaQuantidadeVendaTipoPagamento((int)item[1]);
            EstatisticaDTO.setEstatisticaValorTotalVendaTipoPagamento((int)item[2]);
            listEstatisticas.add(EstatisticaDTO);
        }
        return listEstatisticas;
    }

    @Override
    public List<EstatisticaDTO> selectPerdasPorMes(Long idFilial, String dataInicial, String dataFinal) throws ParseException {
        List<Object[]> perdasPorMes = estatisticaRepository.selectPerdasPorMes(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDTO> listEstatisticas = new ArrayList<>();
        for(Object[] item : perdasPorMes){
            EstatisticaDTO EstatisticaDTO = new EstatisticaDTO();
            EstatisticaDTO.setMes(item[0].toString());
            EstatisticaDTO.setQuantidadePerda((int)item[1]);
            listEstatisticas.add(EstatisticaDTO);
        }
        return listEstatisticas;
    }

    @Override
    public List<EstatisticaDTO> selectPerdasPorProduto(Long idFilial, String dataInicial, String dataFinal) throws ParseException {
        List<Object[]> perdasPorProduto = estatisticaRepository.selectPerdasPorProduto(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDTO> listEstatisticas = new ArrayList<>();
        for(Object[] item : perdasPorProduto){
            EstatisticaDTO EstatisticaDTO = new EstatisticaDTO();
            EstatisticaDTO.setNomeProduto(item[0].toString());
            EstatisticaDTO.setQuantidadePerda((int)item[1]);
            listEstatisticas.add(EstatisticaDTO);
        }
        return listEstatisticas;
    }

    @Override
    public List<EstatisticaDTO> selectTotalVendasDiaria(Long idFilial, String dataInicial, String dataFinal) throws ParseException {
        List<Object[]> estatisticaTotalVendasDiaria = estatisticaRepository.selectTotalVendasDiaria(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDTO> listEstatisticas = new ArrayList<>();
        for(Object[] item : estatisticaTotalVendasDiaria){
            EstatisticaDTO EstatisticaDTO = new EstatisticaDTO();
            EstatisticaDTO.setDiaMes(item[0].toString());
            EstatisticaDTO.setEstatisticaValorTotalVendaTipoPagamento((int)item[1]);
            listEstatisticas.add(EstatisticaDTO);
        }
        return listEstatisticas;
    }

    @Override
    public List<EstatisticaDTO> selectTotalVendasMensais(Long idFilial, String dataInicial, String dataFinal) throws ParseException {
        List<Object[]> estatisticaTotalVendasMensais = estatisticaRepository.selectTotalVendasMensais(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        List<EstatisticaDTO> listEstatisticas = new ArrayList<>();
        for(Object[] item : estatisticaTotalVendasMensais){
            EstatisticaDTO EstatisticaDTO = new EstatisticaDTO();
            EstatisticaDTO.setMes(item[0].toString());
            EstatisticaDTO.setEstatisticaValorTotalVendaTipoPagamento((int)item[1]);
            listEstatisticas.add(EstatisticaDTO);
        }
        return listEstatisticas;
    }
}
