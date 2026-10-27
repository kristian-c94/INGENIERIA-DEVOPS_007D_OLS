package com.fullstack.authservice.service;

import com.fullstack.authservice.dto.LoginResponse;
import com.fullstack.authservice.exception.MalCredencialException;
import com.fullstack.authservice.model.Usuario;
import com.fullstack.authservice.repository.UsuarioRepository;
import com.fullstack.authservice.exception.AuthException;
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