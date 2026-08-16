# Teaching Notes: Microservice Design Patterns

Building a robust microservices architecture requires solving common distributed system problems. Various design patterns have emerged to address these challenges effectively.

## 1. API Gateway Pattern
**Problem:** In a microservices architecture, a client app needs to consume multiple services. Direct client-to-microservice communication leads to a chatty interface, tight coupling, and security issues.
**Solution:** Introduce an API Gateway that acts as a single entry point for all clients.
**Features:**
- **Request Routing:** Routes requests to the appropriate backend microservice.
- **API Composition:** Can invoke multiple microservices and aggregate the results for the client.
- **Cross-cutting Concerns:** Handles authentication, rate limiting, logging, and SSL termination.

## 2. Database per Service Pattern
**Problem:** How to manage data effectively in a microservices environment while ensuring services remain loosely coupled.
**Solution:** Keep each microservice's persistent data private to that service and accessible only via its API. 
**Benefits:** Ensures loose coupling and enables Polyglot Persistence (using different databases for different services).
**Challenges:** Implementing business transactions that span multiple services becomes complex.

## 3. Saga Pattern
**Problem:** Since each service has its own database, traditional distributed transactions (two-phase commit) are not viable. How do we ensure data consistency across multiple services?
**Solution:** Implement a Saga. A saga is a sequence of local transactions. Each local transaction updates the database and publishes a message or event to trigger the next local transaction in the saga.
**Types:**
- **Choreography:** Services publish and listen to events without a central coordinator.
- **Orchestration:** A centralized "orchestrator" service tells the participants what local transactions to execute.
**Compensating Transactions:** If a local transaction fails, the saga executes compensating transactions to undo the changes made by preceding local transactions.

## 4. Circuit Breaker Pattern
**Problem:** In a distributed system, service invocations can fail due to network issues or slow downstream services. Repeatedly trying a failing service can consume valuable resources (like threads) and lead to cascading failures.
**Solution:** Wrap the remote service call in a Circuit Breaker object.
**States:**
- **Closed:** Requests flow normally. If failures cross a threshold, the circuit opens.
- **Open:** Requests fail immediately without making the remote call, allowing the failing service to recover.
- **Half-Open:** After a timeout, a limited number of test requests are allowed. If successful, the circuit closes; if not, it re-opens.

## 5. Command Query Responsibility Segregation (CQRS)
**Problem:** In some domains, the data models used for reading data (Queries) are significantly different from the models used for updating data (Commands). Using a single model can lead to complex and slow queries.
**Solution:** Segregate the application into two parts: one for handling commands (writes) and one for handling queries (reads). This often involves having separate databases for reads and writes, optimized for their specific tasks.

## 6. Event Sourcing Pattern
**Problem:** Traditional databases store the current state of an entity. It's difficult to reliably publish events whenever state changes without complex transaction management.
**Solution:** Store the state of a business entity as a sequence of state-changing events. Whenever the state changes, a new event is appended to the list of events. To reconstruct the current state, the application replays the events. It often works hand-in-hand with CQRS.

## 7. Strangler Fig Pattern
**Problem:** How do you migrate from a legacy monolithic application to a microservices architecture without halting new feature development or risking a massive "big bang" rewrite?
**Solution:** Gradually replace specific pieces of functionality with new applications and services. The new architecture slowly "strangles" the old system until the legacy system can be safely decommissioned.
