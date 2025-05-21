package com.parcao.controllers;

import com.parcao.model.entity.Usuario;
import com.parcao.services.IJwtService;
import com.parcao.services.IUsuarioService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final IJwtService jwtService;

    public UsuarioController(IUsuarioService usuarioService, IJwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @GetMapping("/")
    public List<Usuario> getAllUsers(){
        return usuarioService.getAllUsers();
    }

    /*

    @PostMapping("/changepassword")
	public ResponseEntity<?> changePasswordUser(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) {
		try {
			MessageResponse response = authService.changePassword(changePasswordRequest);
			return ResponseEntity.ok(response);
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (InvalidPasswordException e) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

    @DeleteMapping("/deleteuser/{idUsuario}")
	public ResponseEntity<?> deleteUser(@PathVariable (value = "idUsuario") Long idUsuario) {
		if (!userRepository.existsById(idUsuario)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("USUARIO_NAO_EXISTE"));
		}
		userRepository.deleteById(idUsuario);
		return ResponseEntity.ok(new MessageResponse("SUCESSO"));
	}

	@PutMapping("/changedatauser")
	public ResponseEntity<?> changeDataUser(@Valid @RequestBody SignupRequestDTO signupRequest) {
		if (!userRepository.existsById(signupRequest.getId())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USUARIO_NAO_EXISTE");
		} else {
			Optional<UsuarioDTO> user = userRepository.findById(signupRequest.getId());
			UsuarioDTO userUpdate = new UsuarioDTO();
			userUpdate.setId(signupRequest.getId());
			userUpdate.setNomeCompleto(signupRequest.getNomeCompleto());
			userUpdate.setUserName(user.get().getUserName());
			userUpdate.setEmail(signupRequest.getEmail());
			userUpdate.setPassword(user.get().getPassword());
			userUpdate.setDateInsert(LocalDateTime.now());

			Set<String> strFiliais = signupRequest.getFilial();
			Set<FilialDTO> filiais = new HashSet<>();
			strFiliais.forEach(nomeLocal -> {
				FilialDTO filial = filialService.findByNomeLocal(nomeLocal).orElseThrow(() -> new RuntimeException("INEXISTENTE"));
				filiais.add(filial);
			});

			Set<String> strRoles = signupRequest.getRole();
			Set<Role> roles = new HashSet<>();

			if (strRoles == null) {
				Role userRole = roleRepository.findByName(ERole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("INEXISTENTE"));
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch (role) {
						case "admin":
							Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
									.orElseThrow(() -> new RuntimeException("INEXISTENTE"));
							roles.add(adminRole);

							break;
						case "mod":
							Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
									.orElseThrow(() -> new RuntimeException("INEXISTENTE"));
							roles.add(modRole);

							break;
						default:
							Role userRole = roleRepository.findByName(ERole.ROLE_USER)
									.orElseThrow(() -> new RuntimeException("INEXISTENTE"));
							roles.add(userRole);
					}
				});
			}

			userUpdate.setRoles(roles);
			userUpdate.setFiliais(filiais);

			userUpdate.setDateInsert(LocalDateTime.now());
			userRepository.save(userUpdate);
		}

		return ResponseEntity.ok(new MessageResponse("SUCESSO"));
	}
*/
}
