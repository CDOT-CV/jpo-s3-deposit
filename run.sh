# note: must be run from the project root folder

# Kafka should be running on the host machine

# Make sure to set the following environment variables:
# - DOCKER_HOST_IP
# - DEPOSIT_GROUP
# - DEPOSIT_KEY_NAME
# - DEPOSIT_BUCKET_NAME
# - AWS_REGION
# - DEPOSIT_TOPIC
# - HEADER_X_API_KEY

# if any of the above are not set, exit
if [ -z "$DOCKER_HOST_IP" ]; then
	echo "DOCKER_HOST_IP is not set. Exiting."
	exit 1
elif [ -z "$DEPOSIT_GROUP" ]; then
	echo "DEPOSIT_GROUP is not set. Exiting."
	exit 1
elif [ -z "$DEPOSIT_KEY_NAME" ]; then
	echo "DEPOSIT_KEY_NAME is not set. Exiting."
	exit 1
elif [ -z "$DEPOSIT_BUCKET_NAME" ]; then
	echo "DEPOSIT_BUCKET_NAME is not set. Exiting."
	exit 1
elif [ -z "$AWS_REGION" ]; then
	echo "AWS_REGION is not set. Exiting."
	exit 1
elif [ -z "$DEPOSIT_TOPIC" ]; then
	echo "DEPOSIT_TOPIC is not set. Exiting."
	exit 1
elif [ -z "$HEADER_X_API_KEY" ]; then
	echo "HEADER_X_API_KEY is not set. Exiting."
	exit 1
fi

# build if target folder does not exist
if [ ! -d "target" ]; then
	echo "Building."
	mvn clean package assembly:single
else
	echo "Target folder exists. Skipping build."
fi

# run
echo "Executing."

java -jar ./target/jpo-aws-depositor-jar-with-dependencies.jar -s $DOCKER_HOST_IP:9092 \
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