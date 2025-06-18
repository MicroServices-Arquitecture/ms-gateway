# Usa imagen oficial de Java 17 como base
FROM eclipse-temurin:17-jdk-alpine

# Crea directorio en la imagen
WORKDIR /app

# Copia el jar construido al contenedor
COPY target/gateway-*.jar app.jar

# Expone el puerto que usas localmente (8090)
EXPOSE 8090

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","app.jar"]