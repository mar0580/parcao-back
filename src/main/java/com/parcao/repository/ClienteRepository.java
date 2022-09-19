package com.parcao.repository;

import com.parcao.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNomeCliente(String nomeCliente);

    Boolean existsByNomeCliente(String nomeCliente);

    Boolean existsByTelefone(String telefone);

    List<Cliente> findAll();
}
