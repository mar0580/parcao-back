package com.parcao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parcao.model.entity.Usuario;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {

  Optional<Usuario> findByUserName(String userName);

  Boolean existsByUserName(String userName);

  Boolean existsByEmail(String email);
}
