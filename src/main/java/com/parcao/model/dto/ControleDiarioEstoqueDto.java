package com.parcao.model.dto;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.io.Serializable;

@Getter
@Setter
@SqlResultSetMapping(name = "AggregateStatsResult", classes = {
        @ConstructorResult(targetClass = ControleDiarioEstoqueDto.class,
                columns = {
                        @ColumnResult(name = "id"),
                        @ColumnResult(name = "inicio"),
                        @ColumnResult(name = "entrada"),
                        @ColumnResult(name = "perda"),
                        @ColumnResult(name = "quantidadeFinal"),
                        @ColumnResult(name = "saida"),
                        @ColumnResult(name = "observacao")
                })
})
public class ControleDiarioEstoqueDto extends FechamentoCaixaItemDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String observacao;

    public ControleDiarioEstoqueDto() {
        super();
    }
}
