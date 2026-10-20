package com.fullstack.reclamos;

import com.fullstack.reclamos.model.Reclamos;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ReclamosTest {

	@Test
	void testCrearReclamoExitoso() {
		// Arrange
		Reclamos reclamo = new Reclamos();
		reclamo.setId(1);
		reclamo.setClienteId(10);
		reclamo.setNombreCliente("Pedro Pascal");
		reclamo.setFechaRegistro(new Date());
		reclamo.setAsunto("Retraso en el envío");
		reclamo.setDescripcion("El pedido no ha llegado en la fecha acordada.");

		// Act & Assert
		assertEquals(1, reclamo.getId());
		assertEquals(10, reclamo.getClienteId());
		assertEquals("Pedro Pascal", reclamo.getNombreCliente());
		assertEquals("Retraso en el envío", reclamo.getAsunto());
		assertEquals("El pedido no ha llegado en la fecha acordada.", reclamo.getDescripcion());
	}

	@Test
	void testConstructorAllArgsConstructor() {
		Date fecha = new Date();
		Reclamos reclamo = new Reclamos(
				2, 15, "Laura Gomez", fecha, "Producto dañado", "El paquete llego golpeado"
		);

		assertNotNull(reclamo);
		assertEquals(2, reclamo.getId());
		assertEquals("Producto dañado", reclamo.getAsunto());
	}
}