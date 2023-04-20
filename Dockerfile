FROM maven:3.5.4-jdk-8-alpine as builder
MAINTAINER 583114@bah.com

WORKDIR /home
COPY ./pom.xml .
COPY ./src ./src

RUN mvn clean package assembly:single

FROM eclipse-temurin:11-jre-alpine

COPY --from=builder /home/src/main/resources/logback.xml /home
COPY --from=builder /home/target/jpo-aws-depositor-jar-with-dependencies.jar /home

CMD java -Dlogback.configurationFile=/home/logback.xml \
	-jar /home/jpo-aws-depositor-jar-with-dependencies.jar \
	-s $DOCKER_HOST_IP:9092 \
	-d s3 \
	-g $DEPOSIT_GROUP \
	-k $DEPOSIT_KEY_NAME \
	-b $DEPOSIT_BUCKET_NAME \
	-r $AWS_REGION \
	-t $DEPOSIT_TOPIC \
	-x $HEADER_X_API_KEY \