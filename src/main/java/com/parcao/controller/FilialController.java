package com.parcao.controller;

import com.parcao.model.dto.FilialDTO;
import com.parcao.model.entity.Filial;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.service.IFilialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/filial")
public class FilialController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilialController.class);
    private static final String CORRELATION_ID = "correlationId";

    private final IFilialService filialService;

    public FilialController(IFilialService filialService) {
        this.filialService = filialService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Filial> createFilial(@Valid @RequestBody FilialDTO filialDTO) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para criar filial", correlationId);

        Filial filial = filialService.createFilial(filialDTO);

        LOGGER.info("[correlationId={}] Filial criada com sucesso", correlationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(filial);
    }

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public List<Filial> getAllFiliais() {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Listando todas as filiais", correlationId);
        return filialService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Filial> getFilial(@PathVariable(value = "id") Long id) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Buscando filial: {}", correlationId, id);

        Filial filial = filialService.getFilialById(id);
        return ResponseEntity.ok(filial);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteFilial(@PathVariable(value = "id") Long id) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para excluir filial: {}", correlationId, id);

        filialService.deleteFilial(id);

        LOGGER.info("[correlationId={}] Filial excluída com sucesso", correlationId);
        return ResponseEntity.ok(MensagemEnum.SUCESSO.getMensagem());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Filial> updateFilial(@PathVariable(value = "id") Long id, @Valid @RequestBody FilialDTO filialDTO) {
        String correlationId = currentCorrelationId();
        LOGGER.info("[correlationId={}] Recebendo requisição para atualizar filial: {}", correlationId, id);

        Filial filial = filialService.updateFilial(id, filialDTO);

        LOGGER.info("[correlationId={}] Filial atualizada com sucesso", correlationId);
        return ResponseEntity.ok(filial);
    }

    private String currentCorrelationId() {
        return MDC.get(CORRELATION_ID);
    }
}
