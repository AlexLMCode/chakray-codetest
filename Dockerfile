FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Instalar Maven 3.6.3 específicamente
RUN apt-get update && apt-get install -y wget && \
    wget https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.6.3/apache-maven-3.6.3-bin.tar.gz && \
    tar -xzf apache-maven-3.6.3-bin.tar.gz && \
    mv apache-maven-3.6.3 /opt/maven && \
    rm apache-maven-3.6.3-bin.tar.gz

ENV PATH="/opt/maven/bin:$PATH"
ENV MAVEN_HOME="/opt/maven"

COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]