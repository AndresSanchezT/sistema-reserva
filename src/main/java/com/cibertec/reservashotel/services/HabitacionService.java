package com.cibertec.reservashotel.services;

import java.time.LocalDate;
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

	public int obtenerCantidadDisponible(Long hotelId, LocalDate inicio, LocalDate fin) {
		// Aquí va tu lógica real, por ejemplo:
		// - Total habitaciones del hotel
		// - Menos las reservadas en ese rango
		return habitacionRepository.contarDisponibles(hotelId, inicio, fin);
	}
	
	public boolean estaDisponible(Habitacion habitacion, LocalDate inicio, LocalDate fin) {
	    Long idHabitacion = habitacion.getId();

	    // Obtener la cantidad de reservas en conflicto
	    int conflictos = habitacionRepository
	        .contarReservasPorHabitacionYFechas(idHabitacion, inicio, fin);

	    // Si hay 0 conflictos, entonces está disponible
	    return conflictos == 0;
	}
}
