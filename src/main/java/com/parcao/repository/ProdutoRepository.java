package com.parcao.repository;

import com.parcao.models.Filial;
import com.parcao.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
