package com.parcao;

import com.parcao.models.Pedido;
import com.parcao.models.Produto;
import com.parcao.repository.ProdutoRepository;
import com.parcao.security.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ParcaoApplication {

	@Autowired
	private ProdutoService produtoRepository;

	public static void main(String[] args) {

		SpringApplication.run(ParcaoApplication.class, args);
	}
// https://www.bezkoder.com/spring-boot-login-example-mysql/
}
