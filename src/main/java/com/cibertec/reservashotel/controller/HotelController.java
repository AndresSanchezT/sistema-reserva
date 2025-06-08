package com.cibertec.reservashotel.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cibertec.reservashotel.model.Departamento;
import com.cibertec.reservashotel.model.Habitacion;
import com.cibertec.reservashotel.model.Hotel;
import com.cibertec.reservashotel.model.TipoHabitacion;
import com.cibertec.reservashotel.services.DepartamentoService;
import com.cibertec.reservashotel.services.HotelService;
import com.cibertec.reservashotel.services.TipoHabitacionService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/admin/departamento/{departamentoId}/hoteles")
public class HotelController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private DepartamentoService departamentoService;
    @Autowired
    private TipoHabitacionService tipoHabitacionService;
    

    @GetMapping
    public String listar(@PathVariable Long departamentoId, Model model) {
        Departamento departamento = departamentoService.buscarPorId(departamentoId).orElse(null);
        model.addAttribute("hoteles", hotelService.listarPorDepartamentoId(departamentoId));
        model.addAttribute("departamento", departamento);
        return "hotel/lista";
    }
    
    @GetMapping("/nuevo")
    public String nuevo(@PathVariable Long departamentoId, Model model) {
        Departamento departamento = departamentoService.buscarPorId(departamentoId).orElse(null);
        Hotel hotel = new Hotel();
        hotel.setDepartamento(departamento);
        model.addAttribute("tiposHabitacion",tipoHabitacionService.listar());
        model.addAttribute("hotel", hotel);
        model.addAttribute("departamento", departamento);
        return "hotel/formulario";
    }

    @PostMapping("/guardar")
    public String guardarHotel(@ModelAttribute Hotel hotel, HttpServletRequest request) {
        // Recoge los parámetros de las habitaciones
        String[] numeros = request.getParameterValues("habitaciones[].numero");
        String[] estados = request.getParameterValues("habitaciones[].estado");
        String[] precios = request.getParameterValues("habitaciones[].precio");
        String[] tiposId = request.getParameterValues("habitaciones[].tipoHabitacion.id");

        List<Habitacion> habitaciones = new ArrayList<>();
        if (numeros != null) {
            for (int i = 0; i < numeros.length; i++) {
                Habitacion hab = new Habitacion();
                hab.setNumero(numeros[i]);
                hab.setEstado(estados[i]);
                hab.setPrecio(Double.parseDouble(precios[i]));

                TipoHabitacion tipo = new TipoHabitacion();
                tipo.setId(Long.parseLong(tiposId[i]));
                hab.setTipoHabitacion(tipo);

                hab.setHotel(hotel); // relación bidireccional
                habitaciones.add(hab);
            }
        }

        hotel.setHabitaciones(habitaciones);

        hotelService.guardar(hotel);
        return "redirect:/hoteles/lista";
    }
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long departamentoId, @PathVariable Long id, Model model) {
        Hotel hotel = hotelService.buscarPorId(id).orElse(null);
        model.addAttribute("hotel", hotel);
        model.addAttribute("departamento", departamentoService.buscarPorId(departamentoId));
        return "hotel/formulario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long departamentoId, @PathVariable Long id) {
        hotelService.eliminar(id);
        return "redirect:/departamento/" + departamentoId + "/hoteles";
    }
}

