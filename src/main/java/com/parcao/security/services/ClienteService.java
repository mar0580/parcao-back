package com.parcao.security.services;

import com.parcao.dto.ClienteDto;

public interface ClienteService {

    public boolean existsByNomeCliente(String nomeCliente);

    public Object save(ClienteDto clienteDto);
}
