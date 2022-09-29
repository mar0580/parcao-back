package com.parcao.controllers;

import com.parcao.dto.ClienteDto;
import com.parcao.models.Cliente;
import com.parcao.security.services.ClienteService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDto, cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(cliente));
    }

    @GetMapping("/list")
    public List<Cliente> getAllClientes(){
        List<Cliente> cliente = new ArrayList<Cliente>();
        clienteService.findAll().forEach(cliente1 -> cliente.add(cliente1));
        return cliente;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCliente(@PathVariable(value = "id") Long id){
        Optional<Cliente> clienteOptional = clienteService.findById(id);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CLIENTE_NAO_ENCONTRADO");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCliente(@PathVariable (value = "id") Long id) {
        if (!clienteService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CLIENTE_NAO_EXISTE");
        }
        clienteService.deleleById(id);
        return ResponseEntity.status(HttpStatus.OK).body("SUCESSO");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCliente(@PathVariable(value = "id") Long id, @Valid @RequestBody ClienteDto clienteDto) {
        Optional<Cliente> clienteOptional = clienteService.findById(id);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CLIENTE_NAO_EXISTE");
        }
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDto, cliente);
        cliente.setId(clienteOptional.get().getId());
        return  ResponseEntity.status(HttpStatus.OK).body(clienteService.save(cliente));
    }
}
