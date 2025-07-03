package com.cibertec.reservashotel.controller.auth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.reservashotel.model.Usuario;
import com.cibertec.reservashotel.services.RolService;
import com.cibertec.reservashotel.services.UsuarioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private RolService rolService;
    
    // Mostrar página de login
    @GetMapping("/login")
    public String mostrarLogin(Model model, HttpSession session) {
        // Si ya está logueado, redirigir al dashboard
        if (session.getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            return usuario.isAdmin() ? "redirect:/admin/dashboard" : "redirect:/user/dashboard";
        }
        
        model.addAttribute("usuario", new Usuario());
        return "auth/login";
    }
    
    // Procesar login
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        
        try {
            Usuario usuario = usuarioService.autenticarUsuario(email, password);
            
            if (usuario != null) {
                // Guardar usuario en sesión
                session.setAttribute("usuario", usuario);
                session.setAttribute("nombreUsuario", usuario.getNombre());
                session.setAttribute("rolUsuario", usuario.getRol().getNombre());
                
                // Redirigir según el rol
                if (usuario.isAdmin()) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/user/dashboard";
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Email o contraseña incorrectos");
                return "redirect:/auth/login";
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al iniciar sesión: " + e.getMessage());
            return "redirect:/auth/login";
        }
    }
    
    // Mostrar página de registro
    @GetMapping("/register")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/register";
    }
    
    // Procesar registro
    @PostMapping("/register")
    public String procesarRegistro(@Valid @ModelAttribute("usuario") Usuario usuario,
                                 BindingResult result,
                                 @RequestParam String confirmarPassword,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        
        // Validar errores de binding
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        // Validar que las contraseñas coincidan
        if (!usuario.getPassword().equals(confirmarPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "auth/register";
        }
        
        try {
            usuarioService.registrarUsuario(usuario);
            redirectAttributes.addFlashAttribute("success", 
                "Usuario registrado exitosamente. Ahora puedes iniciar sesión.");
            return "redirect:/auth/login";
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
    
    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Sesión cerrada exitosamente");
        return "redirect:/auth/login";
    }
    
    // Dashboard para usuarios normales
    @GetMapping("/user/dashboard")
    public String dashboardUsuario(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        
        if (usuario.isAdmin()) {
            return "redirect:/admin/dashboard";
        }
        
        model.addAttribute("usuario", usuario);
        return "user/dashboard";
    }
    
    // Dashboard para administradores
    @GetMapping("/admin/dashboard")
    public String dashboardAdmin(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/auth/login";
        }
        
        if (!usuario.isAdmin()) {
            return "redirect:/user/dashboard";
        }
        
        model.addAttribute("usuario", usuario);
        // Agregar datos específicos del admin si es necesario
        model.addAttribute("totalUsuarios", usuarioService.listarUsuariosActivos().size());
        
        return "admin/dashboard";
    }
    
    // Página principal - redirige según el estado de login
    @GetMapping("/")
    public String inicio(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            return usuario.isAdmin() ? "redirect:/admin/dashboard" : "redirect:/user/dashboard";
        }
        return "redirect:/auth/login";
    }
}