package com.parcao.controllers;

import com.parcao.dto.FilialDto;
import com.parcao.models.Filial;
import com.parcao.services.FilialService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Filial filial = new Filial();
        BeanUtils.copyProperties(filialDto, filial);
        return ResponseEntity.status(HttpStatus.CREATED).body(filialService.save(filial));
    }

    @GetMapping("/list")
    public List<Filial> getAllFiliais(){
        List<Filial> filial = new ArrayList<Filial>();
        filialService.findAll().forEach(filial1 -> filial.add(filial1));
        return filial;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getFilial(@PathVariable (value = "id") Long id){
        if (!filialService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FILIAL_NAO_EXISTE");
        }
        return ResponseEntity.status(HttpStatus.OK).body(filialService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFilial(@PathVariable (value = "id") Long id) {
        if (!filialService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FILIAL_NAO_EXISTE");
        }
        filialService.deleleById(id);
        return ResponseEntity.status(HttpStatus.OK).body("SUCESSO");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFilial(@PathVariable(value = "id") Long id,@Valid @RequestBody FilialDto filialDto) {
        Optional<Filial> filialOptional = filialService.findById(id);
        if (!filialOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FILIAL_NAO_EXISTE");
        }
        Filial filial = new Filial();
        BeanUtils.copyProperties(filialDto, filial);
        filial.setId(filialOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(filialService.save(filial));
    }
}
