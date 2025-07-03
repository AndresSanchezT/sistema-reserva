package com.cibertec.reservashotel.controller.admin;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cibertec.reservashotel.services.ReporteService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/reportes")
public class ReporteController {

	@Autowired
	private ReporteService reporteService;

	@GetMapping("/reservas")
	public void generarReporteReservasPorFecha(
			@RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
			@RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
			HttpServletResponse response) {
		try {
			reporteService.exportarReporteReservas(inicio, fin, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/historial")
	public void generarHistorialDeReservas(	HttpServletResponse response) {
		try {
			reporteService.exportarHistorialReservas(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
}
