package com.parcao.services.impl;

import com.parcao.model.dto.TaxaVendaDTO;
import com.parcao.model.entity.TaxaVenda;
import com.parcao.repository.TaxaVendaRepository;
import com.parcao.services.TaxaVendaService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaxaVendaServiceImpl implements TaxaVendaService {

    final TaxaVendaRepository taxaVendaRepository;

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
    public TaxaVenda save(TaxaVendaDTO TaxaVendaDTO) {
        TaxaVenda taxaVenda = new TaxaVenda();
        BeanUtils.copyProperties(TaxaVendaDTO, taxaVenda);
        return taxaVendaRepository.save(taxaVenda);
    }
}
