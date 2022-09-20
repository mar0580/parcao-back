package com.parcao.security.services;

import com.parcao.dto.ClienteDto;
import com.parcao.dto.FilialDto;
import org.springframework.data.domain.Pageable;

public interface FilialService {

    public boolean existsByNomeLocal(String nomeLocal);

    public Object save(FilialDto filialDto);

    public Object findAll(Pageable pageable);

    public boolean existsById(Long id);

    public void deleleById(Long id);
}
