package com.parcao.dao;

import com.parcao.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Boolean existsByTelefone(String telefone);

    List<Cliente> findAll();

    List<Cliente> findBySaldoCreditoGreaterThan(BigDecimal saldoCredito);

    boolean existsByIdAndSaldoCreditoGreaterThanEqual(Long id, BigDecimal saldoCredito);

    @Transactional
    @Modifying
    @Query(value = "update cliente set saldo_credito = saldo_credito - :saldoCredito where id = :id", nativeQuery = true)
    void updateSaldoCliente(Long id, BigDecimal saldoCredito);
}
