package com.parcao.service.impl;

import com.parcao.exception.ResourceNotFoundException;
import com.parcao.dto.FilialDTO;
import com.parcao.model.Filial;
import com.parcao.enums.MensagemEnum;
import com.parcao.repository.FilialRepository;
import com.parcao.service.IFilialService;
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
public class FilialServiceImpl implements IFilialService {

    private static final Logger logger = LoggerFactory.getLogger(FilialServiceImpl.class);
    private static final String CORRELATION_ID = "correlationId";

    private final FilialRepository filialRepository;

    public FilialServiceImpl(FilialRepository filialRepository) {
        this.filialRepository = filialRepository;
    }

    @Override
    public boolean existsByNomeLocal(String nomeLocal) {
        return filialRepository.existsByNomeLocal(nomeLocal);
    }

    @Override
    public Filial save(Filial filial) {
        return filialRepository.save(filial);
    }

    @Override
    @Transactional
    public Filial createFilial(FilialDTO filialDTO) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Criando nova filial: {}", correlationId, filialDTO.getNomeLocal());

        if (existsByNomeLocal(filialDTO.getNomeLocal())) {
            logger.warn("[correlationId={}] Filial já existe: {}", correlationId, filialDTO.getNomeLocal());
            throw new IllegalStateException(MensagemEnum.FILIAL_JA_CADASTRADA.getMensagem());
        }

        Filial filial = new Filial();
        BeanUtils.copyProperties(filialDTO, filial);
        Filial saved = save(filial);

        logger.info("[correlationId={}] Filial criada com sucesso: {}", correlationId, saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public Filial updateFilial(Long id, FilialDTO filialDTO) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Atualizando filial: {}", correlationId, id);

        Filial filialExistente = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MensagemEnum.FILIAL_NAO_EXISTE.getMensagem()));

        Filial filial = new Filial();
        BeanUtils.copyProperties(filialDTO, filial);
        filial.setId(filialExistente.getId());

        Filial updated = save(filial);
        logger.info("[correlationId={}] Filial atualizada com sucesso: {}", correlationId, id);
        return updated;
    }

    @Override
    public List<Filial> findAll() {
        return filialRepository.findAll(Sort.by(Sort.Direction.ASC, "nomeLocal"));
    }

    @Override
    public boolean existsById(Long id) {
        return filialRepository.existsById(id);
    }

    @Override
    public void deleleById(Long id) {
        filialRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteFilial(Long id) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Excluindo filial: {}", correlationId, id);

        if (!existsById(id)) {
            throw new ResourceNotFoundException(MensagemEnum.FILIAL_NAO_EXISTE.getMensagem());
        }
        deleleById(id);
        logger.info("[correlationId={}] Filial excluída com sucesso: {}", correlationId, id);
    }

    @Override
    public Optional<Filial> findById(Long id) {
        return filialRepository.findById(id);
    }

    @Override
    public Filial getFilialById(Long id) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Buscando filial: {}", correlationId, id);

        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MensagemEnum.FILIAL_NAO_EXISTE.getMensagem()));
    }

    @Override
    public Optional<Filial> findByNomeLocal(String nomeLocal) {
        return filialRepository.findByNomeLocal(nomeLocal);
    }
}
