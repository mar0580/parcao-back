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
@Table(name = "cliente", uniqueConstraints = { @UniqueConstraint(columnNames = "telefone")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 10, max = 50    )
    private String nomeCliente;

    @NotBlank
    @Size(min = 11, max = 11, message = "Deve conter 11 digitos")
    private String telefone;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
}