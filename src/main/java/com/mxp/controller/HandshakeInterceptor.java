package com.mxp.controller;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Map<String, Object> map) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            //访问地址与前台webscoket IP 地址要一样，否则这个位置获取不到session 注意:localhost  和 127.0.0.1是不同的
            HttpSession session = serverHttpRequest.getServletRequest().getSession(false);
            WebsocketController wb = new WebsocketController();

            if (session != null) {
                //   map.put("userId", session.getAttribute("userId"));
                //使用userName区分WebSocketHandler，以便定向发送消息
                String userName = (String) session.getAttribute("SESSION_USERNAME");
                session.getAttribute(userName);
                if (userName == null) {
                    return false;
                }
                if (map.containsValue(userName)){
                    return false;
                }
                map.put("WEBSOCKET_USERNAME", userName);
            }

        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        System.out.println("this is afterHandshake");
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
