package websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@ServerEndpoint("/websocket/endpoint")
public class WebSocketServerClass {

    private Session session;
    private static HashMap<String, Session> users = new HashMap<>();
    @OnOpen
    public void handleOpen(Session session) {
        try {
            users.put(session.getId(),session);
            session.getBasicRemote().sendText("id:"+session.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String id,HashMap<String,String> message){
        try {
            users.get(id).getBasicRemote().sendText(message.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public static void sendMessgae(String s){

    }
}

