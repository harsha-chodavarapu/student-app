# StuHub - Student Materials Platform

A comprehensive web application for students to upload, share, and manage educational materials with AI-powered content generation and interactive Q&A features.

## 📖 Overview

StuHub is a modern educational platform designed to streamline the way students manage, share, and interact with academic materials. The platform combines traditional file management with cutting-edge AI technology to provide intelligent content analysis, automated summarization, and community-driven Q&A capabilities.

### Key Benefits:
- **Centralized Material Management**: Upload, organize, and access all your study materials in one place
- **AI-Powered Learning**: Generate summaries and flashcards automatically from your materials
- **Community Collaboration**: Ask questions and get answers from fellow students
- **Smart Organization**: Bookmark materials and organize them by courses
- **Quality Assurance**: Rate and review materials to help the community

## ✨ Features

### 📚 Material Management
- **File Upload**: Support for PDF files up to 100MB with progress tracking
- **Advanced Search**: Search by course codes, subjects, tags, and content
- **Bookmark System**: Save materials for quick access and organization
- **Course Organization**: Group materials by course codes and subjects
- **Rating & Reviews**: Community-driven quality assessment system
- **Download Tracking**: Monitor material popularity and usage

### 🤖 AI-Powered Tools
- **Smart Summaries**: Generate concise, intelligent summaries from uploaded materials
- **Flashcard Creation**: AI-generated flashcards for effective studying and memorization
- **Content Analysis**: Extract key concepts, important information, and study points
- **Coin System**: Earn coins through uploads and spend them on AI features (1 coin = 1 AI generation)
- **Multiple Generation Types**: Choose between summary-only or comprehensive content analysis

### 💬 AskHub Community Platform
- **Q&A System**: Ask questions and receive answers from the student community
- **Priority System**: Mark questions as High, Medium, or Low priority for better organization
- **Anonymous Posting**: Option to post questions anonymously for privacy
- **Real-time Updates**: Live question and answer feed with instant notifications
- **Answer Management**: Submit answers with option to display your name or remain anonymous

### 👤 User Management & Profiles
- **Secure Authentication**: JWT-based authentication system with session management
- **Profile Dashboard**: Personal dashboard with statistics and activity tracking
- **Course Tracking**: Monitor your uploaded materials, bookmarks, and course progress
- **Activity History**: Track your contributions, uploads, and community interactions
- **Statistics Display**: View your coins, uploads, and review counts

## 🛠️ Technologies Used

### Backend Technologies
- **Java 21** - Modern Java with latest language features and performance improvements
- **Spring Boot 3.5.6** - Enterprise-grade application framework with auto-configuration
- **Spring Security** - Comprehensive authentication and authorization framework
- **Spring Data JPA** - Database abstraction layer with repository pattern
- **PostgreSQL** - Primary production database with ACID compliance
- **H2 Database** - In-memory database for development and testing
- **Flyway** - Database migration management and version control
- **JWT (JSON Web Tokens)** - Stateless authentication tokens
- **Apache PDFBox 3.0.1** - PDF processing and text extraction library

### Frontend Technologies
- **HTML5** - Semantic markup with modern web standards
- **CSS3** - Advanced styling with gradients, animations, and responsive design
- **JavaScript (ES6+)** - Modern JavaScript with async/await and fetch API
- **Responsive Design** - Mobile-first approach with flexible layouts
- **Custom Scrollbar Styling** - Branded UI components

### AI & External Services
- **OpenAI API** - GPT models for intelligent content generation
- **OpenAI Assistants API** - Advanced AI processing and file analysis
- **PDF Text Extraction** - Automated content processing from uploaded files

### DevOps & Deployment
- **Railway** - Cloud deployment platform with automatic scaling
- **Docker** - Containerization support for consistent deployments
- **Maven** - Dependency management and build automation
- **Git** - Version control and collaboration
- **Flyway Migrations** - Database schema versioning

## 📋 Prerequisites

