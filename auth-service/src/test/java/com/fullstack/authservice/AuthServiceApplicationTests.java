package com.fullstack.authservice;

import com.fullstack.authservice.model.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

	@Test
	void testCrearUsuarioExitoso() {
		// Arrange
		Usuario usuario = new Usuario();
		usuario.setId(1);
		usuario.setUsername("admin");
		usuario.setPassword("password123");
		usuario.setRol("ROLE_ADMIN");

		// Act & Assert
		assertEquals(1, usuario.getId());
		assertEquals("admin", usuario.getUsername());
		assertEquals("password123", usuario.getPassword());
		assertEquals("ROLE_ADMIN", usuario.getRol());
	}

	@Test
	void testConstructorConParametros() {
		Usuario usuario = new Usuario(2, "user1", "secret", "ROLE_USER");

		assertNotNull(usuario);
		assertEquals(2, usuario.getId());
		assertEquals("user1", usuario.getUsername());
		assertEquals("ROLE_USER", usuario.getRol());
	}
}