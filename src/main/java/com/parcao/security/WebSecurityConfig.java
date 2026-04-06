package com.parcao.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.parcao.security.jwt.AuthEntryPointJwt;
import com.parcao.security.jwt.JwtAuthFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

  private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

  private final JwtAuthFilter jwtAuthFilter;
  private final AuthEntryPointJwt unauthorizedHandler;

  public WebSecurityConfig(JwtAuthFilter jwtAuthFilter, AuthEntryPointJwt unauthorizedHandler) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.unauthorizedHandler = unauthorizedHandler;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    logger.info("Configurando SecurityFilterChain com autenticação JWT");

    return http
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests(auth -> auth
                    // Endpoints públicos - autenticação
                    .antMatchers("/api/auth/**").permitAll()
                    .antMatchers("/api/test/**").permitAll()
                    .antMatchers("/actuator/**").permitAll()

                    // Endpoints de usuário - apenas ADMIN pode criar/atualizar/deletar
                    .antMatchers(HttpMethod.GET, "/api/usuario/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/usuario/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/api/usuario/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/api/usuario/**").hasRole("ADMIN")

                    // Endpoints de cliente - autenticado pode ler, ADMIN/MODERATOR pode modificar
                    .antMatchers(HttpMethod.GET, "/api/cliente/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/cliente/**").hasAnyRole("ADMIN", "MODERATOR")
                    .antMatchers(HttpMethod.PUT, "/api/cliente/**").hasAnyRole("ADMIN", "MODERATOR")
                    .antMatchers(HttpMethod.DELETE, "/api/cliente/**").hasRole("ADMIN")

                    // Endpoints de filial - autenticado pode ler, ADMIN pode modificar
                    .antMatchers(HttpMethod.GET, "/api/filial/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/filial/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/api/filial/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/api/filial/**").hasRole("ADMIN")

                    // Endpoints de produto - autenticado pode ler, ADMIN pode modificar
                    .antMatchers(HttpMethod.GET, "/api/produto/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/produto/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/api/produto/**").hasAnyRole("ADMIN", "MODERATOR")
                    .antMatchers(HttpMethod.DELETE, "/api/produto/**").hasRole("ADMIN")

                    // Endpoints de taxa de venda - autenticado pode ler, ADMIN pode modificar
                    .antMatchers(HttpMethod.GET, "/api/taxavenda/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/taxavenda/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.PUT, "/api/taxavenda/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/api/taxavenda/**").hasRole("ADMIN")

                    // Endpoints de pedido - autenticado pode criar e ler
                    .antMatchers("/api/pedido/**").authenticated()

                    // Endpoints de abastecimento - autenticado pode usar
                    .antMatchers("/api/abastecimento/**").authenticated()

                    // Endpoints de fechamento de caixa - autenticado pode usar
                    .antMatchers("/api/fechamentocaixa/**").authenticated()

                    // Endpoints de venda - autenticado pode usar
                    .antMatchers("/api/venda/**").authenticated()

                    // Endpoints de email - ADMIN/MODERATOR
                    .antMatchers("/api/email/**").hasAnyRole("ADMIN", "MODERATOR")

                    // Endpoints de estatística - autenticado pode ler
                    .antMatchers("/api/estatistica/**").authenticated()

                    // Kafka - apenas ADMIN
                    .antMatchers("/api/kafka/**").hasRole("ADMIN")

                    // Qualquer outra requisição precisa estar autenticado
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:3000"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Correlation-ID"));
    configuration.setExposedHeaders(List.of("Authorization"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
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
