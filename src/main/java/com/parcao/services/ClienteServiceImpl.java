package com.parcao.services;

import com.parcao.model.dto.ClienteDto;
import com.parcao.model.entity.Cliente;
import com.parcao.repository.ClienteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService{

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public boolean existsByTelefone(String telefone, ClienteDto clienteDto) {
        if (clienteRepository.existsByTelefone(telefone)) {
            return false;
        } else {
            Cliente cliente = new Cliente();
            BeanUtils.copyProperties(clienteDto, cliente);
            clienteRepository.save(cliente);
            return true;
        }
    }

    @Override
    public List<Cliente> findAll() {
        return clienteRepository.findAll(Sort.by(Sort.Direction.ASC, "nomeCliente"));
    }

    @Override
    public List<Cliente> findClienteBySaldoCredito() {
        return clienteRepository.findBySaldoCreditoGreaterThan((new BigDecimal(0)));
    }

    @Override
    public boolean existsById(Long id) { return clienteRepository.existsById(id); }

    @Override
    public void deleleById(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public Optional<Cliente> findById(Long id) { return clienteRepository.findById(id); }

    @Override
    public Cliente save(Cliente cliente) { return clienteRepository.save(cliente); }

    @Override
    public boolean existsByIdAndSaldoCreditoGreaterThanEqual(Long id, BigDecimal saldoCredito) {
        return clienteRepository.existsByIdAndSaldoCreditoGreaterThanEqual(id, saldoCredito);
    }

    @Override
    public Cliente updateCliente(ClienteDto clienteDto) {
        Cliente cliente = new Cliente();
        BeanUtils.copyProperties(clienteDto, cliente);
        clienteRepository.save(cliente);
        return cliente;
    }

    @Override
    public void updateSaldoCliente(Long id, BigDecimal saldoCredito) {
        clienteRepository.updateSaldoCliente(id, saldoCredito);
    }
}
