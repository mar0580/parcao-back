package com.parcao.controllers;

import com.parcao.dto.AbastecimentoDto;
import com.parcao.dto.AbastecimentoItemDto;
import com.parcao.dto.PedidoItemDto;
import com.parcao.models.Abastecimento;
import com.parcao.models.AbastecimentoItem;
import com.parcao.models.Filial;
import com.parcao.security.services.AbastecimentoService;
import com.parcao.security.services.PedidoService;
import com.parcao.security.services.ProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/abastecimento")
public class AbastecimentoController {
    final AbastecimentoService abastecimentoService;
    final ProdutoService produtoService;
    final PedidoService pedidoService;

    public AbastecimentoController(AbastecimentoService abastecimentoService, ProdutoService produtoService, PedidoService pedidoService) {
        this.pedidoService = pedidoService;
        this.abastecimentoService = abastecimentoService;
        this.produtoService = produtoService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAbastecimento(@Valid @RequestBody AbastecimentoDto abastecimentoDto) {
        Set<AbastecimentoItemDto> produtoItemDto = abastecimentoDto.getProdutos();
        Set<AbastecimentoItem> produtos = new HashSet<>();

        produtoItemDto.forEach(produto -> produtos.add(
                produtoService.existsById(produto.getId()) ? new AbastecimentoItem(produto) : null));

        Abastecimento abastecimento = new Abastecimento();
        BeanUtils.copyProperties(abastecimentoDto, abastecimento);
        abastecimento.setProdutos(produtos);

        for (AbastecimentoItem productsExists : abastecimento.getProdutos()){

            if (abastecimentoService.getRowCountAbastecimento(abastecimento.getIdFilial(), productsExists.getId()).size() >  0){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("PRODUTO_JA_EXISTE_EM_ESTOQUE " + productsExists.getDescricaoProduto());
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(abastecimentoService.save(abastecimento));
    }

    @PutMapping()
    public ResponseEntity<Object> updateEstoqueFilial(@Valid @RequestBody AbastecimentoDto abastecimentoDto){
        Set<AbastecimentoItemDto> abastecimentoItemDto = abastecimentoDto.getProducts();
        Integer result = null;
        Optional<Abastecimento> produtosParaAtualizarEstoqueOptional = abastecimentoService.findAbastecimentoByIdFilial(abastecimentoDto.getIdFilial());
        if (!produtosParaAtualizarEstoqueOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FILIAL_NAO_POSSUI_PRODUTOS");
        }

        for (AbastecimentoItemDto produtosParaAtualizarEstoque : abastecimentoItemDto){
            if (abastecimentoService.updateAbastecimento(produtosParaAtualizarEstoque.getQuantidade(), abastecimentoDto.getIdFilial(), produtosParaAtualizarEstoque.getId()) != 0){
                ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("ERRO_ATUALIZAR_ESTOQUE_FILIAL");
            }
        }
        return  ResponseEntity.status(HttpStatus.OK).body("ESTOQUE_FILIAL_ATUALIZADO");
    }
}
