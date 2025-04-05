package io.github.hoangtuyen04work.social_backend;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SocialBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SocialBackendApplication.class, args);
	}
	@PostConstruct
	public void init() {
		// Đặt múi giờ mặc định cho ứng dụng là Asia/Ho_Chi_Minh (UTC+7)
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
	}
}
