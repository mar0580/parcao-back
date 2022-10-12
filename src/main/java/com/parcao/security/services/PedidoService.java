package com.parcao.security.services;

import com.parcao.models.Cliente;
import com.parcao.models.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoService {

    Pedido save(Pedido pedido);
}
