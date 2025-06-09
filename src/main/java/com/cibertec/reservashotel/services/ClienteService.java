package com.cibertec.reservashotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.reservashotel.model.Cliente;
import com.cibertec.reservashotel.repository.ClienteRepository;

@Service
public class ClienteService {
	@Autowired
    private ClienteRepository clienteRepository;

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
}
