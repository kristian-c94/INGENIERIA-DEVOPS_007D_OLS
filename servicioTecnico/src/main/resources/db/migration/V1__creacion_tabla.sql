CREATE TABLE servicio_tecnico (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    producto_id BIGINT NOT NULL,
    cliente_id BIGINT NOT NULL,
    numero_serie VARCHAR(255) NOT NULL,
    fecha_ingreso DATE,
    falla VARCHAR(100) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    estado VARCHAR(50) NOT NULL
);