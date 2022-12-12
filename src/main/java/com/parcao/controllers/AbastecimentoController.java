package com.parcao.controllers;

import com.parcao.dto.AbastecimentoDto;
import com.parcao.dto.AbastecimentoItemDto;
import com.parcao.models.Abastecimento;
import com.parcao.models.AbastecimentoItem;
import com.parcao.models.Produto;
import com.parcao.security.services.AbastecimentoService;
import com.parcao.security.services.PedidoService;
import com.parcao.security.services.ProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

        for (AbastecimentoItem produtosParaEstoque : abastecimento.getProdutos()){
            Optional<Produto> produtoOptional = produtoService.findById(produtosParaEstoque.getId());
            //verifica se a quantidade solicitada pela filial consta em estoque geral
            if ((produtoOptional.get().getQuantidade() - produtosParaEstoque.getQuantidade() < 0) ) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PRODUTO_ESTOQUE_GERAL_INSUFICIENTE " + produtosParaEstoque.getDescricaoProduto());
            }

            //verifica se o produto a ser inserido em estoque na filial já existe.
            if (abastecimentoService.getRowCountAbastecimento(abastecimento.getIdFilial(), produtosParaEstoque.getId()).size() > 0){
                abastecimentoService.adicionaQuantidadeProdutoAbastecimento(produtosParaEstoque.getQuantidade(), abastecimento.getIdFilial(), produtosParaEstoque.getId());

                if (produtoService.updateProdutoEstoque(produtosParaEstoque.getId(), produtosParaEstoque.getQuantidade()) == 0) {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("ERRO_AO_ATUALIZAR_ESTOQUE_GERAL");
                }

                return ResponseEntity.status(HttpStatus.CREATED).body("ESTOQUE_PRODUTO_FILIAL_ATUALIZADO");
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(abastecimentoService.save(abastecimento));
    }

    @PutMapping()
    public ResponseEntity<Object> updateEstoqueFilial(@Valid @RequestBody AbastecimentoDto abastecimentoDto){
        Set<AbastecimentoItemDto> abastecimentoItemDto = abastecimentoDto.getProducts();
        //verifica se possui produtos em estoque, antes de atualizar
        Optional<Abastecimento> produtosParaAtualizarEstoqueOptional = abastecimentoService.findAbastecimentoByIdFilial(abastecimentoDto.getIdFilial());
        if (!produtosParaAtualizarEstoqueOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FILIAL_NAO_POSSUI_PRODUTOS");
        }

        for (AbastecimentoItemDto produtosParaAtualizarEstoque : abastecimentoItemDto){
            abastecimentoService.updateAbastecimento(produtosParaAtualizarEstoque.getQuantidade(), abastecimentoDto.getIdFilial(), produtosParaAtualizarEstoque.getId());
        }
        return  ResponseEntity.status(HttpStatus.OK).body("ESTOQUE_FILIAL_ATUALIZADO");
    }
}
