package com.parcao.security.services;

import com.parcao.models.Cliente;
import com.parcao.models.Pedido;
import com.parcao.repository.ClienteRepository;
import com.parcao.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService{

    final PedidoRepository pedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Pedido save(Pedido pedido) { return pedidoRepository.save(pedido); }
}
