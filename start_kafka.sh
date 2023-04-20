# if DOCKER_HOST_IP is not set, exit
if [ -z "$DOCKER_HOST_IP" ]; then
    echo "DOCKER_HOST_IP is not set. Exiting."
    exit 1
fi

echo "Starting Kafka."
docker-compose -f docker-compose-kafka.yml up --remove-orphans