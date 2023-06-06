package com.parcao.controllers;

import com.parcao.dto.ControleDiarioEstoqueDto;
import com.parcao.dto.FechamentoCaixaDto;
import com.parcao.dto.FechamentoCaixaItemDto;
import com.parcao.models.FechamentoCaixa;
import com.parcao.models.FechamentoCaixaItem;
import com.parcao.dao.FechamentoCaixaItemRepository;
import com.parcao.services.FechamentoCaixaItemService;
import com.parcao.services.FechamentoCaixaService;
import com.parcao.services.ProdutoService;
import com.parcao.utils.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/fechamentocaixa")
public class FechamentoCaixaController {

    final FechamentoCaixaService fechamentoCaixaService;
    final FechamentoCaixaItemService fechamentoCaixaItemService;
    final ProdutoService produtoService;
    @Autowired
    FechamentoCaixaItemRepository fechamentoCaixaItemRepository;

    public FechamentoCaixaController(FechamentoCaixaService fechamentoCaixaService, FechamentoCaixaItemService fechamentoCaixaItemService, ProdutoService produtoService) {
        this.fechamentoCaixaService = fechamentoCaixaService;
        this.fechamentoCaixaItemService = fechamentoCaixaItemService;
        this.produtoService = produtoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createFechamentoCaixa(@Valid @RequestBody FechamentoCaixaDto fechamentoCaixaDto) {
        Set<FechamentoCaixaItemDto> produtoItemFechamentoCaixaDto = fechamentoCaixaDto.getProdutos();
        Set<FechamentoCaixaItem> produtos = new HashSet<>();

        produtoItemFechamentoCaixaDto.forEach(produto -> produtos.add(produtoService.existsById(produto.getId()) ? new FechamentoCaixaItem(produto) : null));

        FechamentoCaixa fechamentoCaixa = new FechamentoCaixa();
        BeanUtils.copyProperties(fechamentoCaixaDto, fechamentoCaixa);
        fechamentoCaixa.setProdutos(produtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(fechamentoCaixaService.save(fechamentoCaixa));
    }

    @GetMapping("/buscaFechamentoCaixaProduto/{idFilial}/{idProduto}/{dataInicial}/{dataFinal}")
    public ResponseEntity<Object> buscaFechamentoCaixaProduto(@PathVariable(value = "idFilial") Long idFilial,
                                                              @PathVariable(value = "idProduto") Long idProduto,
                                                              @PathVariable(value = "dataInicial") @DateTimeFormat(pattern = "yyyy-MM-dd")  String dataInicial,
                                                              @PathVariable(value = "dataFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") String dataFinal) throws ParseException {
        List<Object[]> optionalFechamentoCaixaItem = null;
        if(dataInicial.compareTo(dataFinal) == 0) {
            optionalFechamentoCaixaItem = fechamentoCaixaItemService.selectFechamentoCaixaProdutoDiario(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        } else {
            optionalFechamentoCaixaItem = fechamentoCaixaItemService.selectFechamentoCaixaProdutoPeriodo(idFilial, idProduto, Util.dateToInicialTimestamp(dataInicial), Util.dateToFinalTimestamp(dataFinal));
        }
        if (optionalFechamentoCaixaItem.size() > 0) {

            List<ControleDiarioEstoqueDto> customResponseList = new ArrayList();
            for (Object[] item: optionalFechamentoCaixaItem) {
                ControleDiarioEstoqueDto c = new ControleDiarioEstoqueDto();
                BigInteger b = new BigInteger(item[0].toString());
                c.setId(b.longValue());
                c.setInicio((int) item[1]);
                c.setEntrada((int) item[2]);
                c.setPerda((int) item[3]);
                c.setQuantidadeFinal((int) item[4]);
                c.setSaida((int) item[5]);
                if(dataInicial.compareTo(dataFinal) == 0) {
                    c.setObservacao(((String) item[6]));
                } else {
                    c.setObservacao(" ");
                }
                customResponseList.add(c);
            }
            return ResponseEntity.status(HttpStatus.OK).body(customResponseList.get(0));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FECHAMENTO_CAIXA_NAO_ENCONTRADO");
    }

    //Criar método de cancelar fechamento de caixa

}
