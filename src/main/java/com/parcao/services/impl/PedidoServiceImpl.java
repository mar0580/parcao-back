package com.parcao.services.impl;

import com.parcao.model.dto.PedidoDTO;
import com.parcao.model.dto.PedidoItemDTO;
import com.parcao.model.entity.Pedido;
import com.parcao.model.entity.PedidoItem;
import com.parcao.repository.PedidoRepository;
import com.parcao.services.IPedidoService;
import com.parcao.services.IProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PedidoServiceImpl implements IPedidoService {
    final PedidoRepository pedidoRepository;
    final IProdutoService produtoService;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, IProdutoService produtoService) {
        this.pedidoRepository = pedidoRepository;
        this.produtoService = produtoService;
    }

    @Override
    public Pedido save(PedidoDTO pedidoDto) {
        Set<PedidoItemDTO> produtoItemDto = pedidoDto.getProdutos();
        Set<PedidoItem> produtos = new HashSet<>();

        produtoItemDto.forEach(produto -> produtos.add(produtoService.existsById(produto.getId()) ? new PedidoItem(produto, produtoService) : null));

        Pedido pedido = new Pedido();
        BeanUtils.copyProperties(pedidoDto, pedido);
        pedido.setProdutos(produtos);
        return pedidoRepository.save(pedido);
    }
}
