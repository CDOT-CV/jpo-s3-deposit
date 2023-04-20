# note: must be run from the project root folder

# Make sure to set the following environment variables:
# - DOCKER_HOST_IP
# - DEPOSIT_GROUP
# - DEPOSIT_KEY_NAME
# - DEPOSIT_BUCKET_NAME
# - AWS_REGION
# - DEPOSIT_TOPIC
# - HEADER_X_API_KEY

# build if target folder does not exist
if [ ! -d "target" ]; then
	echo "Building."
	mvn clean package assembly:single
else
	echo "Target folder exists. Skipping build."
fi

# run
echo "Executing."

java -jar ./target/jpo-aws-depositor-jar-with-dependencies.jar -s $DOCKER_HOST_IP:9092 -d s3 -g $DEPOSIT_GROUP -k $DEPOSIT_KEY_NAME -b $DEPOSIT_BUCKET_NAME -r $AWS_REGION -t $DEPOSIT_TOPIC -x $HEADER_X_API_KEY