package com.parcao;

import com.parcao.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ParcaoApplication {

	@Autowired
	private ProdutoService produtoRepository;

	public static void main(String[] args) {

		SpringApplication.run(ParcaoApplication.class, args);
	}
	//https://bushansirgur.in/spring-boot-100-tutorials/
// https://www.bezkoder.com/spring-boot-login-example-mysql/
}
