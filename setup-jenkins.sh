#!/bin/bash

# Jenkins Pipeline Setup Script for Android Voting Application
# This script helps set up the Jenkins CI/CD pipeline environment

set -e

echo "🚀 Setting up Jenkins CI/CD Pipeline for Android Voting Application"
echo "=================================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is installed
check_docker() {
    print_status "Checking Docker installation..."
    if command -v docker &> /dev/null; then
        print_success "Docker is installed"
        docker --version
    else
        print_error "Docker is not installed. Please install Docker first."
        exit 1
    fi
}

# Check if Docker Compose is installed
check_docker_compose() {
    print_status "Checking Docker Compose installation..."
    if command -v docker-compose &> /dev/null; then
        print_success "Docker Compose is installed"
        docker-compose --version
    else
        print_error "Docker Compose is not installed. Please install Docker Compose first."
        exit 1
    fi
}

# Check if Java is installed
check_java() {
    print_status "Checking Java installation..."
    if command -v java &> /dev/null; then
        print_success "Java is installed"
        java -version
    else
        print_warning "Java is not installed. Please install Java 11 or higher."
    fi
}

# Check if Git is installed
check_git() {
    print_status "Checking Git installation..."
    if command -v git &> /dev/null; then
        print_success "Git is installed"
        git --version
    else
        print_error "Git is not installed. Please install Git first."
        exit 1
    fi
}

# Create necessary directories
create_directories() {
    print_status "Creating necessary directories..."
    mkdir -p jenkins-data
    mkdir -p sonarqube-data
    mkdir -p sonarqube-logs
    mkdir -p sonarqube-extensions
    print_success "Directories created"
}

# Start services with Docker Compose
start_services() {
    print_status "Starting services with Docker Compose..."
    
    # Set environment variables
    export BUILD_NUMBER=1
    export GIT_COMMIT_SHORT=$(git rev-parse --short HEAD 2>/dev/null || echo "local")
    
    # Start services
    docker-compose up -d
    
    print_success "Services started"
    print_status "Waiting for services to be ready..."
    
    # Wait for services to be ready
    sleep 30
    
    # Check service health
    check_service_health
}

# Check service health
check_service_health() {
    print_status "Checking service health..."
    
    # Check Jenkins
    if curl -s http://localhost:8081/login > /dev/null; then
        print_success "Jenkins is running at http://localhost:8081"
    else
        print_warning "Jenkins might not be ready yet. Please wait a few minutes."
    fi
    
    # Check SonarQube
    if curl -s http://localhost:9000 > /dev/null; then
        print_success "SonarQube is running at http://localhost:9000"
    else
        print_warning "SonarQube might not be ready yet. Please wait a few minutes."
    fi
    
    # Check Android Voting App
    if curl -s http://localhost:8080 > /dev/null; then
        print_success "Android Voting App is running at http://localhost:8080"
    else
        print_warning "Android Voting App might not be ready yet. Please wait a few minutes."
    fi
}

# Display setup instructions
display_instructions() {
    echo ""
    echo "🎉 Setup completed successfully!"
    echo "================================"
    echo ""
    echo "Next steps:"
    echo "1. Access Jenkins at: http://localhost:8081"
    echo "   - Initial admin password: Check docker-compose logs"
    echo "   - Run: docker-compose logs jenkins | grep 'Please use the following password'"
    echo ""
    echo "2. Access SonarQube at: http://localhost:9000"
    echo "   - Default credentials: admin/admin"
    echo "   - Create a new project with key: android-voting-app"
    echo "   - Generate a token and add it to Jenkins credentials"
    echo ""
    echo "3. Access Android Voting App at: http://localhost:8080"
    echo "   - Download the APK for testing"
    echo ""
    echo "4. Configure Jenkins Pipeline:"
    echo "   - Install required plugins (see README-Jenkins-Pipeline.md)"
    echo "   - Create a new Pipeline job"
    echo "   - Point it to your Git repository"
    echo "   - Use the Jenkinsfile in the root directory"
    echo ""
    echo "5. Run the pipeline and monitor the results"
    echo ""
    echo "For detailed instructions, see README-Jenkins-Pipeline.md"
    echo ""
}

# Main execution
main() {
    echo "Starting setup process..."
    echo ""
    
    # Check prerequisites
    check_docker
    check_docker_compose
    check_java
    check_git
    
    echo ""
    print_status "All prerequisites checked successfully!"
    echo ""
    
    # Create directories
    create_directories
    
    # Start services
    start_services
    
    # Display instructions
    display_instructions
}

# Run main function
main "$@"
