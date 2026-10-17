# Etapa 1: compilación con Maven
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY .mvn .mvn
COPY mvnw mvnw.cmd pom.xml ./
COPY src src

RUN chmod +x ./mvnw && ./mvnw -B clean package -DskipTests

# Etapa 2: ejecución
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# Render proporciona PORT. 8080 se usa como valor local por defecto.
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]
