package com.cibertec.reservashotel.controller.admin;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cibertec.reservashotel.services.HabitacionService;

@Controller
@RequestMapping("/api/habitaciones")
public class HabitacionConsultaController {

	@Autowired
	private HabitacionService habitacionService;

	@GetMapping("/disponibles")
	@ResponseBody // si no pones la etiqueta como @RestController, entonces explicitamente tienes que convertir las funciones 
	// con @ResponseBody
	public Map<String, Object> verificarDisponibilidad(@RequestParam Long hotelId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

		int cantidadDisponible = habitacionService.obtenerCantidadDisponible(hotelId, fechaInicio, fechaFin);

		Map<String, Object> response = new HashMap<>();
		response.put("disponible", cantidadDisponible > 0);
		response.put("cantidad", cantidadDisponible);

		return response;
	}
}
