# Parking App — Full-stack (Backend, Frontend, DB) — Run Guide

This repository contains a Spring Boot backend, a simple accessible frontend (served from Spring), and Kubernetes + CI/CD artifacts to build, publish, and deploy the application.

This README describes how to run the entire application locally for development and also how to build/push the Docker image and deploy to Kubernetes (EKS or any cluster).

---

## Prerequisites

- Java 25 (the project POM targets Java 25). If you don't have JDK 25, either install it or change the pom/workflows to a supported Java version.
- Maven (or the included `./mvnw` wrapper)
- Docker (for local DB or building images)
- kubectl (for applying the k8 manifest)
- (Optional) GitHub CLI (for creating PRs / workflows)

---

## Profiles and Configuration

The application uses Spring profiles. By default `application.yml` sets `spring.profiles.active=dev` which points to the dev datasource in `application-dev.yml`.

- `application-dev.yml` (default) currently points at a remote RDS instance.
- `application-postgres.properties` is included for a local Postgres setup and enables Flyway (note: Flyway expects migrations under `src/main/resources/db/migration`).

You can override datasource configuration at runtime with environment variables (recommended for local testing):

- SPRING_DATASOURCE_URL (e.g. jdbc:postgresql://localhost:5432/parkingdb)
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD

You can also set `SPRING_PROFILES_ACTIVE` to pick a profile (`dev`, `postgres`, `prod`, ...).

---

## Local quick-start (recommended for development)

1. Start a local Postgres with Docker:

   docker run --name parking-postgres -e POSTGRES_USER=parking_user -e POSTGRES_PASSWORD=parking_pass -e POSTGRES_DB=parkingdb -p 5432:5432 -d postgres:15

2. Export environment variables so Spring Boot connects to the local DB and does not try to use the remote RDS:

   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/parkingdb
   export SPRING_DATASOURCE_USERNAME=parking_user
   export SPRING_DATASOURCE_PASSWORD=parking_pass

   (Optionally) export SPRING_PROFILES_ACTIVE=dev

   NOTE: The `dev` profile uses `spring.jpa.hibernate.ddl-auto=update` and `spring.sql.init.mode=always`, so `schema.sql` will be executed and the `reservations` table will be created automatically for development.

3. Build and run the application:

   ./mvnw clean package -DskipTests
   java -jar target/*.jar

   Or use the Maven boot plugin:

   ./mvnw spring-boot:run

4. Open the frontend served by Spring Boot:

   http://localhost:8080/

   Use the UI to create, list, and delete reservations. These operations call the backend endpoints under `/api/reservations` and persist data to Postgres.

5. Verify DB contents (optional):

   docker exec -it parking-postgres psql -U parking_user -d parkingdb
   SELECT * FROM reservations;

---

## Docker image build & publish (CI / locally)

A `Dockerfile` is provided that performs a multi-stage build (Maven build stage + Temurin JRE runtime stage).

Build locally:

   docker build -t parking-app:local .

Run locally:

   docker run --rm -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/parkingdb -e SPRING_DATASOURCE_USERNAME=parking_user -e SPRING_DATASOURCE_PASSWORD=parking_pass parking-app:local

CI automation

- A GitHub Actions workflow (`.github/workflows/docker-publish.yml`) is provided to build and push the image to GitHub Container Registry (`ghcr.io/${{ github.repository_owner }}/parking-app`). Pushes to `main` trigger the publish workflow.

Make sure repository/package permissions allow the workflow to publish images.

---

## Kubernetes deployment (EKS / any k8s)

A manifest is available at `k8/parking-app-manifest.yaml`. It includes:
- Namespace `parking-app`
- Secret template for DB credentials (placeholder values)
- ConfigMap for profile
- Deployment (2 replicas) with liveness/readiness probes
- Service and an example Ingress

Before applying:
1. Create a Kubernetes Secret with your DB connection details (replace placeholders):

   kubectl create secret generic parking-db-secret \
     --from-literal=SPRING_DATASOURCE_URL=jdbc:postgresql://<DB_HOST>:5432/parkingdb \
     --from-literal=SPRING_DATASOURCE_USERNAME=parking_user \
     --from-literal=SPRING_DATASOURCE_PASSWORD=parking_pass \
     -n parking-app

2. (Optional) Adjust `SPRING_PROFILES_ACTIVE` in the ConfigMap or use a different mechanism (ConfigMap/Secrets) for configuration.

3. Update the `image` field in the Deployment to the image you published (e.g., `ghcr.io/your-org/parking-app:tag`) or use the GitHub Actions Dispatch workflow to pass the image tag.

Apply manifest:

   kubectl apply -f k8/parking-app-manifest.yaml

Notes on Ingress / TLS
- The manifest includes an example Ingress. Replace `parking.example.com` and `parking-app-tls` with your actual DNS and TLS secret.
- Ensure your cluster has an Ingress controller (nginx, ALB Ingress Controller for AWS, etc.)

---

## GitHub Actions workflows

Three workflows are included:
- `.github/workflows/ci.yml` — runs Maven build+tests on push/PR to `main`.
- `.github/workflows/docker-publish.yml` — builds and pushes Docker image to GHCR on push to `main`.
- `.github/workflows/deploy-to-k8s.yml` — manual dispatch workflow that requires a `KUBE_CONFIG` secret (base64-encoded kubeconfig). Use it to apply the k8 manifest to your cluster by providing the built image tag.

Important: Add repo secret `KUBE_CONFIG` (base64-encoded kubeconfig) in Settings → Secrets to enable deployment.

---

## Security notes

- For quick demo/development the reservations endpoints are permitted without auth. In production you should secure them (JWT or session auth) and update the frontend to authenticate and attach tokens to API requests.
- The codebase contains JWT-based auth pieces; if you enable auth for `api/reservations`, update the `script.js` to call your `/api/auth` login and send `Authorization: Bearer <token>` with requests.

---

## DB schema & migrations

- `src/main/resources/schema.sql` currently includes schema for existing tables and the new `reservations` table. For production use prefer Flyway migrations placed under `src/main/resources/db/migration` (the `application-postgres.properties` profile enables Flyway).

---

## Troubleshooting

- Application fails to start with DB errors: check that the DB host is reachable and credentials are correct. For local dev, ensure the Docker Postgres container is running and ports are open.
- Java version mismatch: make sure your environment supports Java 25 (or change pom/workflow to a lower version).
- Container fails to start in k8: check logs with `kubectl logs` and describe the pod; confirm the secret values are present and correct.

---

## Recommended next steps

- Add Flyway migrations (if you plan to use `application-postgres.properties` / Flyway in CI/prod).
- Add a Dockerfile multi-stage build optimization (already included) and configure image tags for stable releases.
- Secure endpoints and add a small login flow in the frontend.
- Create a small `docker-compose.yml` for local multi-container testing (app + db) if helpful.


If you want, I can add a docker-compose and a Flyway migration file, or update the CI workflows to create releases/tags automatically — tell me which you prefer and I will add them.
