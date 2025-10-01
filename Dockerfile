# Multi-stage Dockerfile for SecureVote app

# Stage 1: Build Android APK
FROM openjdk:11-jdk-slim AS android-builder

# Install Android SDK dependencies
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Set Android SDK environment variables
ENV ANDROID_HOME=/opt/android-sdk
ENV PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

# Download and install Android SDK
RUN mkdir -p $ANDROID_HOME && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip && \
    unzip commandlinetools-linux-9477386_latest.zip -d $ANDROID_HOME && \
    rm commandlinetools-linux-9477386_latest.zip

# Accept Android SDK licenses
RUN yes | $ANDROID_HOME/cmdline-tools/bin/sdkmanager --licenses

# Install required SDK components
RUN $ANDROID_HOME/cmdline-tools/bin/sdkmanager \
    "platform-tools" \
    "platforms;android-34" \
    "build-tools;34.0.0"

# Copy project files
WORKDIR /app
COPY . .

# Build Android APK
RUN ./gradlew assembleDebug

# Stage 2: Python Backend
FROM python:3.11-slim AS backend

WORKDIR /app

# Copy requirements and install dependencies
COPY stripe_backend/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy backend code
COPY stripe_backend/ .

# Expose port
EXPOSE 4242

# Run the Flask app
CMD ["gunicorn", "--bind", "0.0.0.0:4242", "app:app"]

# Stage 3: Final image with both APK and backend
FROM python:3.11-slim

WORKDIR /app

# Install dependencies
COPY stripe_backend/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copy backend code
COPY stripe_backend/ .

# Copy built APK from android-builder stage
COPY --from=android-builder /app/app/build/outputs/apk/debug/app-debug.apk ./securevote.apk

# Expose port
EXPOSE 4242

# Run the Flask app
CMD ["gunicorn", "--bind", "0.0.0.0:4242", "app:app"]
