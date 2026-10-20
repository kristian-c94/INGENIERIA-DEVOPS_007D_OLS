package com.fullstack.servicioTecnico;

import com.fullstack.servicioTecnico.model.ServicioTecnico;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ServicioTecnicoTest {

	@Test
	void testCrearServicioTecnicoExitoso() {
		// Arrange
		ServicioTecnico servicio = new ServicioTecnico();
		servicio.setId(1);
		servicio.setNumeroSerie("SN-778899");
		servicio.setProductoId(3);
		servicio.setClienteId(12);
		servicio.setNombreCliente("Gonzalo Valenzuela");
		servicio.setNombreProducto("Piano Digital");
		servicio.setFechaIngreso(new Date());
		servicio.setFalla("Tecla pegada");
		servicio.setDescripcion("La tecla Do central no emite sonido al presionarla.");
		servicio.setEstado("En Revision");

		// Act & Assert
		assertEquals(1, servicio.getId());
		assertEquals("SN-778899", servicio.getNumeroSerie());
		assertEquals(3, servicio.getProductoId());
		assertEquals(12, servicio.getClienteId());
		assertEquals("Gonzalo Valenzuela", servicio.getNombreCliente());
		assertEquals("Piano Digital", servicio.getNombreProducto());
		assertEquals("Tecla pegada", servicio.getFalla());
		assertEquals("En Revision", servicio.getEstado());
	}

	@Test
	void testConstructorAllArgsConstructor() {
		Date fecha = new Date();
		ServicioTecnico servicio = new ServicioTecnico(
				2, "SN-000111", 5, 20, "Camila Silva", "Consola Mix", fecha, "Sin encender", "Fallo de fuente de poder", "Reparado"
		);

		assertNotNull(servicio);
		assertEquals(2, servicio.getId());
		assertEquals("SN-000111", servicio.getNumeroSerie());
		assertEquals("Reparado", servicio.getEstado());
	}
}