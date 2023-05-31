# !/bin/bash

# get name of project folder
projectFolder=$(pwd | awk -F/ '{print $NF}')
KAFKA_CONTAINER_NAME="$projectFolder-kafka-1"
echo "KAFKA_CONTAINER_NAME: $KAFKA_CONTAINER_NAME"

setup() {
    if [ -z $DOCKER_HOST_IP ]; then
        echo "DOCKER_HOST_IP is not set. Exiting."
        exit 1
    fi

    ./start_kafka.sh
}

waitForKafkaToCreateTopics() {
    while true; do
        if [ $(docker ps | grep $KAFKA_CONTAINER_NAME | wc -l) == "0" ]; then
            echo "Kafka container '$KAFKA_CONTAINER_NAME' is not running. Exiting."
            ./stop_kafka.sh
            exit 1
        fi

        ltopics=$(docker exec -it $KAFKA_CONTAINER_NAME /opt/kafka/bin/kafka-topics.sh --list --zookeeper 172.17.0.1)
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
    # execute run.sh as daemon
    ./run.sh &

    # wait for S3D to start
    sleep 10

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
    echo ""
    echo "Step 7/$numSteps - Pushing test data to Kafka."
    ./send_data_to_topic.sh
}

cleanup() {
    ./stop_kafka.sh
    
    # kill S3D
    echo "Killing S3D with pid $pid."
    kill $pid
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