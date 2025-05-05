package com.parcao.services.impl;

import com.parcao.model.entity.Filial;
import com.parcao.repository.FilialRepository;
import com.parcao.services.IFilialService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilialServiceImpl implements IFilialService {

    final FilialRepository filialRepository;

    public FilialServiceImpl(FilialRepository filialRepository) {
        this.filialRepository = filialRepository;
    }

    @Override
    public boolean existsByNomeLocal(String nomeLocal) {
        return filialRepository.existsByNomeLocal(nomeLocal);
    }

    @Override
    public Filial save(Filial filial) { return filialRepository.save(filial); }

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
    public Optional<Filial> findById(Long id) {
        return filialRepository.findById(id);
    }

    @Override
    public Optional<Filial> findByNomeLocal(String nomeLocal) {
        return filialRepository.findByNomeLocal(nomeLocal);
    }
}
