package websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/endpoint")
public class WebSocketServerClass {
    @OnOpen
    public void handleOpen(Session session) {
        try {
            session.getBasicRemote().sendText("Hi");
            session.getBasicRemote().sendText("Hi From server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public String handleMessage(String message) {
        return message + " [SERVER]";
    }

    @OnClose
    public void handleClose() {
        System.out.println("Client is now disconnected....");
    }

    @OnError
    public void handleError(Throwable throwable) {
        throwable.printStackTrace();
    }
}

