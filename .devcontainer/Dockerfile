FROM eclipse-temurin:21-jdk

# Install required packages
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Install Gradle
ARG GRADLE_VERSION=9.2.0
ARG GRADLE_HOME=/opt/gradle

RUN wget -q https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && unzip -q gradle-${GRADLE_VERSION}-bin.zip -d /opt \
    && mv /opt/gradle-${GRADLE_VERSION} ${GRADLE_HOME} \
    && rm gradle-${GRADLE_VERSION}-bin.zip

# Add Gradle to PATH
ENV PATH="${GRADLE_HOME}/bin:${PATH}"

