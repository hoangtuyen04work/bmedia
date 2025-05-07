FROM openjdk:21-jdk-slim

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR
COPY target/social-backend-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng ứng dụng
EXPOSE 8888

# Chạy ứng dụng với tham số JVM tối ưu
ENTRYPOINT ["java", "-Xms512m", "-Xmx1024m", "-jar", "app.jar"]