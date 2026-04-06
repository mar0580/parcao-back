package com.parcao.service.impl;

import com.parcao.exception.ResourceNotFoundException;
import com.parcao.model.dto.ClienteDTO;
import com.parcao.model.entity.Cliente;
import com.parcao.model.enums.MensagemEnum;
import com.parcao.repository.ClienteRepository;
import com.parcao.service.IClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements IClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);
    private static final String CORRELATION_ID = "correlationId";

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public boolean existsByTelefone(String telefone, ClienteDTO clienteDTO) {
        if (clienteRepository.existsByTelefone(telefone)) {
            return false;
        } else {
            Cliente cliente = new Cliente();
            BeanUtils.copyProperties(clienteDTO, cliente);
            clienteRepository.save(cliente);
            return true;
        }
    }

    @Override
    @Transactional
    public Cliente createCliente(ClienteDTO clienteDTO) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Criando novo cliente: {}", correlationId, clienteDTO.getNomeCliente());

        if (clienteRepository.existsByTelefone(clienteDTO.getTelefone())) {
            logger.warn("[correlationId={}] Telefone já cadastrado: {}", correlationId, clienteDTO.getTelefone());
            throw new IllegalStateException(MensagemEnum.TELEFONE_JA_CADASTRADO.getMensagem());
        }

        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDTO, cliente);
        Cliente saved = clienteRepository.save(cliente);

        logger.info("[correlationId={}] Cliente criado com sucesso: {}", correlationId, saved.getId());
        return saved;
    }

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll(Sort.by(Sort.Direction.ASC, "nomeCliente"));
    }

    @Override
    public List<Cliente> findClienteBySaldoCredito() {
        return clienteRepository.findBySaldoCreditoGreaterThan(BigDecimal.ZERO);
    }

    @Override
    public boolean existsById(Long id) {
        return clienteRepository.existsById(id);
    }

    @Override
    public void deleleById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCliente(Long id) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Excluindo cliente: {}", correlationId, id);

        if (!existsById(id)) {
            throw new ResourceNotFoundException(MensagemEnum.CLIENTE_NAO_EXISTE.getMensagem());
        }
        deleleById(id);
        logger.info("[correlationId={}] Cliente excluído com sucesso: {}", correlationId, id);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public Cliente getClienteById(Long id) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Buscando cliente: {}", correlationId, id);

        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MensagemEnum.CLIENTE_NAO_ENCONTRADO.getMensagem()));
    }

    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public boolean existsByIdAndSaldoCreditoGreaterThanEqual(Long id, BigDecimal saldoCredito) {
        return clienteRepository.existsByIdAndSaldoCreditoGreaterThanEqual(id, saldoCredito);
    }

    @Override
    public void verificarSaldoCredito(Long id, BigDecimal valorCompra) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Verificando saldo de crédito do cliente: {}", correlationId, id);

        if (!existsByIdAndSaldoCreditoGreaterThanEqual(id, valorCompra)) {
            throw new ResourceNotFoundException(MensagemEnum.SALDO_INSUFICIENTE.getMensagem());
        }
        logger.info("[correlationId={}] Saldo suficiente para cliente: {}", correlationId, id);
    }

    @Override
    public Cliente updateCliente(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDTO, cliente);
        clienteRepository.save(cliente);
        return cliente;
    }

    @Override
    @Transactional
    public Cliente updateClienteById(Long id, ClienteDTO clienteDTO) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Atualizando cliente: {}", correlationId, id);

        Cliente clienteExistente = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MensagemEnum.CLIENTE_NAO_EXISTE.getMensagem()));

        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDTO, cliente);
        cliente.setId(clienteExistente.getId());

        Cliente updated = save(cliente);
        logger.info("[correlationId={}] Cliente atualizado com sucesso: {}", correlationId, id);
        return updated;
    }

    @Override
    public void updateSaldoCliente(Long id, BigDecimal saldoCredito) {
        clienteRepository.updateSaldoCliente(id, saldoCredito);
    }

    @Override
    @Transactional
    public void updateSaldoClienteById(Long id, BigDecimal valorCompra) {
        String correlationId = MDC.get(CORRELATION_ID);
        logger.info("[correlationId={}] Atualizando saldo do cliente: {}", correlationId, id);

        if (!existsById(id)) {
            throw new ResourceNotFoundException(MensagemEnum.CLIENTE_NAO_EXISTE.getMensagem());
        }
        updateSaldoCliente(id, valorCompra);
        logger.info("[correlationId={}] Saldo do cliente atualizado: {}", correlationId, id);
    }
}
