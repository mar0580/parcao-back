package com.parcao.service.impl;

import com.google.common.base.Strings;
import com.parcao.dao.VendaRepository;
import com.parcao.exception.ResourceNotFoundException;
import com.parcao.model.dto.ControleDiarioValoresDTO;
import com.parcao.model.entity.Produto;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.service.IFechamentoCaixaItemService;
import com.parcao.service.IProdutoService;
import com.parcao.service.IVendaService;
import com.parcao.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendaServiceImpl implements IVendaService {

    private static final Logger logger = LoggerFactory.getLogger(VendaServiceImpl.class);
    private static final String CORRELATION_ID = "correlationId";
    private static final String COPO = "copo";
    private static final String GARRAFA = "garrafa";

    private final VendaRepository vendaRepository;
    private final IProdutoService produtoService;
    private final IFechamentoCaixaItemService fechamentoCaixaItemService;

    public VendaServiceImpl(VendaRepository vendaRepository, IProdutoService produtoService,
                            IFechamentoCaixaItemService fechamentoCaixaItemService) {
        this.vendaRepository = vendaRepository;
        this.produtoService = produtoService;
        this.fechamentoCaixaItemService = fechamentoCaixaItemService;
    }

    @Override
    public List<Object[]> selectSomatorioVendaProduto(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal) {
        return vendaRepository.selectSomatorioVendaProduto(idFilial, idProduto, dataInicial, dataFinal);
    }

    @Override
    public Object selectValorTotalCocoCopoGarrafa(Long idFilial, String descricaoProduto, Timestamp dataInicial, Timestamp dataFinal) {
        return vendaRepository.selectValorTotalCocoCopoGarrafa(idFilial, descricaoProduto, dataInicial, dataFinal);
    }

    @Override
    public List<Object[]> somatorioTotalBrutoPeriodo(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal) {
        return vendaRepository.somatorioTotalBrutoPeriodo(idFilial, idProduto, dataInicial, dataFinal);
    }

    @Override
    public Object somatorioTotalLiquidoPeriodo(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal) {
        return vendaRepository.somatorioTotalLiquidoPeriodo(idFilial, idProduto, dataInicial, dataFinal);
    }

    @Override
    public Object totalCustosCoco(Long idFilial, Timestamp dataInicial, Timestamp dataFinal) {
        return vendaRepository.totalCustosCoco(idFilial, dataInicial, dataFinal);
    }

    @Override
    public ControleDiarioValoresDTO buscaSomatorioVendaProduto(Long idFilial, Long idProduto, String dataInicial, String dataFinal) throws ParseException {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Buscando somatório de venda para filial: {}, produto: {}", correlationId, idFilial, idProduto);

        List<Object[]> fechamentoCaixaItemProduto;
        if (dataInicial.compareTo(dataFinal) == 0) {
            fechamentoCaixaItemProduto = fechamentoCaixaItemService.selectFechamentoCaixaProdutoDiario(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        } else {
            fechamentoCaixaItemProduto = fechamentoCaixaItemService.selectFechamentoCaixaProdutoPeriodo(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        }

        if (fechamentoCaixaItemProduto.isEmpty()) {
            logger.info("[correlationId={}] Fechamento de caixa não encontrado", correlationId);
            throw new ResourceNotFoundException(MensagemEnum.FECHAMENTO_CAIXA_NAO_ENCONTRADO.getMensagem());
        }

        ControleDiarioValoresDTO c = new ControleDiarioValoresDTO();
        List<ControleDiarioValoresDTO> customResponseList = new ArrayList<>();

        for (Object[] itemFechamentoCaixaItemProduto : fechamentoCaixaItemProduto) {
            BigInteger b = new BigInteger(itemFechamentoCaixaItemProduto[0].toString());
            c.setId(b.longValue());
            c.setInicio((int) itemFechamentoCaixaItemProduto[1]);
            c.setEntrada((int) itemFechamentoCaixaItemProduto[2]);
            c.setPerda((int) itemFechamentoCaixaItemProduto[3]);
            c.setQuantidadeFinal((int) itemFechamentoCaixaItemProduto[4]);
            c.setSaida((int) itemFechamentoCaixaItemProduto[5]);
            if (dataInicial.compareTo(dataFinal) == 0) {
                c.setObservacao(((String) itemFechamentoCaixaItemProduto[6]));
            } else {
                c.setObservacao(" ");
            }
            customResponseList.add(c);
        }

        List<Produto> produtos = produtoService.findAll();
        BigDecimal valorTotalBruto = BigDecimal.ZERO;
        BigDecimal valorTotalLiquido = BigDecimal.ZERO;

        for (Produto p : produtos) {
            List<Object[]> somatorioTotalBrutoPeriodo = vendaRepository.somatorioTotalBrutoPeriodo(idFilial, p.getId(), Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
            for (Object[] itemSomatorioTotalBrutoPeriodo : somatorioTotalBrutoPeriodo) {
                if (!Strings.isNullOrEmpty(itemSomatorioTotalBrutoPeriodo[3].toString())) {
                    valorTotalBruto = valorTotalBruto.add(new BigDecimal(itemSomatorioTotalBrutoPeriodo[3].toString()).multiply(new BigDecimal(itemSomatorioTotalBrutoPeriodo[2].toString())));
                }
            }
            Object somatorioTotalLiquidoPeriodo = vendaRepository.somatorioTotalLiquidoPeriodo(idFilial, p.getId(), Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
            valorTotalLiquido = valorTotalLiquido.add(new BigDecimal(somatorioTotalLiquidoPeriodo.toString()));
        }

        List<Object[]> somatorioVendaProduto = vendaRepository.selectSomatorioVendaProduto(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        Object valorTotalCocoCopoGarrafa = vendaRepository.selectValorTotalCocoCopoGarrafa(idFilial, COPO, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        Object valorTotalCocoGarrafa = vendaRepository.selectValorTotalCocoCopoGarrafa(idFilial, GARRAFA, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        Object totalCustosCoco = vendaRepository.totalCustosCoco(idFilial, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));

        if (somatorioVendaProduto.isEmpty()) {
            logger.info("[correlationId={}] Fechamento de caixa não encontrado", correlationId);
            throw new ResourceNotFoundException(MensagemEnum.FECHAMENTO_CAIXA_NAO_ENCONTRADO.getMensagem());
        }

        for (Object[] itemSomatorioVendaProduto : somatorioVendaProduto) {
            c.setPreco(new BigDecimal(itemSomatorioVendaProduto[1].toString()));
            c.setCusto(new BigDecimal(itemSomatorioVendaProduto[2].toString()));
            c.setTotalCusto((new BigDecimal(itemSomatorioVendaProduto[4].toString()).subtract((new BigDecimal(itemSomatorioVendaProduto[5].toString()).multiply(BigDecimal.valueOf(c.getPerda() + c.getSaida()))))));
            c.setTotalCoco(new BigDecimal(valorTotalCocoCopoGarrafa.toString()).add(new BigDecimal(valorTotalCocoGarrafa.toString())));
            c.setTotal(new BigDecimal(itemSomatorioVendaProduto[1].toString()).multiply(BigDecimal.valueOf(c.getSaida())));
            c.setValorTotalBrutoPeriodo(valorTotalBruto);
            c.setValorTotaLiquidoPeriodo(valorTotalLiquido.subtract(new BigDecimal(totalCustosCoco.toString())));
            customResponseList.add(c);
        }

        logger.info("[correlationId={}] Somatório de venda calculado com sucesso", correlationId);
        return customResponseList.get(0);
    }
}
