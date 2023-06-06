package com.parcao.services;

import com.parcao.dto.ClienteDto;
import com.parcao.models.Cliente;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ClienteService {

    boolean existsByTelefone(String telefone, ClienteDto clienteDto);

    List<Cliente> findAll();

    List<Cliente> findClienteBySaldoCredito();

    boolean existsById(Long id);

    void deleleById(Long id);

    Optional<Cliente> findById(Long id);

    Cliente save(Cliente cliente);

    boolean existsByIdAndSaldoCreditoGreaterThanEqual(Long id, BigDecimal saldoCredito);

    Cliente updateCliente(ClienteDto clienteDto);

    void updateSaldoCliente(Long id, BigDecimal saldoCredito);
}
