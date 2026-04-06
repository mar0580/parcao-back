package com.parcao.service;

import com.parcao.dto.PedidoDTO;
import com.parcao.model.Pedido;

public interface IPedidoService {

    Pedido save(PedidoDTO pedidoDto);
}
