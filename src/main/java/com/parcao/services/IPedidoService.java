package com.parcao.services;

import com.parcao.model.dto.PedidoDTO;
import com.parcao.model.entity.Pedido;

public interface IPedidoService {

    Pedido save(PedidoDTO pedidoDto);
}
