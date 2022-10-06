package com.parcao.repository;

import com.parcao.models.TaxaVenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxaVendaRepository extends JpaRepository<TaxaVenda, Long> {
    boolean existsByNomeTaxa(String nomeTaxa);

    List<TaxaVenda> findAll();
}
