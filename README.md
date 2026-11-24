# INGENIERIA-DEVOPS_007D_OLS — Tienda Musical (Microservicios)

Sistema basado en arquitectura de microservicios para la gestión de una **Tienda Musical**, desarrollado bajo prácticas DevOps de Integración Continua (CI), Despliegue Continuo (CD), gobernanza de código, seguridad automatizada y observabilidad en la nube sobre **AWS EKS** e **Istio Service Mesh**.

---

## Arquitectura del Proyecto

El ecosistema está compuesto por **7 microservicios Spring Boot (Java 21)**, una base de datos relacional **MySQL 8.0** y una capa de enrutamiento y telemetría gestionada por **Istio (Ambient Mesh + Kubernetes Gateway API)**:

| Componente | Tecnología | Puerto Interno | Ruta en Gateway (`tienda-routes`) | Función Principal |
| :--- | :--- | :---: | :--- | :--- |
| `auth-service` | Spring Boot / Java 21 | `8080` | `/api/auth` | Autenticación y emisión de tokens JWT |
| `cliente-service` | Spring Boot / Java 21 | `8081` | `/api/clientes` | Gestión y registro de clientes |
| `reclamo-service` | Spring Boot / Java 21 | `8082` | `/api/reclamos` | Gestión de reclamos postventa |
| `producto-service` | Spring Boot / Flyway | `8083` | `/api/productos` | Catálogo e inventario de instrumentos |
| `servicio-tecnico-service` | Spring Boot / Java 21 | `8084` | `/api/servicioTecnico` | Órdenes de reparación y mantenimiento |
| `pedidos-service` | Spring Boot / Java 21 | `8085` | `/api/pedidos` | Procesamiento de órdenes de compra |
| `devoluciones-service` | Spring Boot / Java 21 | `8086` | `/api/devoluciones` | Gestión de devoluciones y reembolsos |
| `mysql-db` | MySQL 8.0 | `3306` | Interno (`ClusterIP`) | Base de datos compartida (`db_tiendamusical`) |

---

## Flujo de Trabajo Git y Gobernanza (Gitflow + Branch Protection)

El control de versiones sigue la estrategia **Gitflow** para aislar el desarrollo, las nuevas funcionalidades y las correcciones urgentes antes de llegar a producción:

* **`main`:** Rama productiva estable. Protegida mediante **Branch Protection Rules** en GitHub que impiden `push` directos y exigen aprobación mediante *Pull Request* y validación exitosa del pipeline CI.
* **`develop`:** Rama de integración continua donde convergen las nuevas funcionalidades. Cada `push` hacia `develop` dispara el ciclo completo de pruebas, auditoría de seguridad, construcción de imágenes y despliegue automatizado en AWS EKS.
* **`feature/*`:** Ramas creadas desde `develop` para desarrollar nuevas características (ej. `feature/agregar-logs`).
* **`hotfix/*`:** Ramas creadas desde `main` para resolver incidentes críticos en producción de forma aislada (ej. `hotfix/corregir-puerto`).


## Contenedorización Local (Docker y Docker Compose)

Para garantizar paridad entre el entorno de desarrollo local y el clúster en la nube:
* **Dockerfile Multi-stage:** Cada microservicio cuenta con un `Dockerfile` en múltiples etapas que compila el artefacto con Maven/JDK 21 y genera una imagen de ejecución ligera.
* **Orquestación con `docker-compose.yml`:** Permite levantar el ecosistema completo localmente utilizando un archivo `.env` para inyectar variables de entorno, volúmenes persistentes (`mysql_data`) y `healthcheck` (`service_healthy`) para asegurar que MySQL esté operativo antes de iniciar los servicios Java.

---

## Pipeline CI/CD Automatizado (`ci.yml`)

El flujo definido en `.github/workflows/ci.yml` ejecuta **5 etapas secuenciales** utilizando ejecución matricial (`matrix`) sobre los 7 microservicios:

### 1. Pruebas Unitarias, Cobertura JaCoCo y Métrica en CloudWatch (`test`)
* Compila y ejecuta las pruebas unitarias con `mvn clean test -Dspring.profiles.active=test` sobre Java 21 (Temurin).
* Extrae el porcentaje de cobertura de líneas desde `target/site/jacoco/jacoco.xml` mediante un script en Python y verifica el umbral mínimo exigido.
* Publica la métrica personalizada **`CoberturaPruebas`** (dimensión `Servicio`) en **AWS CloudWatch** bajo el namespace `TiendaMusical/CICD` y almacena el reporte HTML como artefacto.

### 2. Análisis de Vulnerabilidades de Dependencias (`snyk`)
* Ejecuta `mvn dependency:tree` y analiza las librerías de cada microservicio con **Snyk CLI** (`snyk test --severity-threshold=high`).
* **Política de bloqueo:** Si detecta vulnerabilidades de severidad **High** o **Critical**, el job falla y detiene el pipeline.

