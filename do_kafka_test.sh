# !/bin/bash

# This script is used to test the S3D application.
# It starts Kafka, S3D, and pushes test data to Kafka.
# It then kills S3D and Kafka.

# Make sure to set the following environment variables:
# - DOCKER_HOST_IP
# - DEPOSIT_GROUP
# - DEPOSIT_KEY_NAME
# - DEPOSIT_BUCKET_NAME
# - AWS_REGION
# - DEPOSIT_TOPIC
# - HEADER_X_API_KEY

setup() {
    # if any of the required environment variables are not set, exit
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

    useEndpoint=false

    # if API_ENDPOINT is set, set useEndpoint to true
    if [ ! -z "$API_ENDPOINT" ]; then
        useEndpoint=true
        echo "API_ENDPOINT is set. Using API_ENDPOINT."
    fi

    projectFolder=$(pwd | awk -F/ '{print $NF}')
    EXPECTED_KAFKA_CONTAINER_NAME="$projectFolder-kafka-1"
    echo "EXPECTED_KAFKA_CONTAINER_NAME: $EXPECTED_KAFKA_CONTAINER_NAME"

    # build
    echo "Building."
    mvn clean package assembly:single

    ./start_kafka.sh
}

waitForKafkaToCreateTopics() {
    while true; do
        if [ $(docker ps | grep $EXPECTED_KAFKA_CONTAINER_NAME | wc -l) == "0" ]; then
            echo "Kafka container '$EXPECTED_KAFKA_CONTAINER_NAME' is not running. Exiting."
            ./stop_kafka.sh
            exit 1
        fi

        ltopics=$(docker exec -it $EXPECTED_KAFKA_CONTAINER_NAME /opt/kafka/bin/kafka-topics.sh --list --zookeeper 172.17.0.1)
        allTopicsCreated=true
        if [ $(echo $ltopics | grep "topic.OdeBsmJson" | wc -l) == "0" ]; then
            allTopicsCreated=false
        fi
        
        if [ $allTopicsCreated == true ]; then
            echo "Kafka has created all required topics"
            break
        fi

        sleep 1
    done
}

runS3D() {
    echo "Starting S3D."

    if [ $useEndpoint == false ]; then
        # execute run.sh as daemon
        ./run.sh &
    else
        # execute run_with_endpoint.sh as daemon
        ./run_with_endpoint.sh &
    fi

    # wait for S3D to start
    echo "Sleeping for 15 seconds."
    sleep 15

    # use pidof to get pid of S3D
    pid=$(pidof java | awk '{print $1}')
    echo "S3D started with pid $pid."

    # if S3D is not running, exit
    if [ -z "$(docker ps | grep s3)" ]; then
        echo "S3D is not running. Exiting."
        exit 1
    fi
}

pushTestDataToKafka() {
    ./send_data_to_topic.sh
    echo "Sleeping for 5 seconds."
    sleep 5
}

cleanup() {
    # kill S3D if pid is not empty
    if [ ! -z "$pid" ]; then
        echo "Killing S3D with pid $pid."
        kill $pid
    fi

    ./stop_kafka.sh
}

run() {
    numSteps=5
    
    echo ""
    echo "Step 1/$numSteps - Setting up."
    setup

    echo ""
    echo "Step 2/$numSteps - Waiting for Kafka to create topics."
    waitForKafkaToCreateTopics

    echo ""
    echo "Step 3/$numSteps - Running S3D."
    runS3D

    echo ""
    echo "Step 4/$numSteps - Pushing test data to Kafka."
    pushTestDataToKafka

    echo ""
    echo "Step 5/$numSteps - Cleaning up."
    cleanup
}

run
