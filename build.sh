cd ui
yarn build
cd ..
./mvnw package -DskipTests 
docker build -f src/main/docker/dockerfile.jvm -t jbcodeforce/person-manager:v0.1.0 .
