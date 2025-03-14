package com.parcao.repository;

import com.parcao.model.entity.Abastecimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AbastecimentoRepository extends JpaRepository<Abastecimento, Long>{

    @Modifying
    @Query(value = "UPDATE abastecimento_itens ai " +
            "SET quantidade = (ai.quantidade - :qtd) " +
            "FROM abastecimento a " +
            "WHERE a.id = ai.abastecimento_id " +
            "AND a.filial_id = :filialId " +
            "AND ai.id = :produtoId", nativeQuery = true)
    int reduzirQuantidadeProduto(int qtd, Long filialId, Long produtoId);

    @Query(value = "SELECT a.* FROM abastecimento a " +
            "JOIN abastecimento_itens ai ON a.id = ai.abastecimento_id " +
            "WHERE a.filial_id = :filialId " +
            "AND ai.id = :produtoId", nativeQuery = true)
    List<Abastecimento> buscarAbastecimentosPorProduto(Long filialId, Long produtoId);

    @Modifying
    @Query(value = "UPDATE abastecimento_itens ai " +
            "SET quantidade = (ai.quantidade + :qtd) " +
            "FROM abastecimento a " +
            "WHERE a.id = ai.abastecimento_id " +
            "AND a.filial_id = :filialId " +
            "AND ai.id = :produtoId", nativeQuery = true)
    void adicionaQuantidadeProdutoAbastecimento(int qtd, Long filialId, Long produtoId);

    @Query(value = "SELECT a.* FROM abastecimento a " +
            "JOIN abastecimento_itens ai ON a.id = ai.abastecimento_id " +
            "WHERE a.filial_id = :filialId " +
            "AND ai.id = :produtoId " +
            "AND ai.quantidade < :qtd", nativeQuery = true)
    List<Abastecimento> buscarAbastecimentosComQuantidadeMenor(Long filialId, Long produtoId, int qtd);
}

