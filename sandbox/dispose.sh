echo "Disposing containers...."

# docker down
cd services
docker-compose down
cd ../app
docker-compose down

echo "Disposed containers...."
