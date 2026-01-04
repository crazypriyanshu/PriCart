### 🛒 **PriCart | Enterprise E-Commerce Backend**
PriCart is a robust, highly-scalable E-commerce platform built with Spring Boot. 
It features a modern microservices-ready architecture, integrating real-time event streaming, distributed caching, and secure payment processing.

## 🚀 **Key Technical Features**
#### 🛡️ Security & Authentication
OAuth2 + JWT: Secure stateless authentication implemented via Spring Security.

Role-Based Access Control (RBAC): Fine-grained permissions for Users and Admins.

#### 💳 Payment & Webhooks
Razorpay Integration: Seamless checkout experience with secure payment gateway integration.

Webhook Handling: Robust webhook listeners to handle asynchronous payment success/failure notifications and order state updates.

#### ⚡ Performance & Scalability
Event-Driven (Kafka): Integrated Kafka & Zookeeper clusters for decoupled communication and high-throughput event processing.

Distributed Caching (Redis): Dockerized Redis Cluster for caching frequent product queries and improving application latency.

Concurrency Control: Strict use of @Transactional and Hibernate optimization to ensure data integrity during high-traffic checkout events.

#### 🏗️ Engineering Excellence
SOLID Principles: Codebase designed for extensibility and maintainability.

Robust Logging: Detailed application logs for monitoring and production debugging.

Observability: Integrated Spring Boot Actuators for health checks and system metrics.

API Documentation: Interactive documentation using Swagger UI.

### **Tech Stack**
Backend: Java 17, Spring Boot, Spring Security (OAuth2/JWT)

Data: MySQL, Hibernate (JPA), Redis

Messaging: Apache Kafka, Zookeeper

Payments: Razorpay API & Webhooks

Testing & Mocking: Data seeders with Java-Faker

DevOps: Docker, Docker-Compose (Multi-container orchestration)


### 📦 Project Structure & Workflow
Auth: User registers/logs in via OAuth2/JWT.

Catalog: Browse products seeded via Faker API.

Cart & Order: Add items to cart and place orders using transactional logic.

Payment: Initiate Razorpay payment; status is updated via secure Webhooks.

Events: Kafka producers trigger notifications/inventory updates upon order completion.

## 🚦 Getting Started
**Prerequisites**
_Docker & Docker Compose_

**JDK 17+**

**Maven**

_Installation & Setup_
Clone the repository:

`git clone https://github.com/crazypriyanshu/PriCart.git`
cd priCart


Spin up Infrastructure (Kafka, Redis, MySQL):

`docker-compose up -d`
Configure Environment: Update src/main/resources/application.properties with your Razorpay API keys and database credentials.

Run the Application:
`mvn spring-boot:run`


### API

**Access API Documentation: Open http://localhost:8080/swagger-ui.html to explore the endpoints.
**
The project includes a built-in Data Seeder component. Upon startup (optional configuration), it uses Java-Faker to populate the MySQL database with:

Mock Users
Categories
Realistic Product Catalogs


#### 🐳 Docker Orchestration
The project includes a docker-compose.yml that manages:

Redis Cluster: For high-speed data retrieval.

Kafka & Zookeeper: For the event-driven backbone.

MySQL Instance: The primary persistent store.

#### 📝 License
This project is licensed under the MIT License - see the LICENSE file for details.

Developed by Priyanshu
