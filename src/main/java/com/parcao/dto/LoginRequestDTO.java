package com.parcao.dto;

import javax.validation.constraints.NotBlank;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
	@NotBlank
	private String userName;

	@NotBlank
	private String password;	
}
