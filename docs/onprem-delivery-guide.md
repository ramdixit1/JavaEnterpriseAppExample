# On-Prem Delivery Guide (Git + Maven + JFrog + Docker + Kubernetes)

## 1) Git workflow
1. Create feature branch from `main`.
2. Implement and test locally.
3. Commit using meaningful messages.
4. Open PR for peer review.
5. Merge to `main`.

## 2) Maven build and unit test stage
- Run `mvn clean verify` from `/backend`.
- Generated JAR artifacts are versioned and ready for publishing.

## 3) JFrog Artifactory publish stage
- CI runner authenticates with Artifactory.
- `mvn deploy` uploads all module artifacts to Maven repositories.
- Docker images can also be pushed to Artifactory Docker registry.

## 4) Docker image stage
- Build image for each service from `/infra/docker` Dockerfiles.
- Tag with semantic version or Git SHA.
- Push image to Artifactory Docker registry.

## 5) Kubernetes deploy stage (on-prem)
- Pull images from internal registry.
- Apply `/infra/k8s/banking-platform.yaml`.
- Validate with `kubectl get pods -n banking-platform`.

## 6) Operations
- Health endpoints available via Spring Actuator.
- Add logging/monitoring stack (ELK/Prometheus/Grafana) in next iteration.
