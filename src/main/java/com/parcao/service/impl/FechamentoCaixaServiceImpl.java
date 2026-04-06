package com.parcao.service.impl;

import com.parcao.exception.ResourceNotFoundException;
import com.parcao.dto.ControleDiarioEstoqueDTO;
import com.parcao.dto.FechamentoCaixaDTO;
import com.parcao.dto.FechamentoCaixaItemDTO;
import com.parcao.model.FechamentoCaixa;
import com.parcao.model.FechamentoCaixaItem;
import com.parcao.enums.MensagemEnum;
import com.parcao.repository.FechamentoCaixaRepository;
import com.parcao.service.IFechamentoCaixaItemService;
import com.parcao.service.IFechamentoCaixaService;
import com.parcao.service.IProdutoService;
import com.parcao.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FechamentoCaixaServiceImpl implements IFechamentoCaixaService {

    private static final Logger logger = LoggerFactory.getLogger(FechamentoCaixaServiceImpl.class);
    private static final String CORRELATION_ID = "correlationId";

    private final FechamentoCaixaRepository fechamentoCaixaRepository;
    private final IFechamentoCaixaItemService fechamentoCaixaItemService;
    private final IProdutoService produtoService;

    public FechamentoCaixaServiceImpl(FechamentoCaixaRepository fechamentoCaixaRepository,
                                       IFechamentoCaixaItemService fechamentoCaixaItemService,
                                       IProdutoService produtoService) {
        this.fechamentoCaixaRepository = fechamentoCaixaRepository;
        this.fechamentoCaixaItemService = fechamentoCaixaItemService;
        this.produtoService = produtoService;
    }

    @Override
    public FechamentoCaixa save(FechamentoCaixa fechamentoCaixa) {
        return fechamentoCaixaRepository.save(fechamentoCaixa);
    }

    @Override
    @Transactional
    public FechamentoCaixa createFechamentoCaixa(FechamentoCaixaDTO fechamentoCaixaDto) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Iniciando criação de fechamento de caixa", correlationId);

        Set<FechamentoCaixaItemDTO> produtoItemFechamentoCaixaDto = fechamentoCaixaDto.getProdutos();
        Set<FechamentoCaixaItem> produtos = new HashSet<>();

        produtoItemFechamentoCaixaDto.forEach(produto -> {
            if (produtoService.existsById(produto.getId())) {
                produtos.add(new FechamentoCaixaItem(produto));
            }
        });

        FechamentoCaixa fechamentoCaixa = new FechamentoCaixa();
        BeanUtils.copyProperties(fechamentoCaixaDto, fechamentoCaixa);
        fechamentoCaixa.setProdutos(produtos);

        FechamentoCaixa saved = fechamentoCaixaRepository.save(fechamentoCaixa);

        logger.info("[correlationId={}] Fechamento de caixa criado com sucesso", correlationId);
        return saved;
    }

    @Override
    public ControleDiarioEstoqueDTO buscaFechamentoCaixaProduto(Long idFilial, Long idProduto, String dataInicial, String dataFinal) throws ParseException {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Buscando fechamento de caixa para filial: {}, produto: {}", correlationId, idFilial, idProduto);

        List<Object[]> optionalFechamentoCaixaItem;

        if (dataInicial.compareTo(dataFinal) == 0) {
            optionalFechamentoCaixaItem = fechamentoCaixaItemService.selectFechamentoCaixaProdutoDiario(
                    idFilial, idProduto,
                    Util.dateToInicialTimestamp(dataInicial),
                    Util.dateToFinalTimestamp(dataFinal));
        } else {
            optionalFechamentoCaixaItem = fechamentoCaixaItemService.selectFechamentoCaixaProdutoPeriodo(
                    idFilial, idProduto,
                    Util.dateToInicialTimestamp(dataInicial),
                    Util.dateToFinalTimestamp(dataFinal));
        }

        if (optionalFechamentoCaixaItem.isEmpty()) {
            logger.info("[correlationId={}] Fechamento de caixa não encontrado", correlationId);
            throw new ResourceNotFoundException(MensagemEnum.FECHAMENTO_CAIXA_NAO_ENCONTRADO.getMensagem());
        }

        List<ControleDiarioEstoqueDTO> customResponseList = new ArrayList<>();
        for (Object[] item : optionalFechamentoCaixaItem) {
            ControleDiarioEstoqueDTO c = new ControleDiarioEstoqueDTO();
            BigInteger b = new BigInteger(item[0].toString());
            c.setId(b.longValue());
            c.setInicio((int) item[1]);
            c.setEntrada((int) item[2]);
            c.setPerda((int) item[3]);
            c.setQuantidadeFinal((int) item[4]);
            c.setSaida((int) item[5]);
            if (dataInicial.compareTo(dataFinal) == 0) {
                c.setObservacao(((String) item[6]));
            } else {
                c.setObservacao(" ");
            }
            customResponseList.add(c);
        }

        logger.info("[correlationId={}] Fechamento de caixa encontrado com sucesso", correlationId);
        return customResponseList.get(0);
    }
}
