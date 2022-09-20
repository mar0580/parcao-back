package com.parcao.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "filial", uniqueConstraints = { @UniqueConstraint(columnNames = "nomeLocal")})
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
    @Size(min = 10, max = 50    )
    private String nomeLocal;

    @NotBlank
    @Size(min = 10, max = 50, message = "Deve conter entre 10-50 digitos")
    private String descricaoLocal;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime dateAtualizacao;

}
