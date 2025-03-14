package com.parcao.repository;

import com.parcao.model.entity.Cliente;
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

    List<Cliente> findBySaldoCreditoGreaterThan(BigDecimal saldoCredito);

    boolean existsByIdAndSaldoCreditoGreaterThanEqual(Long id, BigDecimal saldoCredito);

    @Transactional
    @Modifying
    @Query("UPDATE Cliente c SET c.saldoCredito = c.saldoCredito - :saldoCredito WHERE c.id = :id")
    void updateSaldoCliente(Long id, BigDecimal saldoCredito);
}
