Student App
📘 Overview

The Student App is a comprehensive solution designed to streamline various aspects of student life. It offers features that assist students in managing their academic schedules, assignments, and communication with faculty.

🚀 Features

User Authentication: Secure login and registration system.

Dashboard: Personalized dashboard displaying upcoming assignments and deadlines.

Assignment Management: Upload, view, and manage assignments.

Notifications: Real-time notifications for new assignments and announcements.

🛠️ Technologies Used

Frontend: HTML, CSS, JavaScript

Backend: Java (Spring Boot)

Database: MySQL

Authentication: JWT (JSON Web Tokens)

⚙️ Setup Instructions
Prerequisites

Java 11 or higher

MySQL 8.0 or higher

Maven 3.6 or higher

Installation Steps

Clone the repository:

git clone https://github.com/harshach55/student-app.git
cd student-app


Set up the database:

Create a new MySQL database named student_app.

Import the provided schema.sql file to set up the necessary tables.

Configure application properties:

Navigate to src/main/resources/application.properties.

Update the database connection settings:

spring.datasource.url=jdbc:mysql://localhost:3306/student_app
spring.datasource.username=root
spring.datasource.password=your_password


Build and run the application:

mvn clean install
mvn spring-boot:run


Access the application:

Open a browser and go to http://localhost:8080.
