<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Insert title here</title>
    <script type="text/javascript" src="js/jquery-2.1.1.js"></script>
    <script type="text/javascript" src="js/sockjs.js"></script>
    <script type="text/javascript">
        var websocket = null;
        if ('WebSocket' in window) {
            websocket = new WebSocket("ws://localhost:8080/myHandler/socketServer");
        }
        else if ('MozWebSocket' in window) {
            websocket = new MozWebSocket("ws://localhost:8080/myHandler/socketServer");
        }
        else {
            websocket = new SockJS("http://localhost:8080/myHandler/socketServer");
        }
        websocket.onopen = onOpen;
        websocket.onmessage = onMessage;
        websocket.onerror = onError;
        websocket.onclose = onClose;

        function onOpen(openEvt) {
            //alert(openEvt.Data);
        }

        function onMessage(evt) {
            alert(evt.data);
            var testdiv = document.getElementById("ltk");
            alert(testdiv.innerHTML);
            testdiv.innerHTML +="<p>"+ evt.data +"</p> <br/>";
        }
        function onError() {}
        function onClose() {
            location.href="/myHandler/logout?username=";
        }

        function doSend() {
            if (websocket.readyState == websocket.OPEN) {
                 var msg = document.getElementById("inputMsg").value;
                 //websocket.send(msg);//调用后台handleTextMessage方法
                $.ajax({
                    url:"/myHandler/sendToUser?username=${toUser}&text="+msg,
                })

            }
        }
        window.close=function()
        {
            websocket.onclose();
        }

    </script>
</head>
<body>

<div>
    在线用户：
    <c:forEach items="${allUsers}" var="users">
        <a href="/myHandler/sendToUserJsp?username=${users}">${users}</a>
    </c:forEach>
</div>
<div id="ltk">
<p>输入内容:</p><br/>
</div>
请输入：
<textarea rows="5" cols="10" id="inputMsg" name="text"></textarea>
<button onclick="doSend();">发送</button>
</body>
</html>