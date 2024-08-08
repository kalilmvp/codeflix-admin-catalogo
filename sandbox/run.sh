# create docker networks
docker network create adm_videos_network
docker network create adm_videos_services

# create folders and permissions
mkdir -m 777 .docker
mkdir -m 777 .docker/keycloak

docker-compose -f services/docker-compose.yml up -d
sleep 10
docker-compose -f app/docker-compose.yml up -d

echo "Initializing containers...."

sleep 20
