# 🔹 Etapa 1 — Build da aplicação
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# 🔹 Etapa 2 — Imagem final (mais leve)
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copia o JAR gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Porta padrão do Spring Boot
EXPOSE 8080

# Comando de execução
ENTRYPOINT ["java","-jar","app.jar"]
