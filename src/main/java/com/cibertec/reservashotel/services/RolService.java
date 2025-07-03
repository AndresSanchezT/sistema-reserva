package com.cibertec.reservashotel.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.reservashotel.model.Rol;
import com.cibertec.reservashotel.repository.RolRepository;

@Service
public class RolService {
    
    @Autowired
    private RolRepository rolRepository;
    
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }
    
    public Optional<Rol> buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
    
    public Optional<Rol> buscarPorId(Long id) {
        return rolRepository.findById(id);
    }
}