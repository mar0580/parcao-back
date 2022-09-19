package com.parcao.controllers;

import com.parcao.dto.ClienteDto;
import com.parcao.payload.response.MessageResponse;
import com.parcao.security.services.ClienteService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCliente(@Valid @RequestBody ClienteDto clienteDto) {
       if(clienteService.existsByTelefone(clienteDto.getTelefone())){
           return ResponseEntity.status(HttpStatus.CONFLICT).body("TELEFONE_JA_CADASTRADO");
       }
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.save(clienteDto));
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAllClientes(@PageableDefault(sort = "nomeCliente", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.findAll(pageable));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable (value = "id") Long id) {
        if (!clienteService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USUARIO_NAO_EXISTE");
        }
        clienteService.deleleById(id);
        return ResponseEntity.status(HttpStatus.OK).body("SUCESSO");
    }
}
