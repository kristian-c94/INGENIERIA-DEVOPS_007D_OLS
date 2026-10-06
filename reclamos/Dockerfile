# ==========================================
# ETAPA 1: Construcción (Build)
# ==========================================
FROM maven:3.9-eclipse-temurin-21 AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivo de dependencias primero (aprovecha la caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar omitiendo los tests en el build de imagen
# (Los tests se ejecutan antes en el pipeline de GitHub Actions)
COPY src ./src
RUN mvn clean package -DskipTests

# ==========================================
# ETAPA 2: Imagen final liviana (Runtime)
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar solo el JAR generado desde la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto correspondiente de este microservicio
EXPOSE 8082

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]