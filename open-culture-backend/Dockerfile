FROM maven:3.8.1-openjdk-11-slim
WORKDIR /app
COPY . .
RUN mvn package -DskipTests
RUN	mv target/openculture-0.0.1-SNAPSHOT.jar /run/openculture.jar
CMD java -jar /run/openculture.jar