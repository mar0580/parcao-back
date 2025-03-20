package com.parcao.services;

import com.parcao.model.dto.PedidoDto;
import com.parcao.model.dto.PedidoItemDto;
import com.parcao.model.entity.Pedido;
import com.parcao.model.entity.PedidoItem;
import com.parcao.repository.PedidoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PedidoServiceImpl implements PedidoService{
    final PedidoRepository pedidoRepository;
    final ProdutoService produtoService;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoService = produtoService;
    }

    @Override
    public Pedido save(PedidoDto pedidoDto) {
        Set<PedidoItemDto> produtoItemDto = pedidoDto.getProdutos();
        Set<PedidoItem> produtos = new HashSet<>();

        produtoItemDto.forEach(produto -> produtos.add(produtoService.existsById(produto.getId()) ? new PedidoItem(produto, produtoService) : null));

        Pedido pedido = new Pedido();
        BeanUtils.copyProperties(pedidoDto, pedido);
        pedido.setProdutos(produtos);
        return pedidoRepository.save(pedido);
    }
}
