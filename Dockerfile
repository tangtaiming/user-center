FROM maven:3.5-jdk-8-alpine as builder

# Copy local code to the container image.

# Build a release artifact.

# Run the web service on container startup
CMD ["java", "-jar", "/www/wwwroot/data/app/user-center-0.0.1.jar", "--spring.profiles.active=prod"]