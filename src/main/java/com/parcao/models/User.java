package com.parcao.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "userName"),
		@UniqueConstraint(columnNames = "email") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	public User(String userName, String nomeCompleto, String email, String password) {
		this.userName = userName;
		this.nomeCompleto = nomeCompleto;
		this.email = email;
		this.password = password;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String userName;

	@NotBlank
	@Size(max = 50)
	private String nomeCompleto;

	@NotBlank
	@Size(max = 50)
	@Email
	@Column(insertable=true, updatable=true)
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	@CreationTimestamp
	private LocalDateTime dateInsert;

	@UpdateTimestamp
	private LocalDateTime dateAtualizacao;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_filiais", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "filial_id"))
	private Set<Filial> filiais = new HashSet<>();
}
