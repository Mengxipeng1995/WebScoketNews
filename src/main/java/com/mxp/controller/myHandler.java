package com.mxp.controller;

import com.mxp.dao.UserMapper;
import com.mxp.entiy.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;


/**
 * 推送即将要处理完成的受理单给处理人
 * <p>
 * a.在afterConnectionEstablished连接建立成功之后，记录用户的连接标识，便于后面发信息，这里我是记录将id记录在Map集合中。
 * <p>
 * b.在handleTextMessage中可以对H5 Websocket的send方法进行处理
 * <p>
 * c.sendMessageToUser向指定用户发送消息，传入用户标识和消息体
 * <p>
 * d.sendMessageToAllUsers向左右用户广播消息，只需要传入消息体
 * <p>
 * e.handleTransportError连接出错处理，主要是关闭出错会话的连接，和删除在Map集合中的记录
 * <p>
 * f.afterConnectionClosed连接已关闭，移除在Map集合中的记录。
 * <p>
 * g.getClientId我自己封装的一个方法，方便获取用户标识
 */
public class myHandler extends TextWebSocketHandler {

    private static final Map<WebSocketSession,String> users = new HashMap<WebSocketSession,String>();//这个会出现性能问题，最好用Map来存储，key用userid
    //private static Logger logger = Logger.getLogger(SpringWebSocketHandler.class);
    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private UserDao userDao;

    private Timer timer;


    public myHandler() {
        // TODO Auto-generated constructor stub
    }


    /**
     * 在afterConnectionEstablished连接建立成功之后，记录用户的连接标识，便于后面发信息
     * 会触发open方法
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub

        Map<String, Object> map = session.getAttributes();
        String username = (String) map.get("WEBSOCKET_USERNAME");

        if (users.containsValue(username)){
            ConnectionClosed(username);
            users.put(session, username);
            System.out.println(username + "已登录");
        }else {
            users.put(session, username);
            System.out.println("connect to the websocket success......当前数量:" + users.size());
        }
        //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
        //TextMessage returnMessage = new TextMessage("你将收到的离线");
        //session.sendMessage(returnMessage);
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {
        if (!session.isOpen()) {
            timer.cancel();
            return;
        }
        super.handleTextMessage(session, message);
        session.sendMessage(message);
    }


    class OrderTimeTask extends TimerTask {
        //  private User user;
        private WebSocketSession session;

//        public OrderTimeTask(User user,WebSocketSession session){
//            this.user = user;
//            this.session = session;
//        }

        @Override
        public void run() {
            try {
                //    String reminder = acceptedWorkOrderService.getLastReminderOrder(user.getId());
                //   TextMessage textMessage = new TextMessage(reminder);
                //  handleMessage(session,textMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {
        Set<WebSocketSession> sendUser = users.keySet();
        for (WebSocketSession user : sendUser) {
            // if (user.getAttributes().get("username").equals(userName)) {
            Map<String, Object> map = user.getAttributes();
            System.out.println(map.get("WEBSOCKET_USERNAME"));
            if (map.get("WEBSOCKET_USERNAME") != null) {
                if ((map.get("WEBSOCKET_USERNAME")).equals(userName)) {
                    try {
                        if (user.isOpen()) {
                            user.sendMessage(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }


    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        Set<WebSocketSession> sendAllUser = users.keySet();
        for (WebSocketSession user : sendAllUser) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean ConnectionClosed(String username){

        for(Map.Entry entry:users.entrySet()){
            if(username.equals(entry.getValue())){
                users.remove(entry.getKey());
                return true;
            }
        }
        return false;
    }

//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//       ConnectionClosed("");
//        System.out.println("Connection Closed！");
//    }
//

}