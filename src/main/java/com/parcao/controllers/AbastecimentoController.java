package com.parcao.controllers;

import com.parcao.model.dto.AbastecimentoDTO;
import com.parcao.model.dto.AbastecimentoItemDTO;
import com.parcao.model.entity.Abastecimento;
import com.parcao.model.entity.AbastecimentoItem;
import com.parcao.model.enums.EEmailDetails;
import com.parcao.model.entity.Produto;
import com.parcao.services.IAbastecimentoService;
import com.parcao.services.IEmailService;
import com.parcao.services.IPedidoService;
import com.parcao.services.IProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/abastecimento")
public class AbastecimentoController {
    final IAbastecimentoService abastecimentoService;
    final IProdutoService produtoService;
    final IPedidoService pedidoService;

    final IEmailService emailService;

    public AbastecimentoController(IAbastecimentoService abastecimentoService, IProdutoService produtoService, IPedidoService pedidoService, IEmailService emailService) {
        this.pedidoService = pedidoService;
        this.abastecimentoService = abastecimentoService;
        this.produtoService = produtoService;
        this.emailService = emailService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAbastecimento(@Valid @RequestBody AbastecimentoDTO abastecimentoDto) {
        Set<AbastecimentoItemDTO> produtoItemDto = abastecimentoDto.getProdutos();
        Set<AbastecimentoItem> produtos = new HashSet<>();

        produtoItemDto.forEach(produto -> produtos.add(
                produtoService.existsById(produto.getId()) ? new AbastecimentoItem(produto) : null));

        Abastecimento abastecimento = new Abastecimento();
        BeanUtils.copyProperties(abastecimentoDto, abastecimento);
        abastecimento.setProdutos(produtos);

        for (AbastecimentoItem produtosParaEstoque : abastecimento.getProdutos()){
            Produto produtoOptional = produtoService.findById(produtosParaEstoque.getId());
            //verifica se a quantidade solicitada pela filial consta em estoque geral
            if ((produtoOptional.getQuantidade() - produtosParaEstoque.getQuantidade() < 0) ) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PRODUTO_ESTOQUE_GERAL_INSUFICIENTE " + produtosParaEstoque.getDescricaoProduto());
            }

            //verifica se o produto a ser inserido em estoque na filial já existe.
            if (abastecimentoService.getRowCountAbastecimento(abastecimento.getIdFilial(), produtosParaEstoque.getId()).size() > 0){
                abastecimentoService.adicionaQuantidadeProdutoAbastecimento(produtosParaEstoque.getQuantidade(), abastecimento.getIdFilial(), produtosParaEstoque.getId());

                produtoService.updateProdutoEstoque(produtosParaEstoque.getId(), (produtoOptional.getQuantidade() - produtosParaEstoque.getQuantidade()));

                return ResponseEntity.status(HttpStatus.CREATED).body("ESTOQUE_PRODUTO_FILIAL_ATUALIZADO");
            }
            produtoService.updateProdutoEstoque(produtosParaEstoque.getId(), (produtoOptional.getQuantidade() - produtosParaEstoque.getQuantidade()));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(abastecimentoService.save(abastecimento));
    }

    @PutMapping()
    public ResponseEntity<Object> updateEstoqueFilial(@Valid @RequestBody AbastecimentoDTO abastecimentoDto){
        Set<AbastecimentoItemDTO> abastecimentoItemDto = abastecimentoDto.getProducts();
        //verifica se possui produtos em estoque, antes de atualizar
        String produtosEstoqueBaixo = "";
        for (AbastecimentoItemDTO produtosParaAtualizarEstoque : abastecimentoItemDto){
            if (abastecimentoService.getRowCountQuantidadeAbastecimento(abastecimentoDto.getIdFilial(), produtosParaAtualizarEstoque.getId(), produtosParaAtualizarEstoque.getQuantidade()).size() < 1){
                produtosEstoqueBaixo += System.lineSeparator() + "Produto: " + produtosParaAtualizarEstoque.getDescricaoProduto() + " - Quantidade: " +
                        produtosParaAtualizarEstoque.getQuantidade() + "\n";
            }
            if(!produtosEstoqueBaixo.isEmpty()){
                emailService.sendEmail("userWarn", produtosEstoqueBaixo, EEmailDetails.ESTOQUE_BAIXO.getEEmailDetails());
            }
            if (abastecimentoService.updateAbastecimento(produtosParaAtualizarEstoque.getQuantidade(), abastecimentoDto.getIdFilial(), produtosParaAtualizarEstoque.getId()) == 0) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("ERRO_AO_ATUALIZAR_ESTOQUE");
            }
        }
        return  ResponseEntity.status(HttpStatus.OK).body("ESTOQUE_FILIAL_ATUALIZADO");
    }
}
