package com.parcao.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parcao.model.entity.Usuario;
import com.parcao.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    Usuario user = userRepository.findByUserName(userName)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário de nome " + userName + " não encontrado"));

    return UserDetailsImpl.build(user);
  }

}
