package com.parcao.security.services;

import com.parcao.models.Cliente;
import com.parcao.models.Filial;
import com.parcao.repository.FilialRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilialServiceImpl implements FilialService{

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
        return filialRepository.findAll();
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
}
