
# Note App


Full Stack Note App with Spring Boot, JWT, and React. The backend uses Docker Compose to set up a MySQL database along with PHPMyAdmin for easy database management.

Backend Configuration
Backend Port: 8091

Frontend Configuration
Frontend Port: 3001
## Installation

Clone the repository:

```bash
git clone https://github.com/ensolvers-github-challenges/Rubio-37248f.git
cd Rubio-37248f
```
#### Setting Up the Backend and Database

```bash
docker-compose up
```
PhpMyAdmin is accessible at:
http://localhost:8090

* Username: root
* Password: password
Database config:
- Go to yourdb -> `roles` table and 
- Execute the following SQL query to insert predefined roles:
```
INSERT INTO roles (name) VALUES 
('ROLE_USER'),
('ROLE_MODERATOR'),
('ROLE_ADMIN'); 
```
#### Running Your Spring Boot Application
```
./mvnw spring-boot:run
```

#### Setting Up the Frontend
Install frontend dependencies:
```bash
cd frontend
npm install
```
Start the frontend:
```bash
npm start
```
## Additional Resources

Docker Compose with MySQL and PhpMyAdmin: https://tecadmin.net/docker-compose-for-mysql-with-phpmyadmin/
