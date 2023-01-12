package com.parcao.repository;

import com.parcao.models.TaxaVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaxaVendaRepository extends JpaRepository<TaxaVenda, Long> {
    boolean existsByNomeTaxa(String nomeTaxa);

    List<TaxaVenda> findAll();
}
