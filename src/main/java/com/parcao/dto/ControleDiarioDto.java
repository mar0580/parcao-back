package com.parcao.dto;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import java.io.Serializable;

@Getter
@Setter
@SqlResultSetMapping(name = "AggregateStatsResult", classes = {
        @ConstructorResult(targetClass = ControleDiarioDto.class,
                columns = {
                        @ColumnResult(name = "id"),
                        @ColumnResult(name = "inicio"),
                        @ColumnResult(name = "entrada"),
                        @ColumnResult(name = "perda"),
                        @ColumnResult(name = "quantidadeFinal"),
                        @ColumnResult(name = "observacao")
                })
})
public class ControleDiarioDto extends FechamentoCaixaItemDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String observacao;
    public ControleDiarioDto(Long id, int inicio, int entrada, int perda, int quantidadeFinal) {
        super(id, inicio, entrada, perda, quantidadeFinal);
        this.observacao = observacao;
    }

    public ControleDiarioDto() {
        super();
    }
}
