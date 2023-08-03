1. run docker-compose

```bash
docker-compose -f docker-compose.yml up -d
```

2. build lambda artifact (from core-java directory)

```bash
./gradlew build
```

3. publish zip to s3

```bash
 aws s3 cp build/distributions/core-java-1.0-SNAPSHOT.zip s3://lambda/core-java-1.0-SNAPSHOT.zip --endpoint-url http://localhost:4566
```

4. deploy terraform

```bash
tflocal init
tflocal apply -auto-approve
```

5. sun spring application

6. execute http request (change <REST-ID> to id in tf output)

```bash
curl --location 'https://<REST-ID>.execute-api.localhost.localstack.cloud:4566/person' \
--header 'Content-Type: application/json' \
--data '{
    "id" : "1",
    "firstName" : "FIRST_NAME",
    "lastName" : "LAST_NAME"
}
'
```