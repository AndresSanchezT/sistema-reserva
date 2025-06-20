package com.cibertec.reservashotel.controller.admin;

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
import org.springframework.web.bind.annotation.*;

import com.cibertec.reservashotel.model.Cliente;
import com.cibertec.reservashotel.model.Departamento;
import com.cibertec.reservashotel.model.DetalleReserva;
import com.cibertec.reservashotel.model.Habitacion;
import com.cibertec.reservashotel.model.Hotel;
import com.cibertec.reservashotel.model.Reserva;
import com.cibertec.reservashotel.services.ClienteService;
import com.cibertec.reservashotel.services.DepartamentoService;
import com.cibertec.reservashotel.services.DetalleReservaService;
import com.cibertec.reservashotel.services.HabitacionService;
import com.cibertec.reservashotel.services.HotelService;
import com.cibertec.reservashotel.services.ReservaService;
import com.cibertec.reservashotel.services.TipoHabitacionService;

@Controller
@RequestMapping("/admin")
public class HotelMantenimientoController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private DepartamentoService departamentoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private TipoHabitacionService tipoHabitacionService;
    @Autowired
    private HabitacionService habitacionService;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private DetalleReservaService detalleReservaService;

    @GetMapping("/reservas/buscar")
    public String buscarReservasPorDni(@RequestParam("dni") String dni, Model model) {
        List<Reserva> reservas = reservaService.buscarReservasPorDniCliente(dni);
        model.addAttribute("reservas", reservas);
        return "admin/reserva/lista"; // o la ruta correcta a tu HTML actual
    }
    
    
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
    
    /****************DEPARTAMENTOS CRUD************/
    
    @GetMapping("/departamentos")
    public String listaDepartamentos(Model model) {
    	model.addAttribute("departamentos",departamentoService.listar());   	
    	return "admin/departamento/lista";
    }
    
    @GetMapping("/departamentos/editar/{id}")
    public String editarDepartamento(@PathVariable Long id, Model model) {
    	
    	model.addAttribute("departamento",departamentoService.buscarPorId(id).get());
        return "admin/departamento/formulario";
    }
    
    @PostMapping("/departamentos")
    public String guardarDepartamento(@ModelAttribute Departamento dep){
    	departamentoService.guardar(dep);
    	return "redirect:/admin/departamentos";
    }
    
    @GetMapping("/departamentos/nuevo")
    public String nuevoDepartamento(Model model) {
    	model.addAttribute("departamento",new Departamento());
    	return "admin/departamento/formulario";
    }
    
    @PostMapping("/departamentos/eliminar/{id}")
    public String eliminarDepartamento(@PathVariable Long id) {
        departamentoService.eliminar(id);
        return "redirect:/admin/departamentos";
    }
    
    
    /****************RESERVA CRUD************/
    
    @GetMapping("/reservas")
    public String listaReservas(Model model) {
    	model.addAttribute("reservas",reservaService.listar());   	
    	return "admin/reserva/lista";
    }
    
    @GetMapping("/reservas/editar/{id}")
    public String editarReserva(@PathVariable Long id, Model model) {
    	Reserva reserva = reservaService.buscarPorId(id).orElse(null);
    	List<Long> habitacionesSeleccionadas = reserva.getDetalles()
    		    .stream()
    		    .map(det -> det.getHabitacion().getId())
    		    .collect(Collectors.toList());
    	Departamento departamentoEncontrado = reserva.getHotel().getDepartamento();
    	Hotel hotelEncontrado = reserva.getHotel();
    	System.out.println("Habitaciones del hotel " + hotelEncontrado.getNombre() + ":");
    	for (Habitacion h : hotelEncontrado.getHabitaciones()) {
    	    System.out.println(" - ID: " + h.getId() + ", Tipo: " + h.getTipoHabitacion().getNombre() + ", Precio: " + h.getPrecio());
    	}
    	Map<Long, Double> mapaPrecios = reserva.getHotel().getHabitaciones()
    		    .stream()
    		    .collect(Collectors.toMap(h -> h.getId(), h -> h.getPrecio()));

    	
    	model.addAttribute("mapaPrecios", mapaPrecios);
    	model.addAttribute("reserva",reserva);
    	model.addAttribute("departamento",departamentoEncontrado);
    	model.addAttribute("hoteles",hotelService.listarPorDepartamentoId(departamentoEncontrado.getId()));
    	model.addAttribute("habitacionesSeleccionadas", habitacionesSeleccionadas);
    	model.addAttribute("habitaciones", hotelEncontrado.getHabitaciones());
    	model.addAttribute("departamentos", departamentoService.listar());
        return "admin/reserva/formulario";
    }
 
    
    @PostMapping("/hoteles/{id}/reservar")
    public String guardarReserva(
            @PathVariable("id") Long reservaId,
            @ModelAttribute Reserva reserva,
            @RequestParam(name = "departamentoId") Long departamentoId,
            @RequestParam(name = "habitacionesSeleccionadas", required = false) List<Long> habitacionesSeleccionadas,
            BindingResult result,
            Model model) {

    	//se obtiene la reserva de la base de datos para poder guardar las nuevas modificaciones.
        Reserva reservaObtenida = reservaService.buscarPorId(reservaId).orElse(null);
        if (reservaObtenida == null) {
            return "redirect:/admin/reservas"; // Manejo simple si no se encuentra la reserva
        }

        // Obtener hotel seleccionado desde el binding automático por si se necesita persistir la reserva con otro hotel.
        Long hotelIdSeleccionado = reserva.getHotel().getId();
        Hotel hotelSeleccionado = hotelService.buscarPorId(hotelIdSeleccionado).orElse(null);

        if (result.hasErrors() || hotelSeleccionado == null) {
            System.out.println("Error: Hay errores en el formulario o el hotel es nulo");
            // si encuentra errores se recarga la pagina con los mismos valores de la reserva seleccionada
            model.addAttribute("reserva", reservaObtenida);
            // No es necesario pasar explícitamente el departamento actual;
            //se carga la lista completa por si el usuario quiere cambiarlo.
            model.addAttribute("departamentos", departamentoService.listar());
            // Se cargan los hoteles del departamento seleccionado; En el formulario se debe seleccionar por defecto el departamento de la reserva con javascript 
            model.addAttribute("hoteles", hotelService.listarPorDepartamentoId(departamentoId));
            model.addAttribute("habitaciones", hotelSeleccionado != null ? hotelSeleccionado.getHabitaciones() : Collections.emptyList());
            return "admin/reserva/formulario";
        }

        if (habitacionesSeleccionadas == null || habitacionesSeleccionadas.isEmpty()) {
            //misma logica que el anterior if de arriba
        	System.out.println("Error: No se seleccionaron habitaciones");
            model.addAttribute("reserva", reservaObtenida);
            model.addAttribute("error", "Debe seleccionar al menos una habitación");
            model.addAttribute("departamentos", departamentoService.listar());
            model.addAttribute("hoteles", hotelService.listarPorDepartamentoId(departamentoId));
            model.addAttribute("habitaciones", hotelSeleccionado.getHabitaciones());
            return "admin/reserva/formulario";
        }

        //al setear el hotel seleccionado, automaticamente se settea tambien por detras el departamento, por la relacion bidirecional id y cascade.
        // entonces al persitir la reserva con la modificacion se persisten tambien el hotel y el departamento.
        // como son valores ya establecidos no se necesita persistir cada cambio de departamento y hotel
        reservaObtenida.setHotel(hotelSeleccionado);

        // Guardar cliente actualizado
        // En este caso sí se modifican los valores de la entidad Cliente, por eso es necesario persistir primero los cambios.
        Cliente clienteActualizado = clienteService.guardar(reserva.getCliente());
        reservaObtenida.setCliente(clienteActualizado);

        // Fechas y estado
        reservaObtenida.setFechaInicio(reserva.getFechaInicio());
        reservaObtenida.setFechaFin(reserva.getFechaFin());
        reservaObtenida.setFechaReserva(LocalDate.now());
        reservaObtenida.setEstado("CONFIRMADA");

        // Limpiar detalles anteriores para luego agregarlos nuevamento mas abajo y no se dupliquen
        reservaObtenida.getDetalles().clear();
        reservaObtenida.setTotal(0.0);

        // Guardar reserva base (sin detalles)
        // Se persiste primero cliente y hotel antes de agregar detalles para evitar errores de referencia nula con las habitaciones.
        reservaObtenida = reservaService.guardar(reservaObtenida);
        
        // Procesar habitaciones seleccionadas
        // Es necesario setear todos los campos antes de persistir para evitar errores de integridad en la transacción.        
        double total = 0;
        for (Long habId : habitacionesSeleccionadas) {
            Habitacion habitacion = habitacionService.buscarPorId(habId).orElse(null);
            if (habitacion != null) {
                DetalleReserva detalle = new DetalleReserva();
                detalle.setReserva(reservaObtenida);
                detalle.setHabitacion(habitacion);
                detalle.setPrecioNoche(habitacion.getPrecio());
                //Se persiste porque se crea un nuevo DetalleReserva por cada habitación seleccionada y se guarda en base de datos.
                detalleReservaService.guardar(detalle);
                total += habitacion.getPrecio();
            }
        }

     // Actualizar total final con todos los cambios previamente seteados y persistidos para completar la transacción.
        reservaObtenida.setTotal(total);
        reservaService.guardar(reservaObtenida);

        return "redirect:/admin/reservas";
    }

    @GetMapping("/hoteles/por-departamento/{id}")
    @ResponseBody
    public List<Hotel> obtenerHotelesPorDepartamento(@PathVariable Long id) {
        return hotelService.listarPorDepartamentoId(id);
    }
    
    @PostMapping("/reservas/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id) {
        reservaService.eliminar(id);
        return "redirect:/admin/reservas";
    }
    
}

