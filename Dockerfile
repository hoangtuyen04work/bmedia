# Sử dụng image OpenJDK slim để giảm kích thước
FROM openjdk:21-jdk-slim

# Cài đặt các công cụ cần thiết (nếu cần)
RUN apt-get update && apt-get install -y --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR từ thư mục target
COPY target/social-backend-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng ứng dụng
EXPOSE 8888

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]