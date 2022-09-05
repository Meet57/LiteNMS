package websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/endpoint")
public class WebSocketServerClass {

    ConcurrentHashMap<String, Session> sessionConcurrentHashMap = new ConcurrentHashMap<>();

    @OnOpen
    public void handleOpen(Session session) {
        try {
            sessionConcurrentHashMap.put(session.getId(), session);
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

