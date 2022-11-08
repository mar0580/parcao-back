package com.parcao.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String descricaoProduto;

    @NotNull
    private int quantidade;

    @DecimalMin("0.01")
    private BigDecimal valorUnitario;

    @DecimalMin("0.01")
    private BigDecimal valorCustoUnitario;

    @CreationTimestamp
    private LocalDateTime dateCriacao;

    @UpdateTimestamp
    private LocalDateTime dateAtualizacao;
}
