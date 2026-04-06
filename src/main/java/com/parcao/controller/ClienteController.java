package com.parcao.controller;

import com.parcao.dto.ClienteDTO;
import com.parcao.model.Cliente;
import com.parcao.enums.MensagemEnum;
import com.parcao.service.IClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClienteController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Object> createCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para criar cliente", correlationId);

        clienteService.createCliente(clienteDTO);

        LOGGER.info("[correlationId={}] Cliente criado com sucesso", correlationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(MensagemEnum.SUCESSO.getMensagem());
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public List<Cliente> getAllClientes() {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Listando todos os clientes", correlationId);
        return clienteService.findAll();
    }

    @GetMapping("/listPositiveBalance")
    @PreAuthorize("isAuthenticated()")
    public List<Cliente> getAllClientesPositiveBalance() {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Listando clientes com saldo positivo", correlationId);
        return clienteService.findClienteBySaldoCredito();
    }

    @GetMapping("/getClientPositiveBalance/{id}/{valorCompra}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> getClientPositiveBalance(@PathVariable(value = "id") Long id,
                                                           @PathVariable(value = "valorCompra") BigDecimal valorCompra) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Verificando saldo positivo do cliente: {}", correlationId, id);

        clienteService.verificarSaldoCredito(id, valorCompra);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Cliente> getCliente(@PathVariable(value = "id") Long id) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Buscando cliente: {}", correlationId, id);

        Cliente cliente = clienteService.getClienteById(id);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteCliente(@PathVariable(value = "id") Long id) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para excluir cliente: {}", correlationId, id);

        clienteService.deleteCliente(id);

        LOGGER.info("[correlationId={}] Cliente excluído com sucesso", correlationId);
        return ResponseEntity.ok(MensagemEnum.EXCLUIDO_COM_SUCESSO.getMensagem());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Cliente> updateCliente(@PathVariable(value = "id") Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para atualizar cliente: {}", correlationId, id);

        Cliente cliente = clienteService.updateClienteById(id, clienteDTO);

        LOGGER.info("[correlationId={}] Cliente atualizado com sucesso", correlationId);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}/{valorCompra}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Object> updateSaldoCliente(@PathVariable(value = "id") Long id,
                                                     @PathVariable(value = "valorCompra") BigDecimal valorCompra) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para atualizar saldo do cliente: {}", correlationId, id);

        clienteService.updateSaldoClienteById(id, valorCompra);

        LOGGER.info("[correlationId={}] Saldo do cliente atualizado", correlationId);
        return ResponseEntity.ok(MensagemEnum.SALDO_CLIENTE_ATUALIZADO.getMensagem());
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }
}
