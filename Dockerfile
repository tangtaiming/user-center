FROM maven:3.5-jdk-8-alpine as builder

WORKDIR /data/app

# Copy local code to the container image.
COPY ./user-center-0.0.1.jar /data/app/

# Build a release artifact.

# Run the web service on container startup
CMD ["java", "-jar", "/data/app/user-center-0.0.1.jar", "--spring.profiles.active=prod"]
