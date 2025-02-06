# Construção da aplicação:
FROM openjdk:21-jdk-slim AS build
# Definição do diretório de trabalho dentro do container:
WORKDIR /app
# Copiando os arquivos do projeto para o diretório de trabalho:
COPY . /app
# Definindo a variável de ambiente para habilitar o Gradle no modo "offline":
RUN ./gradlew build --no-daemon

# Imagem de execução:
FROM openjdk:21-jdk-slim
# Definir o diretório de trabalho no container para a aplicação:
WORKDIR /app
# Copiando o arquivo JAR gerado na etapa de construção para o container:
COPY --from=build /app/build/libs/*.jar /app/app.jar
# Expondo a porta 8080 no container:
EXPOSE 8080
# Comando para rodar o aplicativo no container
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
