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
    @Size(min = 10, max = 50, message = "Deve conter entre 10-50 digitos")
    private String descricaoProduto;

    @NotBlank
    private int qtdEstoque;

    @NotBlank
    private BigDecimal vlrUnitario;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAtualizacao;
}
