package com.parcao.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parcao.model.entity.Usuario;
import com.parcao.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
  private static final String CORRELATION_ID = "correlationId";

  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    String correlationId = MDC.get(CORRELATION_ID);
    logger.info("[correlationId={}] Carregando usuário por username: {}", correlationId, userName);

    Usuario user = userRepository.findByUserName(userName)
        .orElseThrow(() -> {
          logger.error("[correlationId={}] Usuário não encontrado: {}", correlationId, userName);
          return new UsernameNotFoundException("Usuário de nome " + userName + " não encontrado");
        });

    logger.info("[correlationId={}] Usuário encontrado: {}", correlationId, userName);
    return UserDetailsImpl.build(user);
  }

}
