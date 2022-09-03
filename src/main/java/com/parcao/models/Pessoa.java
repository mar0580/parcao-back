package com.parcao.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "pessoa", uniqueConstraints = { @UniqueConstraint(columnNames = "telefone")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 10, max = 50, message = "Deve conter 11 digitos")
    private String nomePessoa;

    @NotBlank
    @Size(min = 11, max = 11, message = "Deve conter 11 digitos")
    private String telefone;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ETipoPessoa tpPessoa;
}
