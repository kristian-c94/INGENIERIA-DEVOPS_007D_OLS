package com.fullstack.servicioTecnico.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fullstack.servicioTecnico.model.ServicioTecnico;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ServicioTecnicoResponse {
    private Integer id;
    private Integer productoId;
    private String nombreProducto;
    private Integer clienteId;
    private String nombreCliente;
    private String rutCliente;
    private String numeroSerie;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaIngreso;
    private String falla;
    private String descripcion;
    private String estado;

    public ServicioTecnicoResponse(ServicioTecnico servicio, String nombreProducto, String nombreCliente, String rutCliente) {
        this.id = servicio.getId();
        this.productoId = servicio.getProductoId();
        this.nombreProducto = nombreProducto;
        this.clienteId = servicio.getClienteId();
        this.nombreCliente = nombreCliente;
        this.rutCliente = rutCliente;
        this.numeroSerie = servicio.getNumeroSerie();
        this.fechaIngreso = servicio.getFechaIngreso();
        this.falla = servicio.getFalla();
        this.descripcion = servicio.getDescripcion();
        this.estado = servicio.getEstado();
    }
}
