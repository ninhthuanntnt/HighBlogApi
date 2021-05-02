package com.high.highblog.config;

import com.high.highblog.security.AuthChannelInterceptorAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebsocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthChannelInterceptorAdapter authChannelInterceptorAdapter;

    public WebsocketAuthenticationConfig(final AuthChannelInterceptorAdapter authChannelInterceptorAdapter) {
        this.authChannelInterceptorAdapter = authChannelInterceptorAdapter;
    }

    @Override
    public void configureClientInboundChannel(final ChannelRegistration registration) {
        registration.interceptors(authChannelInterceptorAdapter);
    }
}
