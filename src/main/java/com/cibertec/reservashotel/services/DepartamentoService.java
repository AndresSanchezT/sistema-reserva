package com.cibertec.reservashotel.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.reservashotel.model.Departamento;
import com.cibertec.reservashotel.repository.DepartamentoRepository;

@Service
public class DepartamentoService {
	
	@Autowired
    private DepartamentoRepository departamentoRepository;

	public Optional<Departamento> buscarPorId(Long id) {
		return departamentoRepository.findById(id);
	}
	
	public List<Departamento> listar(){
		return departamentoRepository.findAll();
	}
	
	public Departamento guardar(Departamento departamento) {
		  return departamentoRepository.save(departamento);
	  }
	
	public void eliminar(Long depId) {
		departamentoRepository.deleteById(depId);
	}
	
    public Optional<Departamento> buscarPorNombre(String nombre) {
        return departamentoRepository.findByNombre(nombre);
    }
}
