# FinPay — Financial Transaction System

A backend REST API for managing financial transactions, built with Java 21 and Spring Boot. The project uses PostgreSQL for data persistence, Flyway for database schema versioning, and Stripe (test mode) for payment processing.

## Why This Project

FinPay was created as a practical backend engineering project to explore the challenges involved in building financial applications.

The main focus is on developing reliable APIs while practicing essential backend concepts such as data consistency, input validation, transaction management, exception handling, external API integration, and automated testing.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 4.1.2 (Spring Web MVC, Spring Data JPA, Bean Validation, Actuator)
- **Database:** PostgreSQL 16
- **Database Migrations:** Flyway
- **Payment Processing:** Stripe API (test mode)
- **Infrastructure:** Docker and Docker Compose
- **Build Tool:** Maven
- **Testing:** JUnit 5 and Mockito (planned)
- **API Testing:** Postman

## Architecture

The application follows a layered architecture to separate responsibilities and keep business logic independent from infrastructure concerns.

```text
Client (Postman / Frontend)
        │
        │ HTTP / JSON
        ▼
Controller Layer
        │  Handles HTTP requests,
        │  validates input and returns responses
        ▼
Service Layer
        │  Implements business rules,
        │  transaction logic and validation
        ▼
Repository Layer
        │  Handles database access
        │  through Spring Data JPA
        ▼
PostgreSQL
           Database schema managed by Flyway
```

The Stripe integration will be implemented in a separate service layer, keeping payment provider interactions isolated from the core business logic.

## Getting Started

### Prerequisites

Make sure you have the following tools installed:

- Java 21
- Docker Desktop
- Git
- A Stripe account with test mode enabled (required only for payment integration)

### Run Locally

**1. Clone the repository**

```bash
git clone https://github.com/YOUR_USERNAME/financial-transaction-system.git
cd financial-transaction-system
```

**2. Start the database**

```bash
docker compose up -d
```

**3. Run the application**

On Linux or macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

**4. Check the application health**

Send a GET request to:

```text
http://localhost:8080/actuator/health
```

Expected response:

```json
{
  "status": "UP"
}
```

## Configuration

Database connection settings are configured in:

```text
src/main/resources/application.properties
```

The default configuration is intended for local development.

Stripe API keys must never be committed to the repository. The application will read the secret key from an environment variable.

On Linux or macOS:

```bash
export STRIPE_API_KEY=sk_test_your_key_here
```

On Windows PowerShell:

```powershell
$env:STRIPE_API_KEY="sk_test_your_key_here"
```

Only Stripe test-mode credentials should be used during development.

## Key Engineering Concepts

The project focuses on practicing backend engineering principles commonly used in financial applications:

- **RESTful API Design:** Building structured HTTP endpoints and handling requests and responses.
- **Transaction Management:** Maintaining reliable database operations.
- **Data Consistency:** Protecting the integrity of financial records.
- **Input Validation:** Preventing invalid data from entering the system.
- **Exception Handling:** Returning consistent and meaningful error responses.
- **Database Migrations:** Managing schema changes with Flyway.
- **Payment Gateway Integration:** Communicating with external payment services.
- **Webhook Processing:** Handling asynchronous payment events.
- **Idempotency:** Preventing duplicate processing of transaction requests.
- **Automated Testing:** Verifying application behavior through unit and integration tests.

Some of these concepts are part of the development roadmap and will be implemented incrementally.

## Project Status

| Area | Status |
|---|---|
| Project setup and dependencies | Done |
| PostgreSQL with Docker Compose | Done |
| Application health endpoint | Done |
| Database migrations (Flyway) | In Progress |
| Account and transaction entities | Planned |
| Transaction REST endpoints | Planned |
| Transaction management and validation | Planned |
| Stripe payment integration | Planned |
| Webhook event processing | Planned |
| Idempotency implementation | Planned |
| Unit and integration tests | Planned |

## Design Decisions

- **Layered Architecture:** Controllers, services, and repositories have separate responsibilities, making the application easier to maintain and test.
- **No Sensitive Card Data Storage:** Payment card information is handled by Stripe. The application will store payment references and transaction statuses rather than raw card details.
- **Externalized Secrets:** Sensitive credentials are provided through environment variables instead of being hardcoded into the source code.
- **Database Versioning:** Flyway migrations provide a consistent and reproducible database schema across environments.
- **Separation of Payment Logic:** Stripe-specific operations are isolated from the core business logic to reduce coupling with the payment provider.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

**Disclaimer:** FinPay is an educational portfolio project. It is not a real banking application or a production payment processing system. All payment integrations are intended for Stripe test mode.
