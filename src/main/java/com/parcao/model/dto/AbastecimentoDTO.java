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
public class AbastecimentoDTO {
    private Long id;
    private Long idFilial;
    private Long idUser;
    private Set<AbastecimentoItemDTO> produtos;
    private Set<AbastecimentoItemDTO> products;
}
