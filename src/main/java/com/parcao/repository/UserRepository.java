package com.parcao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parcao.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserName(String userName);

  Boolean existsByUserName(String userName);

  Boolean existsByEmail(String email);
}
