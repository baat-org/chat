# chat
Chat Service


```
export SERVICE=chat &&
export VERSION=1.0 &&

./gradlew clean build bootJar &&

docker build --no-cache -t sachingoyaldocker/baat-org-${SERVICE}:${VERSION} . && 

docker push sachingoyaldocker/baat-org-${SERVICE}:${VERSION}
```
