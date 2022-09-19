package com.parcao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {
    @NotBlank
    private String nomeCliente;

    @NotBlank
    private String telefone;
}
