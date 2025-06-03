# BadoniNetWork

A Spring Boot web application developed for IIS Badoni Technical Institute that facilitates networking between students and companies. The platform provides a comprehensive matching system with skills tracking, job postings, and direct communication channels between students and potential employers.

## Project Overview

BadoniNetWork is designed to:
- Connect students with potential employers
- Track and manage student skills and language proficiencies
- Enable companies to post job opportunities
- Facilitate direct communication between students and companies
- Provide a secure and efficient matching system

## Version Information

Current Version: 1.0.0
- Initial release: Spring 2024
- Developed by: IIS Badoni students and faculty
- Platform Status: Production

For a detailed list of changes and updates, please see the [CHANGELOG.md](CHANGELOG.md) file.

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.3.0
- Spring Security for authentication and authorization
- Spring Data JPA for data persistence
- Spring Validation for input validation
- Spring WebSocket for real-time communications
- Thymeleaf template engine for server-side rendering
- MySQL database
- Maven for dependency management and build
- Lombok for reducing boilerplate code
- Spring Mail for email communications

### Frontend
- Thymeleaf for server-side rendering
- HTML5
- CSS3
- JavaScript
- Bootstrap (for responsive design)

## Main Features

### User Management
- Student registration and profile management
- Company registration with multi-location support
- Email verification system
- Multi-role authentication (Student/Company/Admin)
- Secure password management

### Core Functionality
- Skills Management (Competenze)
  * Track multiple skills per student
  * Skill level assessment (Base, Intermediate, Advanced)
  * Skill categorization
- Language Proficiency
  * Multiple language support
  * Proficiency level tracking
- Company-Student Interaction
  * Direct messaging system
  * Contact management
  * Profile visibility controls
- Job Posting System (Annunci)
  * Detailed job announcements
  * Skill requirements matching
  * Location-based filtering
- Company Multi-location Support
  * Multiple office locations (AltraSede)
  * Region-based job postings

## Database Structure

### Key Entities
- Students (Studente)
  * Personal information
  * Skills and proficiencies (CompetenzaStudente)
  * Language proficiencies (LinguaStudente)
  * Contact details
- Companies (Azienda)
  * Company information
  * Multiple locations
  * Contact persons
- Skills (Competenza)
  * Skill categories
  * Proficiency levels (LivelloCompetenza)
- Languages (Lingua)
  * Language types
  * Proficiency levels
- Job Postings (Annuncio)
  * Job details
  * Contract types
  * Location information
- Contacts (Contatto)
  * Communication history
  * Message tracking
  * Contact status

## Security Features

- CORS configuration for cross-origin requests
- Stateless session management
- Password encryption using BCrypt
- Role-based access control
- Secure email verification process
- Protected API endpoints

## Project Structure

```
src/main/java/dp/esempi/security/
├── configuration/    # Security and application configs
├── controller/       # REST endpoints and web controllers
├── model/            # Entity classes
├── repository/       # Data access interfaces
├── service/          # Business logic implementation
└── validation/       # Input validation
```

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### System Requirements
- Minimum 2GB RAM
- 1GB free disk space
- Internet connection for email verification

### Supported Browsers
- Chrome (latest version)
- Firefox (latest version)
- Safari (latest version)
- Edge (latest version)

### Installation Steps

1. Clone the repository:
```bash
git clone <repository-url>
```

2. Configure MySQL database:
```sql
CREATE DATABASE network;
```

3. Import the database schema:
```bash
mysql -u username -p network < network.sql
```

4. Update application.properties with your database configuration:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/network
spring.datasource.username=your_username
spring.datasource.password=your_password
```

You may also need to configure the following environment variables:
- MAIL_HOST: SMTP server host
- MAIL_PORT: SMTP server port
- MAIL_USERNAME: Email account username
- MAIL_PASSWORD: Email account password

5. Build the project:
```bash
mvn clean install
```

6. Run the application:
```bash
mvn spring-boot:run
```

### Deployment
The application is designed to be deployed in a production environment with:
- Reverse proxy support
- SSL/TLS configuration
- Load balancing capability

## Basic Usage Guidelines

### Student Registration
1. Navigate to the registration page
2. Enter required information (using @iisbadoni.edu.it email domain)
3. Verify email address with the code sent
4. Complete profile with skills and languages

### Company Registration
1. Access company registration
2. Provide company details
3. Add company locations
4. Verify company email
5. Create job postings

### Job Posting
1. Log in as company
2. Create new job announcement
3. Specify contract type and modality
4. Set location preferences
5. Publish the posting

### Profile Management
1. Update personal/company information
2. Manage skills and languages
3. Control profile visibility
4. Track communications

## Data Protection

The system implements various measures to protect user data:
- Encrypted password storage using BCrypt
- Secure email verification process
- HTTPS for all communications
- Session management
- Input validation and sanitization

## Support

For support and questions related to the BadoniNetWork platform, please contact the system administrator.

## Contributing

This project is part of IIS Badoni's educational initiative. Contributions are welcome from:
- Students
- Teachers
- Industry partners
- Open source community

Please follow these steps to contribute:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

