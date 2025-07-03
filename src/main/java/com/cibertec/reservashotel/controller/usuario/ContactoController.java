package com.cibertec.reservashotel.controller.usuario;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactoController {
	@GetMapping("/contacto")
    public String mostrarContacto() {
        return "usuario/contacto";
}
}
