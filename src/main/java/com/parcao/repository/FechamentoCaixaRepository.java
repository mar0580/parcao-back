package com.parcao.repository;

import com.parcao.models.FechamantoCaixaItemTela;
import com.parcao.models.FechamentoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FechamentoCaixaRepository extends JpaRepository<FechamentoCaixa, Long> {

        @Transactional
        @Modifying
        @Query(value = "select fci.id as id, fci.entrada as entrada, fci.inicio as inicio, fci.perda as perda, fci.quantidade_final as quantidadeFinal, fc.observacao as observacao " +
                "from fechamento_caixa fc, fechamento_caixa_itens fci " +
                "where fc.id = fci.fechamento_caixa_id " +
                "and fc.filial_id = :filialId " +
                "and fci.id = :produtoId " +
                "and fc.date_fechamento_caixa between :dataInicial and :dataFinal", nativeQuery = true)
        List<FechamantoCaixaItemTela> selectFechamentoCaixaProduto(Long filialId, Long produtoId, String dataInicial, String dataFinal);
}
