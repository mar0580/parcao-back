package com.parcao.security;

import com.parcao.service.impl.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Componente de segurança para validações customizadas de acesso.
 * Utilizado em anotações @PreAuthorize para verificar se o usuário
 * autenticado é o dono do recurso.
 */
@Component("userSecurity")
public class UserSecurity {

    private static final Logger logger = LoggerFactory.getLogger(UserSecurity.class);
    private static final String CORRELATION_ID = "correlationId";

    /**
     * Verifica se o usuário autenticado é o mesmo do ID informado.
     *
     * @param userId ID do usuário a ser verificado
     * @return true se o usuário autenticado é o mesmo do ID, false caso contrário
     */
    public boolean isCurrentUser(Long userId) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Verificando se usuário autenticado é o dono do recurso: {}", correlationId, userId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.info("[correlationId={}] Usuário não autenticado", correlationId);
            return false;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) principal;
            boolean isOwner = userDetails.getId().equals(userId);
            logger.info("[correlationId={}] Verificação de propriedade: {} - resultado: {}", correlationId, userId, isOwner);
            return isOwner;
        }

        logger.info("[correlationId={}] Principal não é instância de UserDetailsImpl", correlationId);
        return false;
    }

    /**
     * Verifica se o usuário autenticado possui uma role específica.
     *
     * @param role Role a ser verificada (sem o prefixo ROLE_)
     * @return true se o usuário possui a role, false caso contrário
     */
    public boolean hasRole(String role) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Verificando se usuário possui role: {}", correlationId, role);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        boolean hasRole = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));

        logger.info("[correlationId={}] Usuário possui role {}: {}", correlationId, role, hasRole);
        return hasRole;
    }

    /**
     * Retorna o ID do usuário autenticado.
     *
     * @return ID do usuário ou null se não autenticado
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) authentication.getPrincipal()).getId();
        }

        return null;
    }

    /**
     * Retorna o username do usuário autenticado.
     *
     * @return username do usuário ou null se não autenticado
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        }

        return null;
    }
}

