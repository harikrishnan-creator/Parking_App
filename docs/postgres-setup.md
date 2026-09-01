This document explains how to run the Parking_App end-to-end locally using Docker Compose and the included Flyway migration.

Steps:

1) Start Postgres and pgAdmin

```bash
# from repo root
docker compose up -d
```

2) Wait for Postgres to become healthy (check with docker ps or docker logs)

3) Build and run the application using the postgres profile

```bash
# build
./mvnw -DskipTests package

# run with the postgres properties (Spring Boot will pick up application-postgres.properties if you activate the profile)
# Option A: pass spring.config.location
java -jar target/*.jar --spring.config.additional-location=classpath:/application-postgres.properties

# Option B: set SPRING_PROFILES_ACTIVE if you rename the file to application-postgres.yml/properties and use profiles
# export SPRING_PROFILES_ACTIVE=postgres
# ./mvnw spring-boot:run
```

4) Flyway will run automatically at application startup (spring.flyway.enabled=true). If you prefer to run migrations manually, use the Flyway CLI or set spring.flyway.enabled=false and run your SQL manually against the DB.

5) Test endpoints

```bash
curl http://localhost:8080/health
curl http://localhost:8080/api/vehicles
```

Notes and next steps:
- For production or EKS deployments use a managed Postgres (RDS/Aurora) or provide a PersistentVolumeClaim for a Postgres StatefulSet.
- Consider adding a Flyway configuration in pom.xml to run migrations as part of CI/CD, and update the application to use environment variables for secrets.
