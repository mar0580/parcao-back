package com.parcao.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;

    @Size(min = 3, max = 20)
    private String userName;

    @Size(max = 50)
    private String nomeCompleto;

    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @Size(min = 6, max = 40)
    private String password;

    private Set<String> filial;
}
