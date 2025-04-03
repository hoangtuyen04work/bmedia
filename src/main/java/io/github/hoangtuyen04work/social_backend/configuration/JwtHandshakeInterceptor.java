package io.github.hoangtuyen04work.social_backend.configuration;

import io.github.hoangtuyen04work.social_backend.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
            String token = httpServletRequest.getParameter("token"); // Get token from query parameter
            if (token != null && !token.isEmpty()) {
                try {
                    tokenUtils.isValidToken(token); // Validate the token
                    String userId = tokenUtils.getUserId(token); // Extract userId
                    attributes.put("user", userId); // Store userId in attributes
                    return true; // Allow the handshake to proceed
                } catch (Exception e) {
                    System.out.println("Invalid token: " + e.getMessage());
                    return false; // Deny the handshake if token is invalid
                }
            }
        }
        return false; // Deny the handshake if token is missing
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}