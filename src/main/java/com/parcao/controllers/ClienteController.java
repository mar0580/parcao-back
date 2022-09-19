package com.parcao.controllers;

import com.parcao.dto.ClienteDto;
import com.parcao.payload.response.UserInfoResponse;
import com.parcao.security.services.ClienteService;
import org.springframework.http.HttpHeaders;
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

       if(clienteService.existsByNomeCliente(clienteDto.getNomeCliente())){
           return ResponseEntity.status(HttpStatus.CONFLICT).body("CLIENTE_JA_EXISTE");
       }

        return ResponseEntity.status(HttpStatus.OK).body(clienteService.save(clienteDto));
    }
}
