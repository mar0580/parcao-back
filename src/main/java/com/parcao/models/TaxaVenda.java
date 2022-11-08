package com.parcao.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
    private String nomeTaxa;

    @NotNull
    private BigDecimal valorTaxa;

    @NotNull
    private BigDecimal percentualTaxa;

    @CreationTimestamp
    private LocalDateTime dateCriacao;

    @UpdateTimestamp
    private LocalDateTime dateAtualizacao;
}
