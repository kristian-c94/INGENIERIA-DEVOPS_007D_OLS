# INGENIERIA-DEVOPS_007D_OLS
Aprendiendo practicas Devops 
# PRUEBA 1 INGENIERIA DEVOPS
## Inicialización de repositorio
Se inicializo el repositorio en el directorio del proyecto, para después realizar el primer commit del proyecto que seria la subida del mismo en su totalidad
## Creacion de branches (ramas)
Se realizo la creacion de 3 tipos de ramas para asegurar un buen entorno de desarrollo devops.
Con los siguientes comandos:
git checkout -b developer
git checkout -b features/agregar-logs <-- este servira para añadir logs de los microservicios
git checkout -b hotfix/corregir-puerto <--- en caso de que alguna de los puertos este mal declarado y poder solucionarlo de forma rapida
### Developer
En esta rama se guarda el código con los cambios realizadas en las otras ramas, este seria como la rama que guarda el código con modificaciones para después ser aprobado y ser implementado en el main que seria la aplicación principal o que se muestra al publico

Se crea la rama develop y se actualiza
-git checkout -b develop
-git push -u origin develop

(dentro de ella estaran las otras dos ramas) 

### Features 
Aca se estaran guardando las nuevas implementaciones que se le puedan añadir al codigo

Se crea la rama dentro de developer
git checkout -b feature/agregar-logs

Se crea un documento 
echo "// Sistema de logs" > logs.txt

Se añade la solicitud para despues realizar un pull request
git add .
git commit -m "feat: agregar sistema de logs al microservicio"
git push -u origin feature/agregar-logs

### Hotfix
En esta rama principalmente se utilizara para parches que deban ser añadidos con rapidez para solucionar un problema 

Se abre la rama main y se actualiza
git checkout main
git pull origin main

Se crea la rama dentrod de main
git checkout -b hotfix/corregir-puerto

Se crea documento de prueba 
echo "# Correccion de puerto" >> config.txt

Se envia para despues hacer el respectivo pull request 
git add .
git commit -m "fix: corregir puerto de conexion en produccion"
git push -u origin hotfix/corregir-puerto

## Docker y Contenedorización

Para asegurar que los microservicios se ejecuten en cualquier entorno de la misma manera, utilizamos Docker y Docker Compose

### Dockerfile (Multi-stage Build)
Se creó un archivo `Dockerfile` para cada microservicio en múltiples etapas 

### Docker Compose y Variables de Entorno
Utilizamos un archivo `docker-compose.yml` para orquestar los 7 microservicios de de nuestro proyecto.
- **Variables de Entorno (.env):** Utilizamos un archivo `.env` para configurar los puertos de cada microservicio (ej. `PORT_AUTH=8080`).
- **Orden de arranque (depends_on):** Gracias al `healthcheck`, configuramos la condición `service_healthy` en los microservicios para garantizar que no arranquen hasta que la base de datos (en este caso no tenemos bdd, pero localmente es como funciona en este proyecto) esté completamente operativa
- **Volúmenes:** Se implementaron volúmenes locales (`mysql_data`) para persistir la información de la base de datos, y volúmenes de tipo "bind mount" para extraer y revisar los logs generados por cada microservicio

## Integración Continua (CI Pipeline)
### Trazabilidad y Garantía de Calidad
La trazabilidad del proyecto se garantiza mediante el flujo de ramas (Gitflow) y la automatización en GitHub Actions. Cada vez que se realiza un push o pull request hacia las ramas principales, el pipeline ejecuta y registra todo el proceso de construcción, validación y despliegue. Si un paso falla (por ejemplo, código que no pasa las pruebas o vulnerabilidades críticas detectadas por Snyk), el pipeline se detiene, evitando que el código defectuoso llegue a producción. La calidad del código es validada continuamente por SonarCloud, asegurando que se cumplan las métricas del proyecto.
Implementamos un flujo de trabajo automatizado utilizando GitHub Actions (`ci.yml`) que se dispara al hacer push o pull request hacia las ramas `develop` y `main

### 1. Pruebas Unitarias y Cobertura (JaCoCo)
- El pipeline descarga el código y configura Java 21
- Ejecuta de forma automática las pruebas unitarias de todos los microservicios utilizando el comando `mvn clean test`
- Genera y sube un **Reporte HTML de JaCoCo** para visualizar la cobertura del código de nuestras pruebas

### 2. Escaneo de Seguridad (Snyk)
- Se integró Snyk CLI para analizar las dependencias de los microservicios en busca de vulnerabilidades conocidas de seguridad.

### 3. Análisis de Calidad de Código (SonarCloud)
- Conectamos el proyecto a SonarCloud para realizar un análisis estático del código
- A través del plugin de Maven para Sonar, el pipeline envía los resultados del código y las pruebas directamente al dashboard de SonarCloud usando el token de seguridad.

### 4. Construcción y Publicación de Imágenes (Docker Push a GHCR)
- Tras pasar exitosamente las pruebas y los análisis, el pipeline inicia sesión en GitHub Container Registry (GHCR)
- Construye las imágenes Docker para cada microservicio y las publica de forma automatizada (Push) etiquetadas como `latest` bajo los repositorios de la organización facilitando el futuro despliegue de nuestras aplicaciones.

- ## Declaración de Uso de IA
En el desarrollo de este encargo y la documentación técnica, se utilizó Inteligencia Artificial (especificar herramienta, ej. Gemini/ChatGPT) exclusivamente como apoyo para la revisión de sintaxis en archivos YAML, corrección de errores de dependencias de Maven y mejora en la redacción de este README. Todo el contenido generado fue validado técnicamente por el equipo para asegurar la coherencia con el proyecto.

**Reflexión CHRISTIAN PASTRAN:**
La creacion de este proyecto me permitio aprender muchas mas acerca de la automatizacion de procesos para nuestros proyectos, me a ayudado a tener un mejor orden al momento de hacer trabajos en equipo como tambien aprender nuevas herramientas que en un futuro podre utilizar para poder mantener un codigo reproductible, trazable y con varias herramientas que me permiten detectar vulnerabilidades, errores, automatizar procesos, etc. Me encargue principalmente de todo lo que es la creacion del pipeline y la implementacion de las diferentes herramientas vistas en clase (sonar y snyk).

**Reflexión [Nombre de tu compañero]:**
Durante el desarrollo de esta evaluación pude profundizar en la contenedorización de aplicaciones y su orquestación. Mi contribución principal al proyecto estuvo enfocada en la creación y optimización de los archivos Dockerfile para cada microservicio, asegurando el uso de entornos ligeros y patrones de construcción eficientes, así como la configuración del archivo Docker Compose con sus respectivas dependencias y volúmenes. Este trabajo en equipo me permitió mejorar mis habilidades en código y comprender cómo integrar estos entornos con el pipeline automatizado que realizamos con mi compañero.
