# create docker networks
docker network create adm_videos_network
docker network create adm_videos_services
docker network create elastic

# create folders and permissions
sudo chown root app/filebeat/filebeat.docker.yml
mkdir -m 777 .docker
mkdir -m 777 .docker/keycloak
mkdir -m 777 .docker/es01
mkdir -m 777 .docker/filebeat

docker-compose -f services/docker-compose.yml up -d
docker-compose -f elk/docker-compose.yml up -d
sleep 10
#docker-compose -f app/docker-compose.yml up -d

echo "Initializing containers...."

sleep 20
