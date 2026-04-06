package com.parcao.service.impl;

import com.parcao.exception.ResourceNotFoundException;
import com.parcao.exception.TaxaJaCadastradaException;
import com.parcao.dto.TaxaVendaDTO;
import com.parcao.model.TaxaVenda;
import com.parcao.enums.MensagemEnum;
import com.parcao.repository.TaxaVendaRepository;
import com.parcao.service.ITaxaVendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaxaVendaServiceImpl implements ITaxaVendaService {

    private static final Logger logger = LoggerFactory.getLogger(TaxaVendaServiceImpl.class);
    private static final String CORRELATION_ID = "correlationId";

    private final TaxaVendaRepository taxaVendaRepository;

    public TaxaVendaServiceImpl(TaxaVendaRepository taxaVendaRepository) {
        this.taxaVendaRepository = taxaVendaRepository;
    }

    @Override
    public boolean existsByNomeTaxa(String nomeTaxa) {
        return taxaVendaRepository.existsByNomeTaxa(nomeTaxa);
    }

    @Override
    public List<TaxaVenda> findAll() {
        return taxaVendaRepository.findAll(Sort.by(Sort.Direction.ASC, "nomeTaxa"));
    }

    @Override
    public boolean existsById(Long id) {
        return taxaVendaRepository.existsById(id);
    }

    @Override
    public void deleleById(Long id) {
        taxaVendaRepository.deleteById(id);
    }

    @Override
    public Optional<TaxaVenda> findById(Long id) {
        return taxaVendaRepository.findById(id);
    }

    @Override
    public TaxaVenda getTaxaVendaById(Long id) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Buscando taxa de venda: {}", correlationId, id);

        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MensagemEnum.TAXA_NAO_ENCONTRADA.getMensagem()));
    }

    @Override
    public TaxaVenda save(TaxaVendaDTO taxaVendaDTO) {
        TaxaVenda taxaVenda = new TaxaVenda();
        BeanUtils.copyProperties(taxaVendaDTO, taxaVenda);
        return taxaVendaRepository.save(taxaVenda);
    }

    @Override
    @Transactional
    public TaxaVenda createTaxaVenda(TaxaVendaDTO taxaVendaDTO) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Criando nova taxa de venda: {}", correlationId, taxaVendaDTO.getNomeTaxa());

        if (existsByNomeTaxa(taxaVendaDTO.getNomeTaxa())) {
            logger.warn("[correlationId={}] Taxa já existe: {}", correlationId, taxaVendaDTO.getNomeTaxa());
            throw new TaxaJaCadastradaException(MensagemEnum.TAXA_JA_CADASTRADA.getMensagem());
        }

        TaxaVenda saved = save(taxaVendaDTO);
        logger.info("[correlationId={}] Taxa de venda criada com sucesso: {}", correlationId, saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public void deleteTaxaVenda(Long id) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Excluindo taxa de venda: {}", correlationId, id);

        if (!existsById(id)) {
            throw new ResourceNotFoundException(MensagemEnum.TAXA_NAO_ENCONTRADA.getMensagem());
        }
        deleleById(id);
        logger.info("[correlationId={}] Taxa de venda excluída com sucesso: {}", correlationId, id);
    }

    @Override
    @Transactional
    public TaxaVenda updateTaxaVenda(Long id, TaxaVendaDTO taxaVendaDTO) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Atualizando taxa de venda: {}", correlationId, id);

        findById(id).orElseThrow(() -> new ResourceNotFoundException(MensagemEnum.TAXA_NAO_ENCONTRADA.getMensagem()));

        TaxaVenda updated = save(taxaVendaDTO);
        logger.info("[correlationId={}] Taxa de venda atualizada com sucesso: {}", correlationId, id);
        return updated;
    }
}
