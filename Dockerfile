# Etapa 1: Build
FROM maven:3.9.9-eclipse-temurin-24-alpine AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto para o container
COPY pom.xml .
COPY src ./src

# Executa o build do projeto
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:24-jre

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR gerado na etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]