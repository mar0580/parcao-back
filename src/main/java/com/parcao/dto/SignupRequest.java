package com.parcao.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
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

    private Long idFilial;
}
