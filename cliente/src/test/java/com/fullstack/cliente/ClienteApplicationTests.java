package com.fullstack.cliente;

import com.fullstack.cliente.model.Cliente;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteApplicationTests {

	@Test
	void testCrearClienteExitoso() {
		Cliente cliente = new Cliente();
		cliente.setId(1);
		cliente.setRun("12345678-9");
		cliente.setNombre("Juan");
		cliente.setApellido("Perez");
		cliente.setCorreo("juan.perez@example.com");

		assertEquals(1, cliente.getId());
		assertEquals("12345678-9", cliente.getRun());
		assertEquals("Juan", cliente.getNombre());
		assertEquals("Perez", cliente.getApellido());
		assertEquals("juan.perez@example.com", cliente.getCorreo());
	}

	@Test
	void testValidacionCorreoCliente() {
		Cliente cliente = new Cliente();
		cliente.setCorreo("contacto@tienda.cl");

		assertNotNull(cliente.getCorreo());
		assertTrue(cliente.getCorreo().contains("@"), "El correo debe contener un '@'");
	}
}