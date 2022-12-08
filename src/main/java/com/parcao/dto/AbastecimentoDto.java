package com.parcao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbastecimentoDto {
    private Long id;
    private Long idFilial;
    private Long idUser;
    private Set<AbastecimentoItemDto> produtos;
    private Set<AbastecimentoItemDto> products;
}
