package com.parcao.service;

import com.parcao.model.dto.ClienteDTO;
import com.parcao.model.entity.Cliente;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IClienteService {

    boolean existsByTelefone(String telefone, ClienteDTO ClienteDTO);

    Cliente createCliente(ClienteDTO clienteDTO);

    List<Cliente> findAll();

    List<Cliente> findClienteBySaldoCredito();

    boolean existsById(Long id);

    void deleleById(Long id);

    void deleteCliente(Long id);

    Optional<Cliente> findById(Long id);

    Cliente getClienteById(Long id);

    Cliente save(Cliente cliente);

    boolean existsByIdAndSaldoCreditoGreaterThanEqual(Long id, BigDecimal saldoCredito);

    void verificarSaldoCredito(Long id, BigDecimal valorCompra);

    Cliente updateCliente(ClienteDTO ClienteDTO);

    Cliente updateClienteById(Long id, ClienteDTO clienteDTO);

    void updateSaldoCliente(Long id, BigDecimal saldoCredito);

    void updateSaldoClienteById(Long id, BigDecimal valorCompra);
}
