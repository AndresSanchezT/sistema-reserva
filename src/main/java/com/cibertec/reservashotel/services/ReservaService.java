package com.cibertec.reservashotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.reservashotel.model.Reserva;
import com.cibertec.reservashotel.repository.ReservaRepository;

import jakarta.transaction.Transactional;

@Service
public class ReservaService {

	@Autowired
	private ReservaRepository reservaRepository;
	
	@Transactional
	public Reserva guardar(Reserva reserva) {
		return reservaRepository.save(reserva);
	}
}
