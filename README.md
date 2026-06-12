# Student Management System (Spring Boot)

A professional **College ERP** built with **Spring Boot 3**, **Spring Security (JWT)**, **Spring Data JPA**, **Thymeleaf**, **Bootstrap 5**, and **MySQL**.

## Tech Stack

| Layer | Technology |
|-------|------------|
| Backend | Java 17, Spring Boot 3.2, Spring MVC, Spring Security |
| Persistence | Spring Data JPA, Hibernate, MySQL 8 |
| API | REST + Swagger (OpenAPI 3) |
| Frontend | Thymeleaf, Bootstrap 5, Chart.js |
| Auth | JWT (REST) + Form Login (Web) |
| Build | Maven, Lombok |

## Architecture

```
Controller (Web + REST)
    ↓
Service (Business Logic)
    ↓
Repository (JPA)
    ↓
MySQL Database
```

- **DTOs** for API request/response
- **@ControllerAdvice** for global exception handling
- **BCrypt** password encryption
- **Hibernate Validator** for input validation
- **SLF4J** logging

## Features

- Secure login & registration
- Role-based access: **Admin**, **Teacher**, **Student**
- Student CRUD with photo upload, search, filter, pagination
- Course & subject management
- Attendance (manual marking)
- Marks & results with auto grade calculation
- Dashboard with statistics & charts
- PDF/Excel export
- AI performance prediction
- QR attendance (REST API)
- Activity logs
- Dark/light theme toggle
- Glassmorphism UI

## Prerequisites

- JDK 17+
- Maven 3.8+
- MySQL 8.0+

## Quick Start

### 1. Configure Database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 2. Create Database (optional)

```sql
CREATE DATABASE student_management;
```

Or let JPA create it with `createDatabaseIfNotExist=true`.

### 3. Run Application

```bash
mvn spring-boot:run
```

Open: **http://localhost:8080**

### 4. Demo Credentials (auto-seeded on first run)

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@college.edu | Admin@123 |
| Teacher | teacher1@college.edu | Admin@123 |
| Student | student1@college.edu | Admin@123 |

## API Documentation

- Swagger UI: **http://localhost:8080/swagger-ui.html**
- OpenAPI JSON: **http://localhost:8080/api-docs**

### Authenticate via REST

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@college.edu",
  "password": "Admin@123"
}
```

Use the returned token:

```http
Authorization: Bearer <token>
```

### Main API Endpoints

| Module | Endpoints |
|--------|-----------|
| Auth | `POST /api/auth/login`, `POST /api/auth/register` |
| Students | `GET/POST/PUT/DELETE /api/students`, `GET /api/students/export/pdf` |
| Courses | `GET/POST /api/courses`, `GET/POST /api/courses/subjects` |
| Attendance | `GET/POST /api/attendance`, `POST /api/attendance/qr/create` |
| Marks | `GET/POST/DELETE /api/marks` |
| Dashboard | `GET /api/dashboard/stats`, `GET /api/dashboard/predict` |

## Web UI Routes

| URL | Description |
|-----|-------------|
| `/login` | Login page |
| `/register` | Registration |
| `/dashboard` | Analytics dashboard |
| `/students` | Student management |
| `/attendance` | Attendance |
| `/marks` | Marks & results |
| `/courses` | Courses & subjects |
| `/predictions` | AI predictions |

## Project Structure

```
src/main/java/com/college/sms/
├── config/          # Security, OpenAPI, DataInitializer
├── controller/
│   ├── api/         # REST controllers
│   └── web/         # Thymeleaf MVC controllers
├── dto/             # Request/Response DTOs
├── entity/          # JPA entities
├── exception/       # Custom exceptions & handler
├── repository/      # JPA repositories
├── security/        # JWT filter, UserDetails
├── service/         # Business logic
└── util/            # Helpers, file storage

