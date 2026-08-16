# Teaching Notes: Introduction to Microservices

## 1. Introduction to Monolithic Architecture
**Definition & Characteristics:**
- A monolithic application is built as a single unified unit. Typically, an enterprise application consists of a client-side user interface, a server-side application, and a database.
- Tightly coupled components.
- Single codebase that encompasses all business logic, data access, and user interfaces.
- Deployed as a single artifact (e.g., WAR/EAR file in Java).

**Architecture Components:**
- Presentation Layer (UI)
- Business Logic Layer
- Data Access Layer
- Underlying Single Database

**Drawbacks:**
- **Complexity and Size:** As the application grows, it becomes too large and complex to fully understand, leading to longer development cycles.
- **Slow Startup/Deployment:** The entire application must be rebuilt and redeployed for any minor change.
- **Scaling Issues:** Cannot scale individual components. If one module requires more CPU, the whole application must be scaled.
- **Technology Lock-in:** Difficult to adopt new technologies, frameworks, or languages once the monolith is established.

## 2. Introduction to Microservices
**Definition & Characteristics:**
- Microservices Architecture (MSA) structures an application as a collection of loosely coupled, independently deployable services.
- Each service corresponds to a specific business capability.
- Services communicate over a network (typically HTTP/REST, gRPC, or message brokers).
- Decentralized data management (often each service has its own database).

**Architecture Components:**
- API Gateway (Entry point for clients)
- Independent Microservices (e.g., User Service, Product Service, Order Service)
- Distributed Databases (Polyglot persistence)
- Service Registry & Discovery
- Centralized Logging & Monitoring

**Benefits:**
- **Independent Deployment:** Services can be updated and deployed without affecting the rest of the system.
- **Targeted Scaling:** Only services facing high load need to be scaled.
- **Technology Diversity:** Different services can be written in different programming languages and use different databases best suited for their task.
- **Improved Fault Isolation:** A failure in one service doesn't necessarily bring down the entire application.

**Drawbacks:**
- **Complexity of Distributed Systems:** Network latency, handling partial failures, and complex inter-service communication.
- **Data Consistency:** Managing distributed transactions (e.g., using Saga pattern) is much harder than ACID transactions in a monolithic DB.
- **Operational Overhead:** Requires robust DevOps, CI/CD pipelines, containerization (Docker, Kubernetes), and monitoring tools.
- **Testing Challenges:** End-to-end testing becomes complicated.

## 3. Monolithic VS Microservice Architecture
| Feature | Monolithic Architecture | Microservice Architecture |
| :--- | :--- | :--- |
| **Codebase** | Single, large repository | Multiple, smaller repositories |
| **Deployment** | Single unit deployment | Independent service deployments |
| **Scaling** | Scale the entire application (X-axis) | Scale specific services independently (X, Y, Z-axis) |
| **Technology** | Bound to a single technology stack | Polyglot (mix of technologies allowed) |
| **Fault Tolerance** | A bug can crash the entire app | Failure is localized to a specific service |
| **Data Management** | Centralized, shared database | Decentralized, per-service databases |
| **Ideal For** | Small teams, simple applications, MVP | Large teams, complex, evolving, highly scalable apps |
