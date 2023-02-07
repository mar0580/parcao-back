package com.parcao.services;

import com.parcao.models.Abastecimento;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface VendaService {

    Object selectSomatorioVendaProduto(Long idFilial, Long idProduto, Timestamp dataInicial, Timestamp dataFinal);
}
