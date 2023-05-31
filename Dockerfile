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
	-h $HEADER_ACCEPT \
	-x $HEADER_X_API_KEY \
	-b $DEPOSIT_BUCKET_NAME \
	-r $AWS_REGION \
	-k $DEPOSIT_KEY_NAME \
	-t $DEPOSIT_TOPIC \
	-g $DEPOSIT_GROUP \
	-i $AWS_ACCESS_KEY_ID \
	-a $AWS_SECRET_ACCESS_KEY \
	-n $AWS_SESSION_TOKEN \
	-e $AWS_EXPIRATION \
	-d s3