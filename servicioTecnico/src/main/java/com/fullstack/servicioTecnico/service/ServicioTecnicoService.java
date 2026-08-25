package com.fullstack.servicioTecnico.service;

import com.fullstack.servicioTecnico.dto.ServicioTecnicoRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fullstack.servicioTecnico.model.ServicioTecnico;
import com.fullstack.servicioTecnico.repository.ServicioTecnicoRepository;
import com.fullstack.servicioTecnico.webClient.ProductoClient;
import com.fullstack.servicioTecnico.webClient.ClienteClient;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ServicioTecnicoService {

    @Autowired
    private ServicioTecnicoRepository servicioTecnicoRepository;

    @Autowired
    private ProductoClient productoClient;

    @Autowired
    private ClienteClient clienteClient;

    public List<ServicioTecnico> listarServicioTecnico(){
        return servicioTecnicoRepository.findAll();
    }

    public ServicioTecnico buscarPorId(Integer id){
        return servicioTecnicoRepository.findById(id).orElse(null);
    }

    public ServicioTecnico guardar(ServicioTecnico servicioTecnico){
        return servicioTecnicoRepository.save(servicioTecnico);
    }

    public ServicioTecnico crearDesdeRequest(ServicioTecnicoRequest request, String token) {
        Map<String, Object> producto = productoClient.obtenerProductoId(request.getProductoId(), token);
        Map<String, Object> cliente = clienteClient.obtenerClienteId(request.getClienteId(), token);
        if(producto == null || producto.isEmpty()) {
            throw new RuntimeException("Error: El producto no existe en el catálogo principal.");
        }
        if(cliente == null || cliente.isEmpty()) {
            throw new RuntimeException("Error: El cliente no existe en el sistema.");
        }
        ServicioTecnico servicioTecnico = new ServicioTecnico();
        servicioTecnico.setFalla(request.getFalla());
        servicioTecnico.setDescripcion(request.getDescripcion());
        servicioTecnico.setEstado(request.getEstado());
        servicioTecnico.setClienteId(request.getClienteId());
        servicioTecnico.setProductoId(request.getProductoId());
        servicioTecnico.setFechaIngreso(request.getFechaIngreso());
        String nombre = cliente.get("nombre").toString();
        String apellido = cliente.get("apellido").toString();
        String nombreProducto = producto.get("nombreProducto").toString();
        String numeroSerie = producto.get("numeroSerie").toString();
        servicioTecnico.setNombreCliente(nombre + " " + apellido);
        servicioTecnico.setNumeroSerie(numeroSerie);
        servicioTecnico.setNombreProducto(nombreProducto);

        return guardar(servicioTecnico);
    }

    public boolean eliminar(Integer id){
        ServicioTecnico servicioTecnico = buscarPorId(id);
        if(servicioTecnico == null){
            return false;
        }
        servicioTecnicoRepository.delete(servicioTecnico);
        return true;
    }
}
