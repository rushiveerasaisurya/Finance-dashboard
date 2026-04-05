# Finance Data Processing and Access Control Backend

This is a robust and secure Spring Boot backend for a Finance Dashboard system. It handles user management, role-based access control (RBAC), and financial record processing with insights/summaries.

## 🚀 Tech Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.4.x
- **Database**: H2 In-Memory Database (for rapid setup & assessment purposes)
- **Security**: Spring Security with JWT (JSON Web Tokens)
- **Documentation**: Springdoc OpenAPI (Swagger UI)

---

## ⚙️ Setup and Run Instructions

### Prerequisites
- Java 21 installed.
- Maven (optional, wrapper is included).

### Running the Application
1. Clone the repository and navigate to the `Backend` directory.
2. Run the application using the Maven wrapper:
   - **Windows**: `.\mvnw.cmd spring-boot:run`
   - **Mac/Linux**: `./mvnw spring-boot:run`
3. The server will start at `http://localhost:8080`.

### Default Seeded Users (Data Seeder)
On startup, the system automatically runs a `DataSeeder` which creates users and dummy financial records. Use these credentials to test the APIs.

| Role    | Username | Password   | Permissions |
| --------| ---------| ---------- | ----------- |
| **ADMIN**   | admin    | admin123   | Full management of users and records. Global data visibility. |
| **ANALYST** | analyst  | analyst123 | Can view records and summaries (global data). Cannot create/edit records. |
| **VIEWER**  | viewer   | viewer123  | Can view summaries/records (own data only). Cannot create/edit records. |

---

## 📖 API Explanation & Swagger UI
All APIs are thoroughly documented using Swagger/OpenAPI. 
Once the server is running, visit: **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Core API Endpoints

**1. Authentication (`/api/auth`)**
- `POST /api/auth/login`: Accepts `{"username": "...", "password": "..."}` and returns a stateless JWT token.

**2. Financial Records (`/api/records`)**
- `GET /api/records`: Fetches paginated financial records. Includes robust filtering via query params (`startDate`, `endDate`, `category`, `type`, `page`, `size`).
- `POST /api/records` (Admin only): Creates a new record.
- `PUT /api/records/{id}` (Admin only): Updates an existing record.
- `DELETE /api/records/{id}` (Admin only): Deletes a record.

**3. Dashboard Summaries (`/api/dashboard`)**
- `GET /api/dashboard/summary`: Agregates data returning Total Income, Total Expenses, Net Balance, Category-wise expenses, and recent activity (monthly/weekly).

**4. User Management (`/api/users`)**
- `GET /api/users`: Fetches all users (Admin only).
- `POST /api/users`: Creates a new user (Admin only).
- `PATCH /api/users/{id}/role` & `/status`: Updates user access (Admin only).

---

## 🛠️ Design Assumptions
1. **Data Visibility Isolation**: I made a strict architectural assumption that **Admins and Analysts have global visibility** over all records in the system, whereas **Viewers can only view their own specific records and their own dashboard summary**. This effectively isolates data between lower-tier standard users.
2. **Stateless Authentication**: Chose JWT over Session states. In a scalable dashboard, avoiding sticky sessions via server-side session stores ensures horizontal scalability.
3. **Transaction Status Types**: Hardcoded the record types to strictly `INCOME` or `EXPENSE` using enums, establishing strong data integrity.
4. **Validation First**: Strict JSR-303 annotations (`@NotNull`, `@Valid`) placed on incoming DTOs over validating inside the service logic limits bad data from ever hitting the logical application layer.

## ⚖️ Tradeoffs Considered
1. **In-Memory H2 DB vs Relational PostgreSQL**: Used H2 to ensure the evaluator can run the application instantly without provisioning a separate DB server. In a production scenario, JPA dialects would simply switch to PostgreSQL.
2. **Access token only JWT**: For the sake of minimizing assignment complexity overhead, only the access token was implemented. Soft-expiration and refresh-tokens would be the logical next step for longevity in a real-world enterprise situation. 
3. **Soft Delete**: I chose to use hard deletes (`.deleteById()`) rather than soft deletes (`isActive = false`) to prioritize speed-to-delivery for the RBAC core assignment requirements, though the relational scaling strategy would typically lean towards soft deletes in the future.

---

## ☁️ Cloud Deployment (Render)

This application is configured for easy Cloud Deployment. We recommend utilizing a free PaaS provider like [Render](https://render.com/).

**Deployment Steps:**
1. Commit your code and push it to a GitHub Repository. *(Ensure your Application `server.port` binds to `${PORT:8080}`).*
2. Sign in to Render and create a new **Web Service**.
3. Connect your GitHub repository.
4. Set the Environment to **Java**.
5. Set the Build Command: `./mvnw clean package -DskipTests`
6. Set the Start Command: `java -jar target/demo-0.0.1-SNAPSHOT.jar`
7. Click Deploy! Render will build the Maven project and start your API completely free of charge.

*(Note: Since this relies on a free tier H2 In-Memory Database, data resets will occur appropriately upon server restarts/sleep cycles).*
