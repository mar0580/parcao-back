package com.parcao.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.parcao.security.jwt.JwtAuthFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  public WebSecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
    .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(auth -> auth
                    .antMatchers("/api/auth/**").permitAll()
                    .antMatchers("/api/usuario/**").permitAll()
                    .antMatchers("/api/cliente/**").permitAll()
                    .antMatchers("/api/filial/**").permitAll()
                    .antMatchers(HttpMethod.POST,"/api/produto/**").hasRole("ADMIN")
                    .antMatchers("/api/taxavenda/**").permitAll()
                    .antMatchers("/api/pedido/**").permitAll()
                    .antMatchers("/api/abastecimento/**").permitAll()
                    .antMatchers("/api/test/**").permitAll()
                    .antMatchers("/api/fechamentocaixa/**").permitAll()
                    .antMatchers("/api/venda/**").permitAll()
                    .antMatchers("/api/email/**").permitAll()
                    .antMatchers("/api/estatistica/**").permitAll()
                    .antMatchers("/actuator/**").permitAll()
                    .antMatchers("/api/kafka/**").permitAll()
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
