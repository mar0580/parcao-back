package com.parcao.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDTO {
    @NotBlank
    @Size(min = 3, max = 20)
    private String userName;

    @NotBlank
    @Size(min = 4, max = 40)
    private String oldPassword;

    @NotBlank
    @Size(min = 4, max = 40)
    private String newPassword;
}
