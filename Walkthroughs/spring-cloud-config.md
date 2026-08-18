# Walkthrough: Setting up Spring Cloud Config Server and Client with Git

This guide walks you through enabling a Spring Cloud Config Server, mapping it to a Git repository, and setting up a microservice as a Config Client to fetch its configuration.

## Part 1: Setting up the Config Server

1. **Add Dependencies:**
   Create a new Spring Boot application and add the following dependency in your `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-config-server</artifactId>
   </dependency>
   ```

2. **Enable the Config Server:**
   Open your main application class (e.g., `ConfigServiceApplication.java`) and add the `@EnableConfigServer` annotation.
   ```java
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.cloud.config.server.EnableConfigServer;

   @SpringBootApplication
   @EnableConfigServer
   public class ConfigServiceApplication {
       public static void main(String[] args) {
           SpringApplication.run(ConfigServiceApplication.class, args);
       }
   }
   ```

3. **Configure the Git Repository:**
   In the Config Server's `src/main/resources/application.properties`, define the server port and map the Git repository.
   ```properties
   spring.application.name=config-service
   server.port=8888

   # Map to your Git repository
   spring.cloud.config.server.git.uri=https://github.com/your-username/your-config-repo.git
   
   # Use environment variables for secure credentials
   spring.cloud.config.server.git.username=${GIT_USERNAME}
   spring.cloud.config.server.git.password=${GIT_PASSWORD}
   
   spring.cloud.config.server.git.default-label=main
   ```

4. **Run the Server:**
   Start the Config Server. It will now listen on port `8888` and serve configurations fetched from the specified Git repository.

---

## Part 2: Creating the Git Configuration Repository

1. Create a new Git repository (e.g., `order-inventory-config`).
2. Add property files for your microservices. The naming convention is usually `{application-name}-{profile}.properties`.
   For example, create a file named `order-service.properties`:
   ```properties
   message.greeting=Hello from the central Config Server!
   ```
3. Commit and push this file to the `main` branch.

---

## Part 3: Setting up the Config Client

1. **Add Dependencies:**
   In your client microservice (e.g., `order-service`), add the config client dependency in `pom.xml`:
   ```xml
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-config</artifactId>
   </dependency>
   ```

2. **Configure the Client to connect to the Server:**
   In the client's `src/main/resources/application.properties`, specify its application name and the URL of the Config Server.
   
   *(Note: For Spring Boot 2.4 and later, use `spring.config.import`)*
   ```properties
   spring.application.name=order-service
   
   # Tell the client where to find the Config Server
   spring.config.import=optional:configserver:http://localhost:8888
   ```

3. **Test the Configuration:**
   Create a simple REST Controller in the client application to verify that the property was fetched successfully.
   ```java
   import org.springframework.beans.factory.annotation.Value;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.RestController;

   @RestController
   public class MessageController {

       @Value("${message.greeting:Default Greeting}")
       private String greeting;

       @GetMapping("/greeting")
       public String getGreeting() {
           return greeting;
       }
   }
   ```

4. **Run the Client:**
   Start the `order-service`. When you hit the `/greeting` endpoint, it should return `"Hello from the central Config Server!"` instead of the default greeting.
