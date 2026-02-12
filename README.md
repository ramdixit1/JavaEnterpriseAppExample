# Banking Application - Java Enterprise + React (On-Prem)

This repository contains a **basic banking microservices application** using:
- **Java 17**
- **Spring Boot + Spring (MVC, Data JPA, Validation, Actuator)**
- **Hibernate (via Spring Data JPA)**
- **Maven (multi-module)**
- **Microservices architecture**
- **React frontend (Vite)**
- **Docker + Kubernetes**
- **JFrog Artifactory integration points**
- **Git-friendly project structure**

## Why Tomcat (instead of WebSphere)?
For this starter implementation, we use **embedded Tomcat** (default in Spring Boot) because it is:
1. Easiest to run and learn.
2. Very common in many tech companies.
3. Works well for modern containerized microservices.

You can still deploy generated JARs on-prem and orchestrate with Kubernetes.

## Architecture

```text
React Frontend (Vite)
   |
   v
Gateway Service (Spring Cloud Gateway)
   |------------------------|
   v                        v
Account Service          Transaction Service
   ^                        |
   |________________________|
    balance updates via REST
```

- **gateway-service** exposes one backend entry point for the frontend.
- **account-service** manages account records and balances.
- **transaction-service** saves debit/credit operations and triggers account balance updates.

## Folder-by-folder guide

### `/backend`
Maven parent and all Java microservices.

#### `/backend/pom.xml`
Parent Maven file that:
- Defines all modules.
- Imports Spring Boot and Spring Cloud BOMs.
- Configures Java version.
- Adds `distributionManagement` placeholders for JFrog Artifactory.

#### `/backend/common-lib`
Shared DTO contracts used by backend services.
- `AccountDtos.java` contains account API request/response models.
- `TransactionDtos.java` contains transaction API request/response models.

#### `/backend/account-service`
Microservice for account lifecycle.
- `AccountServiceApplication.java`: app entry point.
- `entity/Account.java`: Hibernate entity mapped to `accounts` table.
- `repository/AccountRepository.java`: database access.
- `service/AccountService.java`: business logic (create/list/update balance).
- `controller/AccountController.java`: REST endpoints `/api/accounts`.
- `config/ApiExceptionHandler.java`: maps validation/business errors to HTTP 400.
- `application.yml`: port `8081`, H2 DB, JPA and actuator setup.

#### `/backend/transaction-service`
Microservice for transaction workflows.
- `TransactionServiceApplication.java`: app entry point.
- `entity/BankTransaction.java`: Hibernate entity for transactions.
- `repository/TransactionRepository.java`: CRUD + lookup by account.
- `client/AccountServiceClient.java`: REST call to account-service balance endpoint.
- `service/TransactionService.java`: save transaction + call account-service.
- `controller/TransactionController.java`: endpoints `/api/transactions`.
- `config/HttpConfig.java`: RestTemplate bean.
- `config/ApiExceptionHandler.java`: error handling.
- `application.yml`: port `8082`, H2 DB, account-service URL.

#### `/backend/gateway-service`
Single ingress for frontend.
- `GatewayServiceApplication.java`: app entry point.
- `application.yml`: route rules:
  - `/accounts/**` -> account-service `/api/accounts/**`
  - `/transactions/**` -> transaction-service `/api/transactions/**`

### `/frontend`
React UI for creating accounts and posting transactions.
- `src/App.jsx`: orchestrates API calls and state.
- `src/components/AccountForm.jsx`: create account form.
- `src/components/TransactionForm.jsx`: create debit/credit transaction.
- `src/components/AccountList.jsx`: account listing + selection.
- `src/components/TransactionList.jsx`: statement view.
- `src/services/api.js`: Axios API wrapper.
- `src/styles.css`: basic dashboard style.
- `vite.config.js`: dev proxy to gateway (`localhost:8080`).

### `/infra/docker`
Dockerfiles for all deployable units.
- `Dockerfile.account-service`
- `Dockerfile.transaction-service`
- `Dockerfile.gateway-service`
- `Dockerfile.frontend`

### `/infra/k8s`
Kubernetes manifests for on-prem cluster deployment.
- `banking-platform.yaml`: namespace, deployments, and services.

### `/docs`
Additional setup guide for Artifactory + CI/CD flow.

## File-by-file flow (request lifecycle)
1. User creates transaction in `frontend/src/components/TransactionForm.jsx`.
2. `frontend/src/App.jsx` calls `createTransaction` from `frontend/src/services/api.js`.
3. Request reaches `gateway-service` route `/transactions/**`.
4. Gateway rewrites path to transaction-service `/api/transactions`.
5. `TransactionController` calls `TransactionService#createTransaction`.
6. `TransactionService` saves transaction in DB via `TransactionRepository`.
7. `TransactionService` calls `AccountServiceClient#updateBalance`.
8. `AccountServiceClient` hits account-service `/api/accounts/balance`.
9. `AccountController` delegates to `AccountService#updateBalance`.
10. Account balance is validated and updated with Hibernate/JPA.

## Run locally

### Backend
```bash
cd backend
mvn clean package

# run in separate terminals
mvn -pl account-service spring-boot:run
mvn -pl transaction-service spring-boot:run
mvn -pl gateway-service spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

## JFrog Artifactory integration (on-prem)
1. Update `backend/pom.xml` distributionManagement URLs.
2. Add credentials in `~/.m2/settings.xml` server entries:
```xml
<servers>
  <server>
    <id>artifactory-releases</id>
    <username>YOUR_USER</username>
    <password>YOUR_PASSWORD_OR_TOKEN</password>
  </server>
  <server>
    <id>artifactory-snapshots</id>
    <username>YOUR_USER</username>
    <password>YOUR_PASSWORD_OR_TOKEN</password>
  </server>
</servers>
```
3. Publish artifacts:
```bash
cd backend
mvn clean deploy
```

## Docker build examples
```bash
docker build -f infra/docker/Dockerfile.account-service -t account-service:1.0.0 .
docker build -f infra/docker/Dockerfile.transaction-service -t transaction-service:1.0.0 .
docker build -f infra/docker/Dockerfile.gateway-service -t gateway-service:1.0.0 .
docker build -f infra/docker/Dockerfile.frontend -t banking-frontend:1.0.0 .
```

## Kubernetes apply
```bash
kubectl apply -f infra/k8s/banking-platform.yaml
```
