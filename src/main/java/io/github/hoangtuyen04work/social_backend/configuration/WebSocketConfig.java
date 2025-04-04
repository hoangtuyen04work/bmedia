package io.github.hoangtuyen04work.social_backend.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//    @Autowired
//    private T
    @Autowired
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //end point cua simple broker
        // broker la tang trung gian giua client va server, khi sever muon  gui 1 thong bao nao do thi tin nhan tu
        //server se duoc gui len broker de gui ti nhan do di dua tren cac endpoint, va cac client dang lang nghe tai
        //end point do se nhan duoc tin nhan
        config.enableSimpleBroker("/topic", "/queue", "/user");
        //khi client muon gui ti nhan den server thi phai co prefix nay, prefix nay duoc dung de danh dau day la 1
        // 1 ti nhan tu client va can duoc controller xu ly, con neu khong co prefix nay thi tin nhan se duoc xem
        //nhu la 1 broadcast de gui toi cac endpoint ma client dang subcribe
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .addInterceptors(jwtHandshakeInterceptor); // Đăng ký Interceptor
    }
}
