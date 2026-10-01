# Charter Rewards Program

## 📌 Overview
This Spring Boot application implements a retailer rewards program.  
Customers earn points based on transaction amounts:
- 2 points per dollar spent over $100
- 1 point per dollar spent between $50 and $100

Example: $120 purchase = (2 × 20) + (1 × 50) = 90 points.

The system calculates rewards per customer, per month, and totals across a three‑month period.

---

## ⚙️ Tech Stack
- Java 17
- Spring Boot 4.1.1
- JPA/Hibernate
- H2 in‑memory database (for testing)
- JUnit 5 + Mockito (unit tests)
- MockMvc (integration tests)

---

## 🛠️ Setup & Installation
### Clone the repository
```bash
git clone https://github.com/<your-username>/charter-rewards.git
cd charter-rewards
mvn clean install
```

### Run
```bash
mvn spring-boot:run
mvn test
```

## 📂 Project Structure
```text
rewards/
├── src/
│   ├── main/
│   │   ├── java/com/charter/rewards/
│   │   │   ├── controller/        # REST controllers
│   │   │   │   └── RewardsController.java
│   │   │   ├── service/           # Business logic
│   │   │   │   └── RewardService.java
│   │   │   ├── entity/            # JPA entities
│   │   │   │   ├── Customer.java
│   │   │   │   └── Transaction.java
│   │   │   ├── repository/        # Spring Data JPA repositories
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   └── TransactionRepository.java
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   │   ├── CustomerRewardsDTO.java
│   │   │   │   └── ErrorResponseDTO.java
│   │   │   ├── exception/         # Custom exceptions
│   │   │   │   └── CustomerNotFoundException.java
│   │   │   └── advice/            # Global exception handlers
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       ├── application.properties   # Config (thresholds, DB, etc.)
│   │       ├── schema.sql               # DB schema
│   │       └── data.sql                 # Seed data for testing
│   └── test/
│       ├── java/com/charter/rewards/
│       │   ├── service/RewardServiceTest.java
│       │   └── controller/RewardsControllerIntegrationTest.java
│       └── resources/
│           └── application-test.properties
├── .gitignore
├── README.md
├── pom.xml
```

## 🔗 REST Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/api/rewards/all` | Rewards for all customers |
| GET    | `/api/rewards/{customerId}` | Rewards for a single customer |
| GET    | `/api/rewards/{customerId}/last-three-months` | Rewards for last 3 months |

### Example Response
```json
{
  "customerId": 1,
  "name": "Shalini",
  "pointsByYear": {
    "2026": {
      "September": 90,
      "October": 40
    }
  },
  "totalPoints": 130
}
```

### Error Response
```json
{
  "status": 404,
  "message": "Customer not found"
}
```

## Notes
- Data is loaded from SQL scripts, not Java code.
- H2 console: /h2-console, JDBC URL jdbc:h2:mem:testdb.

## 📈 Enhancements

The current implementation meets the assignment requirements. Future improvements could include:

1. **Pagination & Filtering**
    - Add pagination for `/api/rewards/all` endpoint.
    - Support filtering by date range, customer name, or transaction amount.

2. **Configurable Time Windows**
    - Extend beyond the fixed three-month calculation.
    - Allow clients to specify custom periods (e.g., last 6 months, year-to-date).

3. **Database Support**
    - Switch from H2 in-memory DB to external databases like PostgreSQL/MySQL for production.
    - Add migration scripts using Flyway or Liquibase.

4. **Caching**
    - Introduce caching (e.g., Redis) for frequently accessed reward data.
    - Reduce DB load and improve response times.

5. **Front-End Integration**
    - Build a simple UI (React/Angular) to visualize monthly rewards.
    - Show charts for points earned per customer.