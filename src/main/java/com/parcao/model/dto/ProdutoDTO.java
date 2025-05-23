package com.parcao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    private Long id;

    @NotBlank
    private String descricaoProduto;

    private int quantidade;

    private BigDecimal valorUnitario;

    private BigDecimal valorCustoUnitario;
}
