package com.parcao.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "produto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, max = 15, message = "Deve conter entre 5-15 digitos")
    private String descricaoProduto;

    @NotNull
    private int qtdEstoque;

    @NotNull
    private BigDecimal vlrUnitario;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAtualizacao;
}
