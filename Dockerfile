# Multi-stage Dockerfile for Android Voting Application
# Stage 1: Build the Android application
FROM openjdk:11-jdk-slim AS build

# Install necessary packages
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Set environment variables
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools

# Install Android SDK
RUN mkdir -p ${ANDROID_HOME} && \
    cd ${ANDROID_HOME} && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip && \
    unzip commandlinetools-linux-9477386_latest.zip && \
    rm commandlinetools-linux-9477386_latest.zip && \
    mkdir -p cmdline-tools/latest && \
    mv cmdline-tools/* cmdline-tools/latest/ 2>/dev/null || true

# Accept Android SDK licenses
RUN yes | ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager --licenses

# Install required Android SDK components
RUN ${ANDROID_HOME}/cmdline-tools/latest/bin/sdkmanager \
    "platform-tools" \
    "platforms;android-35" \
    "build-tools;35.0.0" \
    "extras;android;m2repository" \
    "extras;google;m2repository"

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Build the Android application
RUN ./gradlew clean assembleDebug

# Stage 2: Create deployment image
FROM nginx:alpine AS deployment

# Install Python for simple HTTP server
RUN apk add --no-cache python3 py3-pip

# Copy the built APK
COPY --from=build /app/app/build/outputs/apk/debug/app-debug.apk /usr/share/nginx/html/

# Create a simple HTML page for APK download
RUN echo '<!DOCTYPE html>\
<html>\
<head>\
    <title>Android Voting App - Test Deployment</title>\
    <style>\
        body { font-family: Arial, sans-serif; margin: 40px; }\
        .container { max-width: 600px; margin: 0 auto; }\
        .download-btn { display: inline-block; padding: 12px 24px; background: #007bff; color: white; text-decoration: none; border-radius: 4px; }\
        .download-btn:hover { background: #0056b3; }\
    </style>\
</head>\
<body>\
    <div class="container">\
        <h1>Android Voting Application</h1>\
        <p>Test deployment of the Android Voting Application</p>\
        <p>Build: ${BUILD_NUMBER}</p>\
        <p>Commit: ${GIT_COMMIT_SHORT}</p>\
        <a href="app-debug.apk" class="download-btn">Download APK</a>\
    </div>\
</body>\
</html>' > /usr/share/nginx/html/index.html

# Expose port 80
EXPOSE 80

# Start nginx
CMD ["nginx", "-g", "daemon off;"]