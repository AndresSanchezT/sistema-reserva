package com.cibertec.reservashotel.controller.usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cibertec.reservashotel.model.Cliente;
import com.cibertec.reservashotel.model.Departamento;
import com.cibertec.reservashotel.model.DetalleReserva;
import com.cibertec.reservashotel.model.Habitacion;
import com.cibertec.reservashotel.model.Hotel;
import com.cibertec.reservashotel.model.Reserva;
import com.cibertec.reservashotel.services.ClienteService;
import com.cibertec.reservashotel.services.DepartamentoService;
import com.cibertec.reservashotel.services.HabitacionService;
import com.cibertec.reservashotel.services.HotelService;
import com.cibertec.reservashotel.services.ReservaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("reserva")
public class HotelReservaController {

    @Autowired
    private DepartamentoService departamentService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private HabitacionService habitacionService;
    @Autowired
    private ClienteService clienteService;
    
    @GetMapping("/departamentos")
    public String mostrarDepartamentos(@RequestParam(required = false) String nombre, Model model) {
        if (nombre != null && !nombre.isEmpty()) {
            Optional<Departamento> departamento = departamentService.buscarPorNombre(nombre);
            if (departamento.isPresent()) {
                model.addAttribute("departamentos", List.of(departamento.get()));
                model.addAttribute("mensaje", "Mostrando resultados para: " + nombre);
            } else {
                model.addAttribute("departamentos", List.of());
                model.addAttribute("mensaje", "No se encontraron resultados para: " + nombre);
            }
        } else {
            model.addAttribute("departamentos", departamentService.listar());
        }
        return "usuario/departamentos";
    }

    @GetMapping("/buscar")
    public String buscarReservasPorDni(@RequestParam("dni") String dni, Model model) {
        List<Reserva> reservas = reservaService.buscarReservasPorDniCliente(dni);
        model.addAttribute("reservas", reservas);
        return "admin/reserva/lista"; // o la ruta correcta a tu HTML actual
    }
    

    @GetMapping("/hoteles")
    public String verHotelesPorDepartamento(@RequestParam("depId") Long depId, Model model) {
        Departamento departamento = departamentService.buscarPorId(depId).orElse(null);
        List<Hotel> hoteles = hotelService.listarPorDepartamentoId(depId);

        model.addAttribute("departamento", departamento);
        model.addAttribute("hoteles", hoteles);
        return "usuario/hoteles";
    }


    @GetMapping("/hoteles/{id}/reservar")
    public String mostrarFormularioReserva(@PathVariable("id") Long hotelId, Model model) {

        Hotel hotel = hotelService.buscarPorId(hotelId).orElse(null);
        Map<Long, Double> mapaPrecios = hotel.getHabitaciones()
    		    .stream()
    		    .collect(Collectors.toMap(h -> h.getId(), h -> h.getPrecio()));

        Reserva reserva = new Reserva();
        reserva.setCliente(new Cliente());

        model.addAttribute("hotel", hotel);
        model.addAttribute("mapaPrecios", mapaPrecios);
        model.addAttribute("departamento", hotel != null ? hotel.getDepartamento() : null);
        model.addAttribute("habitaciones", hotel != null ? hotel.getHabitaciones() : Collections.emptyList());
        model.addAttribute("reserva", reserva);

        return "usuario/reserva-form";
    }

    @PostMapping("/hoteles/{id}/reservar")
    public String procesarReserva(
            @PathVariable("id") Long hotelId,
            @Valid @ModelAttribute("reserva") Reserva reserva,
            BindingResult result,
            @RequestParam(name = "habitacionesSeleccionadas", required = false) List<Long> habitacionesSeleccionadas,
            Model model) {

        Optional<Hotel> optHotel = hotelService.buscarPorId(hotelId);
        Hotel hotel = optHotel.orElse(null);

        // ⚠️ Validación de campos
        if (result.hasErrors() || hotel == null) {
            model.addAttribute("reserva", reserva); // 🔥 importante
            model.addAttribute("hotel", hotel);
            model.addAttribute("departamento", hotel != null ? hotel.getDepartamento() : null);
            model.addAttribute("habitaciones", hotel != null ? hotel.getHabitaciones() : Collections.emptyList());
            model.addAttribute("habitacionesSeleccionadas", habitacionesSeleccionadas); // por si ya había alguna
            return "usuario/reserva-form";
        }

        // ⚠️ Validación de fechas
        LocalDate hoy = LocalDate.now();
        LocalDate fechaInicio = reserva.getFechaInicio();
        LocalDate fechaFin = reserva.getFechaFin();

        if (fechaInicio == null || fechaFin == null || fechaInicio.isBefore(hoy) || fechaFin.isBefore(hoy) || fechaInicio.isAfter(fechaFin)) {
            model.addAttribute("error", "Las fechas seleccionadas no son válidas.");
            model.addAttribute("reserva", reserva); // 🔥 importante
            model.addAttribute("hotel", hotel);
            model.addAttribute("departamento", hotel.getDepartamento());
            model.addAttribute("habitaciones", hotel.getHabitaciones());
            model.addAttribute("habitacionesSeleccionadas", habitacionesSeleccionadas);
            return "usuario/reserva-form";
        }

        // ⚠️ Validación de habitaciones
        if (habitacionesSeleccionadas == null || habitacionesSeleccionadas.isEmpty()) {
            model.addAttribute("error", "Debe seleccionar al menos una habitación");
            model.addAttribute("reserva", reserva); // 🔥 importante
            model.addAttribute("hotel", hotel);
            model.addAttribute("departamento", hotel.getDepartamento());
            model.addAttribute("habitaciones", hotel.getHabitaciones());
            return "usuario/reserva-form";
        }

        // ⚠️ Validar disponibilidad
        List<String> noDisponibles = new ArrayList<>();
        for (Long habId : habitacionesSeleccionadas) {
            Habitacion hab = habitacionService.buscarPorId(habId).orElse(null);
            if (hab != null && !habitacionService.estaDisponible(hab, fechaInicio, fechaFin)) {
                noDisponibles.add(hab.getTipoHabitacion().getNombre());
            }
        }

        if (!noDisponibles.isEmpty()) {
            model.addAttribute("error", "Las siguientes habitaciones no están disponibles: " + String.join(", ", noDisponibles));
            model.addAttribute("reserva", reserva); // 🔥 importante
            model.addAttribute("hotel", hotel);
            model.addAttribute("departamento", hotel.getDepartamento());
            model.addAttribute("habitaciones", hotel.getHabitaciones());
            model.addAttribute("habitacionesSeleccionadas", habitacionesSeleccionadas);
            return "usuario/reserva-form";
        }

        // Guardar cliente y reserva
        Cliente clientePersistido = clienteService.guardar(reserva.getCliente());
        reserva.setCliente(clientePersistido);
        reserva.setHotel(hotel);
        reserva.setFechaReserva(LocalDate.now());
        reserva.setEstado("CONFIRMADA");

        List<DetalleReserva> detalles = new ArrayList<>();
        double total = 0;

        for (Long habId : habitacionesSeleccionadas) {
            Habitacion hab = habitacionService.buscarPorId(habId).orElse(null);
            if (hab != null) {
                DetalleReserva detalle = new DetalleReserva();
                detalle.setHabitacion(hab);
                detalle.setReserva(reserva);
                detalle.setPrecioNoche(hab.getPrecio());
                detalles.add(detalle);
                total += hab.getPrecio();
            }
        }

        reserva.setDetalles(detalles);
        reserva.setTotal(total);
        reserva.setId(null);
        reservaService.guardar(reserva);

        return "usuario/confirmacion";
    }
    
  
}

