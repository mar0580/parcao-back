package com.parcao.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = "nomeLocal")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Filial implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nomeLocal;

    @NotBlank
    private String descricaoLocal;

    @CreationTimestamp
    private LocalDateTime dateCriacao;

    @UpdateTimestamp
    private LocalDateTime dateAtualizacao;

    public Filial(Long idFilial) {
        this.id = idFilial;
    }
}
