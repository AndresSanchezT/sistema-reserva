package com.cibertec.reservashotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cibertec.reservashotel.repository.DepartamentoRepository;

@Controller
public class DepartamentoSeleccionController {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @GetMapping("/hoteles/seleccionar-departamento")
    public String seleccionarDepartamento(Model model) {
        model.addAttribute("departamentos", departamentoRepository.findAll());
        return "hotel/seleccionar_departamento";
    }
}
