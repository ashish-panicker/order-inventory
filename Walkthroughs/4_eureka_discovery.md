# Walkthrough: Eureka Discovery Server and Clients

In a microservices architecture, services need a way to find each other dynamically without hardcoding IP addresses. Netflix Eureka is a Service Registry that handles this.

This walkthrough covers setting up the Eureka Server and configuring existing microservices to register as Eureka Clients.

## Part 1: Setting up the Eureka Server

### 1. Create the Server Project
Generate a new Spring Boot project (e.g., `discovery-service`) and add the Eureka Server dependency in `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

### 2. Enable Eureka Server
Open your main Application class and add the `@EnableEurekaServer` annotation:

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServiceApplication.class, args);
    }
}
```

### 3. Configure the Server (`application.yml`)
Configure the server so it doesn't try to register itself as a client (since it is a standalone registry).

```yaml
server:
  port: 8761 # Standard Eureka port

spring:
  application:
    name: discovery-service

eureka:
  client:
    register-with-eureka: false # Don't register itself
    fetch-registry: false       # Don't fetch registry from itself
```
Run the application and navigate to `http://localhost:8761`. You will see the Eureka dashboard.

---

## Part 2: Enabling Existing Services as Eureka Clients

### 1. Add the Dependency
In the `pom.xml` of your existing microservices (e.g., `order-service` or `inventory-service`), add the Eureka Client dependency:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### 2. Configure the Client (`application.yml`)
Tell the client where the Eureka server is located and provide an application name (which will be used by other services to discover it).

```yaml
spring:
  application:
    name: order-service # Name registered in Eureka

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/ # URL of the Eureka server
  instance:
    prefer-ip-address: true # Prefer IP over hostname
```

*(Note: The `@EnableDiscoveryClient` annotation on the main class is no longer strictly required in newer Spring Cloud versions as long as the dependency is present, but it's good practice for clarity).*

### 3. Verify Registration
Start your client service, wait a few seconds, and refresh the Eureka dashboard (`http://localhost:8761`). You should see `ORDER-SERVICE` registered under the "Instances currently registered with Eureka" section.

### 4. Making Inter-Service Calls
Now, instead of hardcoding `http://localhost:8080/api/inventory` when using `RestTemplate` or `WebClient`, you can use the service name:
`http://INVENTORY-SERVICE/api/inventory`. Spring Cloud will automatically intercept the call, lookup the IP/port in Eureka, and perform client-side load balancing.
