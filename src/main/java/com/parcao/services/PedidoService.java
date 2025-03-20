package com.parcao.services;

import com.parcao.model.dto.PedidoDto;
import com.parcao.model.entity.Pedido;

public interface PedidoService {

    Pedido save(PedidoDto pedidoDto);
}
