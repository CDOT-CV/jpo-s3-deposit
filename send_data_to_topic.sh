# if DOCKER_HOST_IP is not set, exit
if [ -z "$DOCKER_HOST_IP" ]; then
    echo "DOCKER_HOST_IP is not set. Exiting."
    exit 1
elif [ -z "$DEPOSIT_TOPIC" ]; then
    echo "DEPOSIT_TOPIC is not set. Exiting."
    exit 1
fi

# check if kafkacat is installed
if ! command -v kafkacat &> /dev/null
then
    # install kafkacat
    echo "kafkacat could not be found. Installing kafkacat."
    sudo apt-get update
    sudo apt-get install -y kafkacat
fi

# send "test" message to topic
echo "Sending test message to topic $DEPOSIT_TOPIC."
kafkacat -b $DOCKER_HOST_IP:9092 -t $DEPOSIT_TOPIC -P -p 0 -K: <<< "test: test"