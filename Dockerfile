# --- Etapa 1: Construcción (Build) ---
# Usamos una imagen de Maven con Java 17 para compilar el proyecto
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos todo el código fuente al contenedor
COPY . .

# Compilamos el proyecto y creamos el .jar (saltando los tests para que sea más rápido en despliegue)
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución (Run) ---
# Usamos una imagen ligera de Java 17 solo para ejecutarlo
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiamos el archivo .jar generado en la etapa anterior
# El nombre 'basedato-0.0.1-SNAPSHOT.jar' sale de tu pom.xml (artifactId + version)
COPY --from=build /app/target/basedato-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080 (el default de Spring Boot)
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]