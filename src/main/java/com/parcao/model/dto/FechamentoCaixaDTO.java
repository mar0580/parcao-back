package com.parcao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FechamentoCaixaDTO {
    private Long id;
    private Long idFilial;
    private Long idUser;
    private String observacao;
    private Set<FechamentoCaixaItemDTO> produtos;
}
