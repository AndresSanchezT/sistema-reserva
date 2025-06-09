package com.cibertec.reservashotel.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.cibertec.reservashotel.model.Departamento;
import com.cibertec.reservashotel.model.Habitacion;
import com.cibertec.reservashotel.model.Hotel;
import com.cibertec.reservashotel.services.DepartamentoService;
import com.cibertec.reservashotel.services.HabitacionService;
import com.cibertec.reservashotel.services.HotelService;
import com.cibertec.reservashotel.services.TipoHabitacionService;

@Controller
@RequestMapping("/admin")
public class HotelMantenimientoController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private DepartamentoService departamentoService;
    @Autowired
    private TipoHabitacionService tipoHabitacionService;
    @Autowired
    private HabitacionService habitacionService;

    @GetMapping("/seleccionar_departamento")
    public String seleccionarDepartamento(Model model) {
        model.addAttribute("departamentos", departamentoService.listar());
        return "admin/hotel/seleccionar_departamento";
    }

    @GetMapping("/hoteles")
    public String listaHoteles(@RequestParam("depId") Long depId, Model model) {
        Departamento departamento = departamentoService.buscarPorId(depId).orElse(null);
        model.addAttribute("departamento", departamento);
        model.addAttribute("hoteles", hotelService.listarPorDepartamentoId(depId));
        return "admin/hotel/lista";
    }

    @GetMapping("/hoteles/nuevo")
    public String nuevoHotel(Model model) {
        model.addAttribute("hotel", new Hotel());
        model.addAttribute("departamentos", departamentoService.listar());
        model.addAttribute("tiposHabitacion", tipoHabitacionService.listar());
        return "admin/hotel/formulario";
    }

    @GetMapping("/hoteles/editar/{id}")
    public String editarHotel(@PathVariable Long id, Model model) {
        Hotel hotel = hotelService.buscarPorId(id).orElse(null);
        model.addAttribute("hotel", hotel);
        model.addAttribute("departamentos", departamentoService.listar());
        model.addAttribute("tiposHabitacion", tipoHabitacionService.listar());
        return "admin/hotel/formulario";
    }

    @PostMapping("/hoteles")
    public String guardarHotel(@ModelAttribute Hotel hotel,
                               @RequestParam(value = "habitacionesEliminadas", required = false) String habitacionesEliminadas) {
        // 1. Eliminar habitaciones marcadas
        if (habitacionesEliminadas != null && !habitacionesEliminadas.trim().isEmpty()) {
            String[] ids = habitacionesEliminadas.split(",");
            for (String idStr : ids) {
                try {
                    Long id = Long.parseLong(idStr);
                    habitacionService.eliminarPorId(id); // usa tu servicio para eliminar
                } catch (NumberFormatException e) {
                    // Opcional: log o ignorar
                }
            }
        }

        // 2. Asignar hotel a sus habitaciones (para nuevas o editadas)
        if (hotel.getHabitaciones() != null) {
            for (Habitacion hab : hotel.getHabitaciones()) {
                hab.setHotel(hotel);
            }
        }

        // 3. Asignar departamento
        Long depId = hotel.getDepartamento().getId();
        hotel.setDepartamento(departamentoService.buscarPorId(depId).orElse(null));

        // 4. Guardar hotel y habitaciones
        hotelService.guardar(hotel);

        return "redirect:/admin/hoteles?depId=" + depId;
    }


    @PostMapping("/hoteles/eliminar/{id}")
    public String eliminarHotel(@PathVariable Long id, @RequestParam("depId") Long depId) {
        hotelService.eliminar(id);
        return "redirect:/admin/hoteles?depId=" + depId;
    }
}

