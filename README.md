# 💳 MicroBank - Microservices-Based Banking System

MicroBank is a **Spring Boot-based microservices banking system** designed with scalability, modularity, and modern cloud-native practices in mind. It leverages **Spring Cloud components**, **Docker**, and **Kubernetes (Minikube)** to provide a robust backend infrastructure.

---

## 🛠️ Technologies & Tools Used

- **Spring Boot** – For building each individual microservice
- **Spring Cloud Gateway** – Acts as the API Gateway
- **Spring Cloud Config Server** – Centralized configuration management
- **Eureka Server** – Service discovery and registration
- **Spring Security + JWT** – For secure authentication and authorization
- **FeignClient** – For simplified and declarative inter-service communication
- **MySQL** – As relational databases for persistent storage
- **Docker** – Containerization of all services
- **Kubernetes (Minikube)** – Local orchestration and deployment of services

---

## 🧩 System Architecture

- **Microservices**: Each core domain (e.g., user, account, transaction) is built as an independent Spring Boot microservice.
- **API Gateway**: All external requests are routed through the Spring Cloud Gateway for security and routing.
- **Service Discovery**: Eureka Server allows services to register themselves and discover each other dynamically.
- **Configuration Management**: Centralized via Spring Cloud Config Server, with support for dynamic config updates.
- **Security**: JWT-based authentication handled through Spring Security.
- **Inter-Service Communication**: Implemented using FeignClient for clean, declarative, and synchronous REST communication between microse
- **Deployment**: Each service is Dockerized and deployed to a **local Minikube Kubernetes cluster** for orchestration and scalability testing.


