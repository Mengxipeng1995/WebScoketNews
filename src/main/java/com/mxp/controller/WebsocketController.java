package com.mxp.controller;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class WebsocketController {
  @Bean//这个注解会从Spring容器拿出Bean
    public myHandler infoHandler() {
        return new myHandler();
    }

    @RequestMapping("/myHandler/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        System.out.println(username+"登录");
        HttpSession session = request.getSession(true);
        session.setAttribute("SESSION_USERNAME", username);
        //response.sendRedirect("/quicksand/jsp/websocket.jsp");
        return new ModelAndView("scoket");
    }

    @RequestMapping("/myHandler/send")
    @ResponseBody
    public void send(HttpServletRequest request,HttpSession session) throws Exception {
        String username = (String) session.getAttribute("SESSION_USERNAME");
        infoHandler().sendMessageToUser(username, new TextMessage("你好，测试！！！！"));
    }
}