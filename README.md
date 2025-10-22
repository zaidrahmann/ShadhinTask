# 🧮 ShadhinTask API

A simple Spring Boot REST API that performs basic CRUD operations backed by a MySQL database, plus logic endpoints to test Java problem-solving and Spring Boot understanding.

## 🚀 Project Setup Instructions

### 1️⃣ Prerequisites
- Java 17+
- Maven
- MySQL Server 8.0+ (with a taskdb database created)
- Postman or cURL (for testing endpoints)

### 2️⃣ Database Setup


**Option A: Create the database manually**
```sql
CREATE DATABASE taskdb;
```
**Option B: Use provided schema.sql**  
Place the `schema.sql` file in `src/main/resources/`. Spring Boot will execute it automatically on startup.

**File: `src/main/resources/schema.sql`**
```sql
CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority INT NOT NULL CHECK (priority BETWEEN 1 AND 5),
    completed BOOLEAN DEFAULT FALSE
);
```
**Optional Sample Data**
Include a `data.sql` file in `src/main/resources/ for sample data.`

File: `src/main/resources/data.sql`

```sql
INSERT INTO tasks (title, description, priority, completed) VALUES 
('Setup project', 'Initialize folders', 3, false),
('Implement CRUD', 'Develop API routes', 2, false);
```

### 3️⃣ Configure Database Credentials

Create a `.env` file in the project root (do not commit this to GitHub):
```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=taskdb
DB_USER=your_mysql_username
DB_PASS=your_mysql_password
```

Update `src/main/resources/application.properties`:
```
spring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.sql.init.mode=always
server.port=8080
spring.main.banner-mode=off
```

⚠️ **Never commit real credentials to GitHub. Use environment variables or placeholders.**

### 4️⃣ Run the Project

Open a terminal in the project directory and run:
```bash
mvn spring-boot:run
```

The application will start on: 👉 **http://localhost:8080**

## 🗂️ Database Schema

See `src/main/resources/schema.sql` above for the tasks table structure.

## 🧰 List of Raw SQL Statements Used

**CRUD Operations**
- **Create:** `INSERT INTO tasks (title, description, priority, completed) VALUES (?, ?, ?, ?)`
- **Read (All):** `SELECT * FROM tasks`
- **Read (By ID):** `SELECT * FROM tasks WHERE id = ?`
- **Update (Complete):** `UPDATE tasks SET completed = TRUE WHERE id = ?`
- **Delete:** `DELETE FROM tasks WHERE id = ?`

**Special Endpoints**
- **Highest Priority:** `SELECT * FROM tasks WHERE priority = (SELECT MIN(priority) FROM tasks) LIMIT 1`
- **Stats:** `SELECT COUNT() FROM tasks; SELECT COUNT() FROM tasks WHERE completed = TRUE`

## 🌐 Example API Endpoints

### 1️⃣ Create a new task
**Method:** POST  
**Endpoint:** `/tasks`  
**Request:**
```json
{
    "title": "Finish documentation",
    "description": "Add README.md",
    "priority": 1,
    "completed": false
}
```
**Response (201 Created):**
```json
{
    "id": 3,
    "title": "Finish documentation",
    "description": "Add README.md",
    "priority": 1,
    "completed": false
}
```

### 2️⃣ Get all tasks
**Method:** GET  
**Endpoint:** `/tasks`  
**Response (200 OK):**
```json
[
    {
        "id": 1,
        "title": "Setup project",
        "description": "Initialize folders",
        "priority": 3,
        "completed": false
    },
    {
        "id": 2,
        "title": "Implement CRUD",
        "description": "Develop API routes",
        "priority": 2,
        "completed": false
    }
]
```

### 3️⃣ Get a task by ID
**Method:** GET  
**Endpoint:** `/tasks/{id}` (e.g., `/tasks/2`)  
**Response (200 OK):**
```json
{
    "id": 2,
    "title": "Implement CRUD",
    "description": "Develop API routes",
    "priority": 2,
    "completed": false
}
```

### 4️⃣ Mark a task as completed
**Method:** PUT  
**Endpoint:** `/tasks/{id}/complete` (e.g., `/tasks/2/complete`)  
**Response (200 OK):** No content

### 5️⃣ Delete a task
**Method:** DELETE  
**Endpoint:** `/tasks/{id}` (e.g., `/tasks/2`)  
**Response (204 No Content):** No content

### 6️⃣ Get highest priority task
**Method:** GET  
**Endpoint:** `/tasks/highest-priority`  
**Response (200 OK):**
```json
{
    "id": 3,
    "title": "Finish documentation",
    "description": "Add README.md",
    "priority": 1,
    "completed": false
}
```

### 7️⃣ Get task stats
**Method:** GET  
**Endpoint:** `/tasks/stats`  
**Response (200 OK):**
```json
"Total: 3, Completed: 0, Pending: 3"
```

## ⚙️ Assumptions & Edge Cases

**Assumptions:**
- Priority ranges from 1 to 5, with 1 being the highest priority.
- id is auto-incremented by the database.

**Edge Cases Handled:**
- Invalid priority (outside 1-5) throws an error.
- Non-existent task ID returns 404.
- Empty task list for `/tasks/highest-priority` or `/tasks/stats` handles appropriately (e.g., no tasks or zero counts).

## 🧑‍💻 Author

**Name:** Zaid Rahman  
**Project:** ShadhinTask  
**Tech Stack:** Spring Boot, MySQL, Maven