### 3. Análisis Estático de Calidad de Código (`sonarcloud`)
* Ejecuta el escáner de **SonarCloud** (`sonar-maven-plugin`) con el parámetro `-Dsonar.qualitygate.wait=true`.
* **Política de bloqueo:** Si el código no supera el *Quality Gate* configurado (bugs, vulnerabilidades, *code smells* o deuda técnica), el pipeline se interrumpe automáticamente.

### 4. Construcción y Publicación en GitHub Container Registry (`docker`)
* Depende estrictamente de que los tres jobs anteriores (`test`, `snyk`, `sonarcloud`) finalicen en verde (`needs: [test, snyk, sonarcloud]`).
* Construye las imágenes `linux/amd64` con Docker Buildx y las publica en **`ghcr.io`** etiquetadas con `:latest` y el hash del commit (`:${{ github.sha }}`).

### 5. Despliegue Continuo a AWS EKS y Telemetría (`deploy-eks`)
* Se ejecuta tras un `push` en `develop` o `main` una vez publicadas las imágenes.
* Conecta `kubectl` al clúster **`tienda-musical-eks`** (`us-east-1`), aplica los manifiestos en `k8s/`, reinicia los *deployments* (`tier=app`) y verifica el estado del despliegue con `kubectl rollout status`.
* Calcula la duración total del despliegue en segundos y envía la métrica **`TiempoDespliegueSegundos`** a CloudWatch, además de actualizar automáticamente el dashboard **`TiendaMusical-CICD`** desde `monitoring/dashboard.json`.

---

## Despliegue en AWS EKS e Istio Service Mesh (`k8s/`)

La infraestructura en la nube está declarada como código en tres manifiestos principales:

1. **`k8s/00-namespace-mysql.yaml`:** Crea el namespace `tienda` con la etiqueta `istio.io/dataplane-mode: ambient` para habilitar observabilidad de red, telemetría y cifrado mTLS automático sin necesidad de contenedores *sidecar*, y despliega `mysql-db` con sonda de disponibilidad (`mysqladmin ping`).
2. **`k8s/01-microservices.yaml`:** Despliega los 7 microservicios desde GHCR con límites de recursos (`requests` y `limits` de CPU/memoria), migraciones de base de datos con Flyway y sondas de salud de **Spring Boot Actuator** (`startupProbe`, `readinessProbe` y `livenessProbe` en `/actuator/health/*`) para garantizar cero tiempo de inactividad durante los *rolling updates*.
3. **`k8s/02-gateway.yaml`:** Implementa **Kubernetes Gateway API** (`gateway.networking.k8s.io/v1`) mediante el recurso `Gateway` (`tienda-gateway` con controlador `istio`) y `HTTPRoute` (`tienda-routes`) para exponer y enrutar todo el tráfico HTTP hacia cada microservicio según su prefijo `/api/*`.



## Observabilidad, Monitoreo y Dashboards

La plataforma integra tres niveles de observabilidad para supervisar tanto el proceso de entrega como el comportamiento en tiempo real del clúster:

### 1. Dashboard Personalizado en AWS CloudWatch (`TiendaMusical-CICD`)
Definido como código en `monitoring/dashboard.json` y actualizado automáticamente por el pipeline. Consolida en una sola vista:
* **Métricas del Pipeline (`TiendaMusical/CICD`):** Porcentaje de cobertura de pruebas unitarias por microservicio (`CoberturaPruebas`) y duración de cada despliegue en Kubernetes (`TiempoDespliegueSegundos`).
* **Métricas de Infraestructura (`ContainerInsights`):** Utilización promedio de CPU (`pod_cpu_utilization`) y memoria (`pod_memory_utilization`), cantidad de pods activos (`namespace_number_of_running_pods`) y reinicios de contenedores (`pod_number_of_container_restarts`) en el namespace `tienda`.
* **Registro Centralizado de Errores (CloudWatch Logs Insights):** Consulta automatizada sobre `/aws/containerinsights/tienda-musical-eks/application` que filtra y lista en tiempo real las trazas con nivel `ERROR` generadas por los microservicios.


### 2. Topología y Tráfico de Malla con Kiali y Grafana
Gracias a la integración de Istio en el namespace `tienda`, se monitorea la comunicación entre servicios:
* **Kiali (`Graph -> namespace tienda`):** Visualiza en tiempo real el mapa de dependencias entre `tienda-gateway` y los 7 microservicios, tasas de éxito HTTP y latencias.
* **Grafana (Dashboards de Istio):** Permite inspeccionar series temporales detalladas de volumen de peticiones por segundo, códigos de respuesta (`2xx`, `4xx`, `5xx`) y tiempos de respuesta por servicio.
  
