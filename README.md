### INGENIERIA-DEVOPS_007D_OLS

Aprendiendo practicas Devops
### PRUEBA 1 INGENIERIA DEVOPS

**Resumen del Proyecto**
El proyecto subido al repositorio consiste en el microservicio **`auth-Service`**, una aplicación desarrollada en Java 17 con Spring Boot enfocada en la gestión de autenticación y autorización mediante tokens JWT. El servicio implementa persistencia de datos relacional integrada con Flyway para el control de versiones de la base de datos, arquitectura limpia mediante la separación de controladores, servicios y repositorios, e incluye un manejo global de excepciones estandarizado.

## Inicialización de repositorio

Se inicializo el repositorio en el directorio del proyecto, para después realizar el primer commit del proyecto que seria la subida del mismo en su totalidad
Creacion de branches (ramas)

Se realizo la creacion de 3 tipos de ramas para asegurar un buen entorno de desarrollo devops. Con los siguientes comandos: git checkout -b developer git checkout -b features/agregar-logs <-- este servira para añadir logs de los microservicios git checkout -b hotfix/corregir-puerto <--- en caso de que alguna de los puertos este mal declarado y poder solucionarlo de forma rapida
Developer

En esta rama se guarda el código con los cambios realizadas en las otras ramas, este seria como la rama que guarda el código con modificaciones para después ser aprobado y ser implementado en el main que seria la aplicación principal o que se muestra al publico
Se crea la rama develop y se actualiza -git checkout -b develop -git push -u origin develop
(dentro de ella estaran las otras dos ramas)
Features

Aca se estaran guardando las nuevas implementaciones que se le puedan añadir al codigo
Se crea la rama dentro de developer git checkout -b feature/agregar-logs
Se crea un documento echo "// Sistema de logs" > logs.txt
Se añade la solicitud para despues realizar un pull request git add . git commit -m "feat: agregar sistema de logs al microservicio" git push -u origin feature/agregar-logs
Hotfix

En esta rama principalmente se utilizara para parches que deban ser añadidos con rapidez para solucionar un problema
Se abre la rama main y se actualiza git checkout main git pull origin main
Se crea la rama dentrod de main git checkout -b hotfix/corregir-puerto
Se crea documento de prueba echo "# Correccion de puerto" >> config.txt
Se envia para despues hacer el respectivo pull request git add . git commit -m "fix: corregir puerto de conexion en produccion" git push -u origin hotfix/corregir-puerto

---

**Argumentación del uso de GitFlow**
Se eligio el metodo de ramas **GitFlow**:
- Git flow es un modelo de organización de ramas (branching) para Git que utiliza diferentes ramas para gestionar el desarrollo, las pruebas y las versiones de producción de un software de manera estructurada, para optimizar la colaboración y estabilidad del microservicio:

* **Aislamiento de Cambios:** Separa el código de producción (`main`), el entorno de integración (`develop`), el trabajo de nuevas funcionalidades (`feature/`) y las correcciones de emergencia (`hotfix/`).
* **Desarrollo Paralelo:** Permite trabajar en la rama `feature/agregar-logs` sin interferir con la estabilidad de `develop` ni con el trabajo de otros desarrolladores.
* **Respuesta Eficiente:** Facilita la creación directa de la rama `hotfix/corregir-puerto` desde `main` para aplicar parches críticos en producción sin arrastrar código incompleto que esté en desarrollo.
* **Trazabilidad:** Fuerza el paso por Pull Requests antes de integrar código, asegurando la revisión por pares y el disparo automático de validaciones en el pipeline.

**Funcionamiento del Pipeline de CI/CD**
El pipeline automatiza el ciclo de vida del código cargado al repositorio mediante las siguientes fases principales:

-Se ejecuta al hacer un push hacia la rama **develop** o al hacer un **pull request** a la rama main

-Esta misma corre atravez de una maquina virtual de Ubuntu

-Usa **actions/checkout@v4** para crear una copia del repositorio en la maquina de ubuntu

-Se configura la version 17 de java JDK **actions/setup-java@v4**

-Y al final no se ejecutan comandos de compilacion reales, si no que se ejecutan instrucciones **echo** que imprimen mensajes en la consola de github actions para hacer una simulacion del proceso de compilacion.


**Explicación Técnica del Microservicio (`auth-Service`)**
El sistema se compone de los siguientes módulos internos:

* **Capa de Controladores (`AuthController.java`):** Expone los endpoints REST públicos y privados para la recepción de credenciales y devolución de tokens.
* **Capa de Seguridad (`SecurityConfig.java` & `JwtService.java`):** Configura la cadena de filtros de Spring Security, gestiona la firma criptográfica, vigila la expiración de tokens JWT y protege los accesos al sistema.
* **Capa de Negocio y Datos (`AuthService.java`, `Usuario.java`, `UsuarioRepository.java`):** Procesa la lógica de autenticación y gestiona la comunicación con la base de datos mediante Spring Data JPA.
* **Manejo de Errores (`GlobalExceptionHandler.java` & `ErrorResponse.java`):** Intercepta excepciones como `MalCredencialException` y genera respuestas JSON limpias y estructuradas con códigos HTTP pertinentes.
* **Migraciones SQL (`V1__crea_tabla_auth.sql` & `V2__insertar_usuarios.sql`):** Mantienen el control de versiones sobre la estructura de la base de datos e insertan los registros iniciales para pruebas.

### Uso de GEMINI IA
-Se realizo el uso de IA para la orientacion al momento de la creacion del pipeline y para la claritud de diferentes conceptos que no se llegaban a conocer como tambien guia al no entender errores o mal uso de los comandos 
