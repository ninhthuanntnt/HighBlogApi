package com.high.highblog.security;

import com.high.highblog.bloc.JwtBloc;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {

    private final JwtBloc jwtBloc;

    private static final String AUTH_HEADER = "Authorization";

    public AuthChannelInterceptorAdapter(final JwtBloc jwtBloc) {
        this.jwtBloc = jwtBloc;
    }

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            final String autHeader = accessor.getFirstNativeHeader(AUTH_HEADER);
            if (autHeader != null && autHeader.startsWith("Bearer" + " ")) {
                String token = autHeader.replace("Bearer" + " ", "").trim();

                jwtBloc.validateToken(token);

                Authentication authentication =
                        jwtBloc.getUserDetailsFromToken(token)
                               .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails,
                                                                                           null,
                                                                                           userDetails
                                                                                                   .getAuthorities()))
                               .orElse(null);

                accessor.setLeaveMutable(false);
                accessor.setUser(authentication);
            }
        }

        return message;
    }

    @Override
    public Message<?> postReceive(final Message<?> message, final MessageChannel channel) {
        return ChannelInterceptor.super.postReceive(message, channel);
    }

    @Override
    public void afterReceiveCompletion(final Message<?> message, final MessageChannel channel, final Exception ex) {
        ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
    }
}
