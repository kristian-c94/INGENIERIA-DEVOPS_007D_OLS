package com.duoc.pedidos;

import com.duoc.pedidos.model.Pedido;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

	@Test
	void testCrearPedidoExitoso() {
		// Arrange
		Pedido pedido = new Pedido();
		pedido.setId(1);
		pedido.setClienteId(10);
		pedido.setProductoId(5);
		pedido.setNombreCliente("Maria Gonzalez");
		pedido.setNombreProducto("Teclado Yamaha");
		pedido.setTipoInstrumento("Teclados");
		pedido.setDescripcion("Teclado de 61 teclas");
		pedido.setVentaArriendo("Venta");
		pedido.setEstado("Pendiente");
		pedido.setDireccion("Av. Siempreviva 742");
		pedido.setPrecioEnvio(5000);
		pedido.setFechaPedido(new Date());

		// Act & Assert
		assertEquals(1, pedido.getId());
		assertEquals(10, pedido.getClienteId());
		assertEquals(5, pedido.getProductoId());
		assertEquals("Maria Gonzalez", pedido.getNombreCliente());
		assertEquals("Teclado Yamaha", pedido.getNombreProducto());
		assertEquals("Venta", pedido.getVentaArriendo());
		assertEquals("Pendiente", pedido.getEstado());
		assertEquals(5000, pedido.getPrecioEnvio());
	}

	@Test
	void testValidacionEstructuraEstado() {
		Pedido pedido = new Pedido();
		pedido.setEstado("Enviado");

		assertNotNull(pedido.getEstado());
		assertFalse(pedido.getEstado().isEmpty(), "El estado del pedido no debe estar vacío");
	}
}