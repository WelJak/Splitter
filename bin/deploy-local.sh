cd ..
gradle clean build
docker-compose -f local-infrastructure/docker-compose.yml build --force-rm
docker-compose -f local-infrastructure/docker-compose.yml up -d