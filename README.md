📚 NK’s Book Store – Microservices Full Stack App

A full-stack online bookstore built using Spring Boot microservices and a React frontend, designed to demonstrate scalable system design, secure authentication, and resilient service communication.

🚀 Tech Stack

Backend

Java, Spring Boot

Spring Cloud (Eureka Service Discovery, OpenFeign)

JWT Authentication

Redis (Refresh Tokens)

JUnit & Mockito (Unit Testing)

Frontend

React.js

DevOps

Docker & Docker Compose

Environment-based configuration using .env

🧩 Architecture Overview

The application follows a microservices architecture, where each service is independently deployable and communicates via REST.

Service	Responsibility
Auth Service	JWT authentication & token refresh
User Service	User management
Book Service	Book catalog & inventory
Cart Service	Shopping cart management
Order Service	Order placement & processing
Discovery Service	Eureka server for service registration

✨ Key Features

🔐 JWT-based Authentication with secure refresh token handling

🔎 Service Discovery (Eureka) for dynamic service registration

🔗 Inter-service communication using OpenFeign

♻️ Resilient order processing with retry, fallback, and failure handling

🧪 Unit testing with JUnit & Mockito

🐳 Dockerized microservices for consistent local development

🧱 Clean layered architecture following SOLID principles
