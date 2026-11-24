package com.fullstack.productos;

import com.fullstack.productos.model.Producto;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

	@Test
	void testCrearProductoExitoso() {
		// Arrange
		Producto producto = new Producto();
		producto.setId(1);
		producto.setNumeroSerie("SN-12345");
		producto.setNombreProducto("Batería Acústica");
		producto.setTipoInstrumento("Percusión");
		producto.setDescripcion("Batería de 5 piezas con platillos");
		producto.setPrecioInstrumento(450000);
		producto.setPrecioArriendo(30000);
		producto.setVentaArriendo("Ambos");
		producto.setEstado("Disponible");
		producto.setFechaRegistro(new Date());

		// Act & Assert
		assertEquals(1, producto.getId());
		assertEquals("SN-12345", producto.getNumeroSerie());
		assertEquals("Batería Acústica", producto.getNombreProducto());
		assertEquals("Percusión", producto.getTipoInstrumento());
		assertEquals(450000, producto.getPrecioInstrumento());
		assertEquals("Disponible", producto.getEstado());
	}

	@Test
	void testConstructorAllArgsConstructor() {
		Producto producto = new Producto(
				2, "SN-99999", "Amplificador 50W", "Accesorios",
				"Amplificador para guitarra", 120000, null, "Venta", "Disponible", new Date()
		);

		assertNotNull(producto);
		assertEquals("Amplificador 50W", producto.getNombreProducto());
		assertEquals(120000, producto.getPrecioInstrumento());
	}
}