package com.cibertec.reservashotel.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.reservashotel.model.Habitacion;
import com.cibertec.reservashotel.repository.HabitacionRepository;

@Service
public class HabitacionService {
	
	@Autowired
	private HabitacionRepository habitacionRepository;
	
	public Optional<Habitacion> buscarPorId(Long id) {
		return habitacionRepository.findById(id);
	}
	
	public void eliminarPorId(Long id) {
		habitacionRepository.deleteById(id);
	}
}
