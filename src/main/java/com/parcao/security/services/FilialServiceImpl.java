package com.parcao.security.services;

import com.parcao.dto.ClienteDto;
import com.parcao.dto.FilialDto;
import com.parcao.models.Cliente;
import com.parcao.models.Filial;
import com.parcao.repository.ClienteRepository;
import com.parcao.repository.FilialRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Object save(FilialDto filialDto) {
        Filial filial = new Filial();
        BeanUtils.copyProperties(filialDto, filial);
        return filialRepository.save(filial);
    }

    @Override
    public Object findAll(Pageable pageable) {
        return filialRepository.findAll(pageable);
    }

    @Override
    public boolean existsById(Long id) {
        return filialRepository.existsById(id);
    }

    @Override
    public void deleleById(Long id) {
        filialRepository.deleteById(id);
    }

}
