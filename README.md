# 🚗 Driver Dashboard Backend

*A Spring Boot based backend system for managing drivers, routes, vehicles, and transport operations.*

---

## 📌 Overview

This backend system helps streamline transport operations by managing drivers, vehicles, trips, route assignments, maintenance, and emergency alerts.

It is built using a clean layered architecture (Controller → Service → Repository) and follows REST API best practices with validation, DTOs, pagination, and proper error handling.

---

## 🛠️ Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring MVC**
* **Hibernate • JPA**
* **MySQL**
* **Maven**
* **Postman (API Testing)**

---

## 🔥 Core Features

* ✔ CRUD operations for all modules
* ✔ DTO-based request and response
* ✔ Input validation using Java Validation API
* ✔ Global exception handling
* ✔ Pagination & sorting support
* ✔ File upload support (driver license/documents)
* ✔ Structured layered architecture

---

## 📂 Major Modules

| Module                | Purpose                                                                 |
| --------------------- | ----------------------------------------------------------------------- |
| Driver Management     | Create, update, delete, and fetch driver info + license document upload |
| Vehicle & Maintenance | Track vehicles and maintenance histories                                |
| Fuel Logs             | Record fuel consumption and mileage statistics                          |
| Trip Scheduling       | Assign drivers to routes and track trip logs                            |
| Announcements         | Admin broadcast system                                                  |
| Emergency Contacts    | Store and retrieve emergency support details                            |

---

## 📡 Example Endpoints

### ➤ Create Driver

```
POST /api/drivers
```

**Request Body:**

```json
{
  "name": "John Doe",
  "phone": "9876543210",
  "licenseNumber": "DL123456",
  "experience": 4
}
```

### ➤ Fetch Drivers (Paginated)

```
GET /api/drivers?page=0&size=10
```

---

## 🧱 Architecture

```
src/
 └── main/
     ├── java/
     │   └── com.project.driverdashboard/
     │       ├── controller/
     │       ├── service/
     │       ├── repository/
     │       ├── model/
     │       ├── dto/
     │       └── exception/
     └── resources/
         ├── application.properties
         └── schema.sql (optional)
```

---

## ⚙️ Run Locally

1. Clone repo:

```
git clone https://github.com/kirans93/driver-dashboard.git
```

2. Update DB credentials in `application.properties`.
3. Run:

```
mvn spring-boot:run
```

---

## 🚀 Future Enhancements

* JWT authentication & role-based access
* Swagger documentation
* Deployment to AWS/Railway

---

## 🧑‍💻 Author

**Kiran S**
Backend Developer (Java | Spring Boot | SQL)
📧 Email: [kiran.shivanna.dev@gmail.com](mailto:kiran.shivanna.dev@gmail.com)
🌐 Portfolio: kirans93.github.io

---

⭐ If you found this interesting, consider giving it a star!
