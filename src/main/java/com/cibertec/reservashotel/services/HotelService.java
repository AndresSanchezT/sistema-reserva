package com.cibertec.reservashotel.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.reservashotel.model.Hotel;
import com.cibertec.reservashotel.repository.HotelRepository;

@Service
public class HotelService {

	  @Autowired
	   private HotelRepository hotelRepository;
	  
	  public List<Hotel> listarPorDepartamentoId(Long departamentoId) {
		  return hotelRepository.findByDepartamentoId(departamentoId);
	  }
	  
	  public Hotel guardar(Hotel hotel) {
		  return hotelRepository.save(hotel);
	  }
	  
	  public Optional<Hotel> buscarPorId(Long id) {
		  return hotelRepository.findById(id);
	  }
	  
	  public void eliminar(Long id) {
		  hotelRepository.deleteById(id);
	  }
	  
}
