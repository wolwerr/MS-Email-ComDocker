# Primeira etapa: Construir a aplicação
FROM maven:3.9.5-amazoncorretto-17 AS build

WORKDIR /workspace

# Copie o pom.xml e baixe as dependências, isso melhora o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copie o código fonte e construa o JAR
COPY src src
ARG MAVEN_SKIP_TEST=false
RUN if [ "$MAVEN_SKIP_TEST" = "true" ] ; then mvn clean package -DskipTests ; else mvn clean package ; fi

# Segunda etapa: Rodar a aplicação
FROM amazoncorretto:17

LABEL maintainer="ricardo@ricardo.net"
LABEL version="1.0"
LABEL description="Testes Aula FIAP"
LABEL name="Aula FIAP"

EXPOSE 8080

# Copie o JAR da primeira etapa
COPY --from=build /workspace/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]