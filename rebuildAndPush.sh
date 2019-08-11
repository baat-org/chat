docker rmi $(docker images -qa 'sachingoyaldocker/baat-org-chat')

./gradlew clean build bootJar
docker build --no-cache -t sachingoyaldocker/baat-org-chat:1.0 . 
docker push sachingoyaldocker/baat-org-chat:1.0
