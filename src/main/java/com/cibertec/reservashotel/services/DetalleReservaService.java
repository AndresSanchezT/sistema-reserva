package com.cibertec.reservashotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.reservashotel.model.DetalleReserva;
import com.cibertec.reservashotel.repository.DetalleReservaRepository;

@Service
public class DetalleReservaService {

	@Autowired
	private DetalleReservaRepository detalleReservaRepository;
	
	public DetalleReserva guardar(DetalleReserva detalleReserva) {
		return detalleReservaRepository.save(detalleReserva);
	}
}
