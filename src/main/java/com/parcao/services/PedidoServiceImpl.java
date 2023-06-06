package com.parcao.services;

import com.parcao.models.Pedido;
import com.parcao.dao.PedidoRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoServiceImpl implements PedidoService{
    final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido save(Pedido pedido) { return pedidoRepository.save(pedido); }
}
