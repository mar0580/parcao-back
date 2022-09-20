package com.parcao.controllers;

import com.parcao.dto.ClienteDto;
import com.parcao.dto.FilialDto;
import com.parcao.security.services.ClienteService;
import com.parcao.security.services.FilialService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/filial")
public class FilialController {

    final FilialService filialService;

    public FilialController(FilialService filialService) {
        this.filialService = filialService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createFilial(@Valid @RequestBody FilialDto filialDto) {
       if(filialService.existsByNomeLocal(filialDto.getNomeLocal())){
           return ResponseEntity.status(HttpStatus.CONFLICT).body("FILIAL_JA_CADASTRADA");
       }
        return ResponseEntity.status(HttpStatus.OK).body(filialService.save(filialDto));
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getAllFiliais(@PageableDefault(sort = "nomeLocal", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(filialService.findAll(pageable));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFilial(@PathVariable (value = "id") Long id) {
        if (!filialService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FILIAL_NAO_EXISTE");
        }
        filialService.deleleById(id);
        return ResponseEntity.status(HttpStatus.OK).body("SUCESSO");
    }
}
