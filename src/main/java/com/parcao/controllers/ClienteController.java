package com.parcao.controllers;

import com.parcao.model.dto.ClienteDTO;
import com.parcao.model.entity.Cliente;
import com.parcao.services.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @PostMapping("/create")
    public ResponseEntity<?> createCliente(@Valid @RequestBody ClienteDTO ClienteDTO) {
        if(!clienteService.existsByTelefone(ClienteDTO.getTelefone(), ClienteDTO)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("TELEFONE_JA_CADASTRADO");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body("SUCESSO");
        }
    }

    @GetMapping("/list")
    public List<Cliente> getAllClientes(){
        List<Cliente> cliente = new ArrayList<Cliente>();
        clienteService.findAll().forEach(cliente1 -> cliente.add(cliente1));
        return cliente;
    }

    @GetMapping("/listPositiveBalance")
    public List<Cliente> getAllClientesPositiveBalance(){
        return clienteService.findClienteBySaldoCredito();
    }

    @GetMapping("/getClientPositiveBalance/{id}/{valorCompra}")
    public ResponseEntity<Object> getClientPositiveBalance(@PathVariable(value = "id") Long id,
                                                           @PathVariable(value = "valorCompra")BigDecimal saldoCredito){
        if (!clienteService.existsByIdAndSaldoCreditoGreaterThanEqual(id, saldoCredito)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SALDO_INSUFICIENTE");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("OK");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCliente(@PathVariable(value = "id") Long id){
        Optional<Cliente> clienteOptional = clienteService.findById(id);
        return clienteOptional.<ResponseEntity<Object>>map(cliente -> ResponseEntity.status(HttpStatus.OK)
                .body(cliente)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("CLIENTE_NAO_ENCONTRADO"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCliente(@PathVariable (value = "id") Long id) {
        if (!clienteService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CLIENTE_NAO_EXISTE");
        }
        clienteService.deleleById(id);
        return ResponseEntity.status(HttpStatus.OK).body("EXCLUIDO_COM_SUCESSO");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCliente(@PathVariable(value = "id") Long id, @Valid @RequestBody ClienteDTO ClienteDTO) {
        Optional<Cliente> clienteOptional = clienteService.findById(id);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CLIENTE_NAO_EXISTE");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(clienteService.updateCliente(ClienteDTO));
        }
    }

    @PutMapping("/{id}/{valorCompra}")
    public ResponseEntity<Object> updateSaldoCliente(@PathVariable(value = "id") Long id,
                                                     @PathVariable(value = "valorCompra")BigDecimal saldoCredito) {
        Optional<Cliente> clienteOptional = clienteService.findById(id);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CLIENTE_NAO_EXISTE");
        } else {
            clienteService.updateSaldoCliente(id, saldoCredito);
            return ResponseEntity.status(HttpStatus.OK).body("SALDO_CLIENTE_ATUALIZADO");
        }
    }
}
