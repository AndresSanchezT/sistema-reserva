package com.cibertec.reservashotel.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cibertec.reservashotel.model.Rol;
import com.cibertec.reservashotel.model.Usuario;
import com.cibertec.reservashotel.repository.RolRepository;
import com.cibertec.reservashotel.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Registrar nuevo usuario
    public Usuario registrarUsuario(Usuario usuario) throws Exception {
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new Exception("El email ya está registrado");
        }
        
        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        // Asignar rol USER por defecto si no tiene rol
        if (usuario.getRol() == null) {
            Rol rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new Exception("Rol USER no encontrado"));
            usuario.setRol(rolUser);
        }
        
        return usuarioRepository.save(usuario);
    }
    
    // Autenticar usuario
    public Usuario autenticarUsuario(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return usuario;
            }
        }
        return null;
    }
    
    // Buscar usuario por email
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    // Buscar usuario por ID
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    // Listar todos los usuarios activos
    public List<Usuario> listarUsuariosActivos() {
        return usuarioRepository.findAll();
    }
    
    // Actualizar usuario
    public Usuario actualizarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    // Desactivar usuario
    public void desactivarUsuario(Long id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        }
    }
    
    // Cambiar contraseña
    public boolean cambiarPassword(Long userId, String passwordActual, String passwordNueva) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (passwordEncoder.matches(passwordActual, usuario.getPassword())) {
                usuario.setPassword(passwordEncoder.encode(passwordNueva));
                usuarioRepository.save(usuario);
                return true;
            }
        }
        return false;
    }
}