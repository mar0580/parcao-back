package com.parcao.repository;

import com.parcao.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Boolean existsByTelefone(String telefone);

    List<Cliente> findAll();

    List<Cliente> findBySaldoCreditoGreaterThan(BigDecimal saldoCredito);

    @Transactional
    @Modifying
    @Query(value = "select c.id from cliente c where c.id = :id and c.saldo_credito >= :valorCompra", nativeQuery = true)
    Optional<Cliente> getClientPositiveBalance(Long id, BigDecimal valorCompra);
}
