package com.mxp.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;



@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/myHandler/socketServer").setAllowedOrigins("*").addInterceptors(new HandshakeInterceptor());
        registry.addHandler(myHandler(), "/myHandler/socketServer").setAllowedOrigins("*").addInterceptors(new HandshakeInterceptor()).withSockJS();
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new myHandler();
    }

}