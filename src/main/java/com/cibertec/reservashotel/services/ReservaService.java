package com.cibertec.reservashotel.services;

import java.util.List;
import java.util.Optional;

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
	
	public List<Reserva> listar() {
		return reservaRepository.findAll();
	}
	
	public Optional<Reserva> buscarPorId(Long id) {
		return reservaRepository.findById(id);
	}
	
	public void eliminar(Long id) {
		reservaRepository.deleteById(id);
	}
}
