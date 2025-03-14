package com.parcao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilialDto {

    private Long id;

    @NotBlank
    private String nomeLocal;

    @NotBlank
    private String descricaoLocal;
}
