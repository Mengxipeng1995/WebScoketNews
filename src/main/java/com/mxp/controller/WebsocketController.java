package com.mxp.controller;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Controller
public class WebsocketController {

    private  final  static Set<String> OnlineUser = new HashSet<String>();

    @Bean//这个注解会从Spring容器拿出Bean
    public myHandler infoHandler() {
        return new myHandler();
    }

    @RequestMapping("/myHandler/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
        String username = request.getParameter("username");
        System.out.println(username + "登录");
       // if (!(OnlineUser.contains(username))){
            session.setAttribute("SESSION_USERNAME", username);
       // }
        //response.sendRedirect("/quicksand/jsp/websocket.jsp");
        return new ModelAndView("redirect:/scoket");
    }

    @RequestMapping("/scoket")
    public String scoket(HttpSession session,HttpServletRequest request){

        String username = (String) session.getAttribute("SESSION_USERNAME");
        OnlineUser.add(username);
        request.setAttribute("allUsers",OnlineUser);

        return "scoket";
    }

    @RequestMapping("/myHandler/send")
    public String send(HttpServletRequest request, HttpSession session) throws Exception {
        String username = (String) session.getAttribute("SESSION_USERNAME");
        infoHandler().sendMessageToUser(username, new TextMessage("你好，测试！！！！"));
        return null;
    }

    @RequestMapping("/myHandler/sendToUserJsp")
    public String sendToUserJsp(HttpServletRequest request, HttpSession session) throws Exception {
        String username = request.getParameter("username");
        request.setAttribute("toUser",username);
        return "scoketToUser";
    }

    @RequestMapping("/myHandler/sendToUser")
    public void sendToUser(HttpServletRequest request, HttpSession session) throws Exception {
        String text = request.getParameter("text");
        String username = request.getParameter("username");
       infoHandler().sendMessageToUser(username, new TextMessage(text));
    }

    @RequestMapping("/myHandler/logout")
    public void logout(HttpServletRequest request) {
        String username = request.getParameter("username");
        boolean result = myHandler.ConnectionClosed(username);
        if (result){
            OnlineUser.remove(username);
            System.out.println(username + "已退出");
        }else {
            System.out.println(username + "未登录");
        }
    }
}