Before setting up StuHub, ensure you have the following installed and configured:

### Required Software
- **Java 21** or higher - [Download from Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
- **Maven 3.6+** - [Download Maven](https://maven.apache.org/download.cgi)
- **PostgreSQL 12+** - [Download PostgreSQL](https://www.postgresql.org/download/) (for production)
- **Git** - [Download Git](https://git-scm.com/downloads)

### Required Accounts & Keys
- **OpenAI API Key** - [Get API Key](https://platform.openai.com/api-keys) (for AI features)
- **Railway Account** - [Sign up for Railway](https://railway.app/) (for deployment)

### System Requirements
- **RAM**: Minimum 2GB, Recommended 4GB+
- **Storage**: At least 1GB free space for dependencies and uploads
- **Network**: Stable internet connection for API calls and deployment

## 🚀 Setup Instructions

### Step 1: Clone the Repository
```bash
# Clone the repository
git clone https://github.com/harshach55/student-app.git

# Navigate to the backend directory
cd student-app/ffenf/backend
```

### Step 2: Environment Configuration
Create environment variables or a `.env` file with the following configuration:

```bash
# Database Configuration (Production)
DB_URL=jdbc:postgresql://localhost:5432/stuhub
DB_USERNAME=your_postgres_username
DB_PASSWORD=your_postgres_password

# Database Configuration (Development - Optional)
# Uses H2 in-memory database if not specified

# OpenAI Configuration (Required for AI features)
OPENAI_API_KEY=sk-your-openai-api-key-here

# JWT Secret (Generate a secure random string)
APP_JWT_SECRET=your-super-secure-jwt-secret-key-here

# File Storage Configuration
STORAGE_PATH=/path/to/your/uploads/directory

# Server Configuration (Optional)
PORT=8080
```

### Step 3: Database Setup

#### For Production (PostgreSQL):
```bash
# Create PostgreSQL database
createdb stuhub

# Or using psql
psql -U postgres
CREATE DATABASE stuhub;
\q
```

#### For Development (H2):
No additional setup required - H2 database will be created automatically.

### Step 4: Build the Application
```bash
# Clean and compile the project
mvn clean compile

# Run tests (optional)
mvn test
```

### Step 5: Run the Application
```bash
# Start the application
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

### Step 6: Verify Installation
1. Open your browser and navigate to `http://localhost:8080`
2. You should see the StuHub login/registration page
3. Create a new account or login with existing credentials
4. Test the material upload and AI generation features

## 🏗️ Project Structure

```
student-app/
├── ffenf/
│   ├── backend/
│   │   ├── src/main/java/com/ffenf/app/
│   │   │   ├── ai/                 # AI content generation services
│   │   │   │   ├── AiController.java
│   │   │   │   ├── AiSummaryService.java
│   │   │   │   ├── OpenAiFileService.java
│   │   │   │   └── PdfProcessingService.java
│   │   │   ├── askhub/             # Q&A platform
│   │   │   │   └── AskHubController.java
│   │   │   ├── auth/               # Authentication & JWT
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── JwtAuthFilter.java
│   │   │   │   └── JwtService.java
│   │   │   ├── config/             # Configuration classes
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── OpenAiConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── domain/             # Entity models
│   │   │   │   ├── User.java
│   │   │   │   ├── Material.java
│   │   │   │   ├── Question.java
│   │   │   │   └── Answer.java
│   │   │   ├── materials/          # Material management
│   │   │   │   └── MaterialsController.java
│   │   │   ├── profile/            # User profiles
│   │   │   │   └── ProfileController.java
│   │   │   ├── repo/               # Repository interfaces
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── MaterialRepository.java
│   │   │   │   └── QuestionRepository.java
│   │   │   └── storage/            # File storage service
│   │   │       └── FileStorageService.java
│   │   ├── src/main/resources/
│   │   │   ├── db/migration/       # Database migrations
│   │   │   │   ├── V1__init.sql
│   │   │   │   ├── V2__auth.sql
│   │   │   │   └── V3__course_bookmarks.sql
│   │   │   ├── static/             # Frontend files
│   │   │   │   └── index.html
│   │   │   └── application.properties
│   │   ├── Dockerfile              # Docker configuration
│   │   ├── pom.xml                 # Maven dependencies
│   │   └── railway.json            # Railway deployment config
│   └── frontend/                   # Frontend assets (if separate)
└── README.md
```

## 🔧 Configuration Details

### Application Properties
Key configuration options in `src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=${PORT:8080}

# Database Configuration
spring.datasource.url=${DB_URL:jdbc:h2:mem:devdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false}
spring.datasource.username=${DB_USERNAME:sa}
spring.datasource.password=${DB_PASSWORD:}

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=none
spring.jpa.open-in-view=false
spring.jpa.show-sql=${JPA_SHOW_SQL:false}

# Flyway Database Migration
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.validate-on-migrate=false

# File Upload Configuration
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
spring.servlet.multipart.enabled=true

# JWT Configuration
app.jwt.secret=${APP_JWT_SECRET:1B0D1704AA029EEB285A4B7857AEEF48FDC2A2782E33CD7375BF67FCAA5A138D}
app.jwt.ttlSeconds=3600

# OpenAI Configuration
openai.api.key=${OPENAI_API_KEY:}
openai.api.timeout=120000
openai.api.max-retries=3

# File Storage Configuration
app.storage.path=${STORAGE_PATH:/tmp/uploads}

# Logging Configuration
logging.level.com.ffenf.app.materials=DEBUG
logging.level.com.ffenf.app.storage=DEBUG
```

### Security Configuration
- **JWT Authentication**: Stateless token-based authentication
- **CORS Enabled**: Cross-origin resource sharing for frontend integration
- **Public Endpoints**: Health checks, authentication, and material search
- **Protected Endpoints**: User-specific operations and AI generation
- **Role-based Access**: USER role with appropriate permissions

## 📊 API Documentation

### Authentication Endpoints
```
POST /auth/register
- Register a new user account
- Body: { "name": "string", "email": "string", "password": "string" }

POST /auth/login
- Authenticate user and receive JWT token
- Body: { "email": "string", "password": "string" }
- Response: { "token": "string", "user": { ... } }
```

### Material Management Endpoints
```
POST /materials/upload
- Upload new material file
- Headers: Authorization: Bearer <token>
- Body: multipart/form-data with file and metadata

GET /materials/search
- Search materials by query, course, or subject
- Query params: ?q=search_term&course=course_code&subject=subject_name

GET /materials/{id}
- Get material details and metadata
- Response: { "id": "uuid", "title": "string", "subject": "string", ... }

GET /materials/{id}/file
- Download material file
- Headers: Authorization: Bearer <token>
```

### AI Tools Endpoints
```
GET /ai/generate/{materialId}?type=summary
- Generate AI content from material
- Headers: Authorization: Bearer <token>
- Query params: type=summary|flashcards
- Response: { "content": "string", "remainingCoins": number }

GET /ai/debug/{materialId}
- Debug AI processing (development only)
- Headers: Authorization: Bearer <token>
```

### AskHub Q&A Endpoints
```
GET /askhub/questions
- Get all questions with pagination
- Query params: ?page=0&size=10

POST /askhub/questions
- Create new question
- Headers: Authorization: Bearer <token>
- Body: { "title": "string", "content": "string", "priority": "HIGH|MEDIUM|LOW" }

POST /askhub/questions/{id}/answers
- Submit answer to question
- Headers: Authorization: Bearer <token>
- Body: { "content": "string", "displayName": "string|anonymous" }
```

### Profile Management Endpoints
```
GET /profile/me
- Get current user profile and statistics
- Headers: Authorization: Bearer <token>

GET /profile/me/materials
- Get user's uploaded materials
- Headers: Authorization: Bearer <token>

GET /profile/me/courses
- Get user's bookmarked courses
- Headers: Authorization: Bearer <token>

POST /profile/me/courses/bookmark?courseCode=COURSE123
- Bookmark a course
- Headers: Authorization: Bearer <token>
```

## 🚀 Deployment Guide

### Railway Deployment (Recommended)
1. **Connect Repository**:
   - Go to [Railway Dashboard](https://railway.app/dashboard)
   - Click "New Project" → "Deploy from GitHub repo"
   - Select your `student-app` repository

2. **Configure Environment Variables**:
   ```
   DB_URL=postgresql://username:password@host:port/database
   OPENAI_API_KEY=sk-your-openai-api-key
   APP_JWT_SECRET=your-secure-jwt-secret
   STORAGE_PATH=/tmp/uploads
   ```

3. **Deploy**:
   - Railway will automatically detect Java/Maven project
   - Build and deploy will start automatically
   - Your app will be available at `https://your-app-name.railway.app`

### Docker Deployment
```bash
# Build Docker image
docker build -t stuhub-backend .

# Run with environment variables
docker run -p 8080:8080 \
  -e DB_URL=your_postgres_url \
  -e OPENAI_API_KEY=your_openai_key \
  -e APP_JWT_SECRET=your_jwt_secret \
  stuhub-backend
```

### Local Production Setup
```bash
# Build JAR file
mvn clean package -DskipTests

# Run with production profile
java -jar target/backend-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --DB_URL=your_production_db_url \
  --OPENAI_API_KEY=your_openai_key
```

## 🤝 Contributing

We welcome contributions to StuHub! Here's how you can help:

### Development Workflow
1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/your-username/student-app.git
   cd student-app
   ```
3. **Create a feature branch**:
   ```bash
   git checkout -b feature/amazing-new-feature
   ```
4. **Make your changes** and test thoroughly
5. **Commit your changes**:
   ```bash
   git commit -m 'Add amazing new feature'
   ```
6. **Push to your fork**:
   ```bash
   git push origin feature/amazing-new-feature
   ```
7. **Open a Pull Request** on GitHub

### Contribution Guidelines
- Follow Java coding standards and Spring Boot best practices
- Write clear commit messages
- Add tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

### Areas for Contribution
- **Frontend Improvements**: UI/UX enhancements, responsive design
- **AI Features**: New content generation types, improved algorithms
- **Performance**: Database optimization, caching strategies
- **Security**: Enhanced authentication, input validation
- **Testing**: Unit tests, integration tests, end-to-end tests

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Harsha Chodavarapu**
- GitHub: [@harshach55](https://github.com/harshach55)
- Email: harshach55@gmail.com
- LinkedIn: [Harsha Chodavarapu](https://linkedin.com/in/harsha-chodavarapu)

## 🙏 Acknowledgments

- **OpenAI** for providing powerful AI capabilities and API access
- **Spring Boot Community** for excellent documentation and framework
- **Railway** for seamless cloud deployment platform
- **PostgreSQL Community** for robust database solutions
- **All Contributors** and users of StuHub for feedback and improvements

## 📞 Support & Contact

### Getting Help
1. **Check Documentation**: Review this README and code comments
2. **Search Issues**: Look through [existing issues](https://github.com/harshach55/student-app/issues)
3. **Create Issue**: [Open a new issue](https://github.com/harshach55/student-app/issues/new) with detailed description
4. **Contact Maintainer**: Reach out directly via email or GitHub

### Issue Reporting
When reporting issues, please include:
- **Environment**: OS, Java version, Maven version
- **Steps to Reproduce**: Detailed steps to recreate the issue
- **Expected Behavior**: What should happen
- **Actual Behavior**: What actually happens
- **Error Messages**: Any error logs or console output
- **Screenshots**: If applicable, include screenshots

---

**StuHub** - Empowering students with intelligent material management and collaborative learning tools. 🚀📚✨