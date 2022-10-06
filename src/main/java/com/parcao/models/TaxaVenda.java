package com.parcao.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "taxaVenda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaxaVenda implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 1, message = "Nome da taxa nao pode estar nula")
    private String nomeTaxa;

    @NotNull
    private BigDecimal valorTaxa;

    @NotNull
    private BigDecimal percentualTaxa;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAtualizacao;
}
