package com.parcao.repository;

import com.parcao.models.Abastecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbastecimentoRepository extends JpaRepository<Abastecimento, Long>{
    @Transactional
    @Modifying
    @Query(value = "update abastecimento_itens set quantidade = :qtd from  abastecimento where abastecimento.id = abastecimento_itens.abastecimento_id and abastecimento.filial_id = :filialId and abastecimento_itens.id = :produtoId", nativeQuery = true)
    Integer updateAbastecimento(int qtd, Long filialId, Long produtoId);

    Optional<Abastecimento> findAbastecimentoByIdFilial(Long id);

    @Transactional
    @Modifying
    @Query(value = "select abastecimento.* from  abastecimento, abastecimento_itens where abastecimento.id = abastecimento_itens.abastecimento_id and abastecimento.filial_id = :filialId and abastecimento_itens.id = :produtoId", nativeQuery = true)
    List<Abastecimento> getRowCountAbastecimento(Long filialId, Long produtoId);
}

