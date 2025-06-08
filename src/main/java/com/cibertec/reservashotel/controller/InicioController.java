package com.cibertec.reservashotel.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cibertec.reservashotel.model.Departamento;
import com.cibertec.reservashotel.model.Hotel;
import com.cibertec.reservashotel.repository.DepartamentoRepository;
import com.cibertec.reservashotel.repository.HotelRepository;

@Controller()
public class InicioController {
	
	private final DepartamentoRepository departamentoRepository;
	private final HotelRepository hotelRepository;
	
	public InicioController(DepartamentoRepository departamentoRepository,
			HotelRepository hotelRepository) {
		this.hotelRepository = hotelRepository;
		this.departamentoRepository = departamentoRepository;
	}

    @GetMapping("/")
    public String mostrarInicio() {
        return "index"; // 
    }
    
    @GetMapping("/departamentos")
    public String mostrarDepartamentos(Model model) {
    	model.addAttribute("departamentos", departamentoRepository.findAll());
        return "departamentos";
    }
    
    @GetMapping("/departamento/{id}/hoteles")
    public String verHotelesPorDepartamento(@PathVariable Long id, Model model) {
    	Departamento departamento = departamentoRepository.findById(id).orElse(null);
    	Long departamentoId = departamento.getId();
        List<Hotel> hoteles = hotelRepository.findByDepartamentoId(departamentoId);

        model.addAttribute("departamento", departamento);
        model.addAttribute("hoteles", hoteles);
        return "hoteles";
    }
    
    @GetMapping("/departamento/{idDepartamento}/hoteles/{idHotel}/reservar")
    public String reservarHotel(
            @PathVariable Long idDepartamento,
            @PathVariable Long idHotel,
            Model model) {

        Hotel hotel = hotelRepository.findById(idHotel).orElse(null);
        
        if (hotel == null || !hotel.getDepartamento().getId().equals(idDepartamento)) {
            return "redirect:/departamento/" + idDepartamento + "/hoteles";
        }

        model.addAttribute("hotel", hotel);
        model.addAttribute("departamento", hotel.getDepartamento());
        model.addAttribute("habitaciones", hotel.getHabitaciones());

        return "reserva-form";
    }
    

}