package com.cibertec.reservashotel.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.reservashotel.model.TipoHabitacion;
import com.cibertec.reservashotel.repository.TipoHabitacionRepository;

@Service
public class TipoHabitacionService {
	
	@Autowired
	private TipoHabitacionRepository tipoHabitacionRepository;
	
	public List<TipoHabitacion> listar() {
		return tipoHabitacionRepository.findAll();
	}
}