src/main/resources/
├── templates/       # Thymeleaf HTML
├── static/          # CSS, JS
└── application.properties
```

## Deployment

### JAR Deployment

```bash
mvn clean package -DskipTests
java -jar target/student-management-system-1.0.0.jar
```

### Production Checklist

1. Set `spring.jpa.hibernate.ddl-auto=validate` (or `none`)
2. Use strong `app.jwt.secret` (32+ characters)
3. Configure production MySQL credentials
4. Enable HTTPS via reverse proxy (Nginx)
5. Set `spring.thymeleaf.cache=true`
6. Configure SMTP for email notifications

### Docker (optional)

```dockerfile
FROM eclipse-temurin:17-jre
COPY target/student-management-system-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## License

MIT – Educational and commercial use permitted.


# AI-Powered Smart Retail Management System

## Overview

AI-Powered Smart Retail Management System is an intelligent retail solution designed to help shop owners manage their inventory, billing, product placement, and sales analytics efficiently. The system uses Artificial Intelligence and Data Science techniques to provide smart recommendations for store layout optimization, demand forecasting, and inventory management.

This project combines Spring Boot, MySQL, Machine Learning, and Data Analytics to create a next-generation retail management platform.

---

## Problem Statement

Most shop owners arrange products based on personal experience rather than data-driven insights. Poor product placement, inefficient inventory management, and lack of sales analysis can reduce profitability.

This system aims to solve these problems by providing AI-powered recommendations and analytics.

---

## Key Features

### Authentication & Security

* User Registration and Login
* JWT Authentication
* Role-Based Access Control

### Shop Management

* Shop Registration
* Store Layout Configuration
* Shop Dimension Management

### Product Management

* Add, Update, Delete Products
* Product Categorization
* Shelf Location Management

### Inventory Management

* Real-Time Stock Tracking
* Low Stock Alerts
* Inventory Monitoring

### Billing System (POS)

* Product Billing
* Invoice Generation
* Sales Record Management
* Transaction History

### Analytics Dashboard

* Daily Sales Reports
* Monthly Sales Reports
* Product Performance Analysis
* Revenue Tracking

### AI-Powered Features

#### Smart Product Placement

Suggests optimal product locations based on sales behavior and customer purchasing patterns.

#### Market Basket Analysis

Identifies products frequently purchased together and recommends shelf arrangements.

#### Demand Forecasting

Predicts future product demand using historical sales data.

#### Dead Stock Detection

Detects slow-moving products and suggests promotional actions.

#### Store Layout Optimization

Recommends ideal product positioning based on store dimensions and product categories.

#### AI Recommendations

Provides actionable insights to improve sales and customer experience.

---

## Technology Stack

### Backend

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Hibernate/JPA

### Database

* MySQL

### AI & Data Science

* Python
* Pandas
* NumPy
* Scikit-Learn

### Frontend (Planned)

* React.js

### Tools

* Git
* GitHub
* Postman
* Maven

---

## System Architecture

Frontend (React)

↓

Spring Boot REST APIs

↓

MySQL Database

↓

AI Engine (Python)

↓

Analytics & Recommendations

---

## Future Enhancements

* Automatic Store Layout Visualization
* QR Code Billing
* Supplier Recommendation System
* Multi-Shop Management
* Customer Purchase Analysis
* AI-Based Dynamic Pricing
* Mobile Application Support

---

## Project Goals

* Improve product visibility
* Increase sales through intelligent placement
* Reduce inventory waste
* Automate retail decision-making
* Provide data-driven business insights

---

## Learning Objectives

Through this project, the following concepts will be explored:

* Spring Boot Development
* REST API Design
* Database Design
* Authentication & Authorization
* Machine Learning Integration
* Demand Forecasting
* Recommendation Systems
* Retail Analytics
* System Design

---

## Project Status

🚧 Currently Under Development

Phase 1: Backend Foundation & Authentication

Planned Modules:

* User Management
* Shop Management
* Product Management
* Inventory Management
* Billing System
* Analytics Dashboard
* AI Recommendation Engine

---

## Author

Chandan Pathak

B.Tech (Data Science)

Passionate about Artificial Intelligence, Data Science, Java, and System Design.
