package com.parcao.security.services;

import com.parcao.dto.ClienteDto;
import com.parcao.models.Cliente;
import com.parcao.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ClienteService {

    boolean existsByTelefone(String telefone);

    List<Cliente> findAll();

    List<Cliente> findClienteBySaldoCredito(BigDecimal saldoCredito);

    boolean existsById(Long id);

    void deleleById(Long id);

    Optional<Cliente> findById(Long id);

    Cliente save(Cliente cliente);

    boolean existsByIdAndSaldoCreditoGreaterThanEqual(Long id, BigDecimal saldoCredito);
}
