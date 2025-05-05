package com.parcao.controllers;

import com.parcao.model.entity.Usuario;
import com.parcao.services.IUsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public List<Usuario> getAllUsers(){
        return usuarioService.getAllUsers();
    }

    /*
    @DeleteMapping("/deleteuser/{idUsuario}")
	public ResponseEntity<?> deleteUser(@PathVariable (value = "idUsuario") Long idUsuario) {
		if (!userRepository.existsById(idUsuario)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("USUARIO_NAO_EXISTE"));
		}
		userRepository.deleteById(idUsuario);
		return ResponseEntity.ok(new MessageResponse("SUCESSO"));
	}*/
}
