-- Conectarse a la base de datos
USE db_tiendamusical;

-- Borrar la entrada fallida de Flyway para productos
DELETE FROM flyway_schema_history_productos WHERE version = 2;

-- O simplemente borrar toda la tabla de historial para empezar limpio
-- DROP TABLE IF EXISTS flyway_schema_history_productos;
