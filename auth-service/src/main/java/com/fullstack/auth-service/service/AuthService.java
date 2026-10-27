package com.fullstack.auth-service.service;

import com.fullstack.auth-service.dto.LoginResponse;
import com.fullstack.auth-service.exception.MalCredencialException;
import com.fullstack.auth-service.model.Usuario;
import com.fullstack.auth-service.repository.UsuarioRepository;
import org.hibernate.exception.AuthException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new MalCredencialException("Usuario no encontrado"));
        if (!usuario.getPassword().equals(password)) {
            throw new MalCredencialException("Password incorrecta");
        }
        String token = jwtService.generarToken(usuario.getUsername(), usuario.getRol());
        return new LoginResponse("Usuario autorizado", token, usuario.getUsername(), usuario.getRol());
    }
}