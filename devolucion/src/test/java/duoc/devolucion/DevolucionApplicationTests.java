package duoc.devolucion;

import duoc.devolucion.model.Devolucion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DevolucionTest {

	@Test
	void testCrearDevolucionExitosa() {
		// Arrange
		Devolucion devolucion = new Devolucion();
		devolucion.setId(1);
		devolucion.setClienteId(10);
		devolucion.setPedidoId(100);
		devolucion.setRequerimiento("Cambio de producto");
		devolucion.setMotivo("Falla de fábrica");
		devolucion.setNombreCliente("Carlos Lopez");
		devolucion.setNumeroSerie("SN-987654");
		devolucion.setNombreProducto("Guitarra Eléctrica");
		devolucion.setPrecioInstrumento(250000);
		devolucion.setPrecio(250000);

		// Act & Assert
		assertEquals(1, devolucion.getId());
		assertEquals(10, devolucion.getClienteId());
		assertEquals(100, devolucion.getPedidoId());
		assertEquals("Falla de fábrica", devolucion.getMotivo());
		assertEquals("Guitarra Eléctrica", devolucion.getNombreProducto());
		assertEquals(250000, devolucion.getPrecio());
	}

	@Test
	void testConstructorAllArgsConstructor() {
		Devolucion devolucion = new Devolucion(
				2, 15, 200, "Reembolso", "Garantía",
				"Ana Maria", "SN-112233", "Bajo Eléctrico", 300000, 300000
		);

		assertNotNull(devolucion);
		assertEquals("Reembolso", devolucion.getRequerimiento());
		assertEquals("SN-112233", devolucion.getNumeroSerie());
	}
}