## Toma de Decisiones Técnicas Basada en Métricas y Seguridad

La integración de estas herramientas en el ciclo CI/CD permite tomar decisiones objetivas antes y después de cada despliegue:

| Herramienta / Métrica | Indicador Observado | Decisión Técnica Habilitada |
| :--- | :--- | :--- |
| **JaCoCo (`CoberturaPruebas`)** | Porcentaje de líneas cubiertas por pruebas unitarias en cada microservicio. | Priorizar la escritura de pruebas en los servicios con menor cobertura y ajustar el umbral mínimo del pipeline antes de liberar nuevas versiones. |
| **Snyk Security Scan** | Vulnerabilidades conocidas (CVE) de severidad alta o crítica en dependencias Maven. | Bloquear la construcción de imágenes Docker vulnerables y actualizar versiones de dependencias en el `pom.xml` antes de exponer el servicio en EKS. |
| **SonarCloud Quality Gate** | Bugs, vulnerabilidades estáticas, duplicidad y deuda técnica. | Exigir refactorización mediante *Pull Request* antes de permitir la integración en `develop` o `main`. |
| **CloudWatch (`TiempoDespliegueSegundos`)** | Tiempo total desde `kubectl apply` hasta que los 7 pods aprueban su `readinessProbe`. | Optimizar los tiempos de arranque de Spring Boot y calibrar los `initialDelaySeconds` y `periodSeconds` de las sondas de salud. |
| **Container Insights + Logs Insights** | Picos de CPU/memoria, reinicios de pods (`CrashLoopBackOff`) y trazas `ERROR`. | Ajustar los `requests` y `limits` de memoria de la JVM (`768Mi`) y diagnosticar fallos de conexión a MySQL o migraciones Flyway sin entrar contenedor por contenedor. |
| **Kiali y Grafana** | Errores de enrutamiento (`404`/`503`) o latencia elevada entre el Gateway y los backends. | Corregir prefijos en `HTTPRoute` (`02-gateway.yaml`) o escalar réplicas del microservicio con mayor carga. |

---

## Políticas de Cumplimiento y Evidencia de Bloqueo del Pipeline

Para proteger el entorno de producción en AWS EKS, el pipeline actúa como barrera automática ante incumplimientos normativos o de calidad:
* **Auditoría de Calidad (SonarCloud):** Validación obligatoria del *Quality Gate*.
* **Auditoría de Seguridad (Snyk):** Bloqueo automático ante vulnerabilidades `high` o `critical`.
* **Gobernanza de Código (GitHub):** Reglas de protección en ramas principales que impiden saltarse las verificaciones automáticas.


## Declaración de Uso de Inteligencia Artificial

En el desarrollo de esta evaluación y su documentación técnica se utilizó Inteligencia Artificial (**Google Gemini**) exclusivamente como herramienta de apoyo para:
1. Revisión de sintaxis y estructura de archivos YAML (`ci.yml` y manifiestos de Kubernetes/Istio).
2. Diagnóstico de conectividad de red, enrutamiento con Kubernetes Gateway API y consultas de métricas en AWS CloudWatch.
3. Organización y mejora de redacción técnica de este documento `README.md`.

Todo el código, configuraciones en AWS/GitHub y evidencias fueron implementados, probados y validados técnicamente por los integrantes del equipo. Las reflexiones individuales presentadas a continuación fueron redactadas íntegramente por cada estudiante sin apoyo de IA.

---

## Reflexiones Individuales

**Reflexión Christian Pastran:**
Desarrollar este proyecto junto a mi compañero me ayudó a entender realmente para qué sirven las prácticas DevOps en el día a día. Más allá de configurar herramientas, comprendí lo importante que es trabajar de forma ordenada en equipo usando ramas y revisiones antes de integrar cambios. Al encargarme principalmente del pipeline de automatización y las validaciones de calidad y seguridad, me di cuenta de cómo estos filtros evitan que subamos errores sin darnos cuenta, logrando que el camino desde escribir el código hasta verlo funcionando en la nube sea mucho más rápido, seguro y confiable.

**Reflexión Diego Mancilla:**
Con este trabajo pude entender de forma práctica cómo lograr que un sistema con varios servicios funcione igual tanto en nuestros computadores como en la nube. Al enfocarme en preparar los contenedores con Docker y levantar todo el entorno local, comprendí por qué es tan importante empaquetar bien cada servicio y asegurarse de que la base de datos esté lista antes de arrancar. Trabajar en equipo me permitió ver cómo esa base local se conecta directamente con el pipeline automatizado de mi compañero y con el monitoreo final, entendiendo de manera clara todo el ciclo de vida de una aplicación.
