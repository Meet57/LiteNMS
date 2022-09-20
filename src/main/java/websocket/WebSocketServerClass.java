package websocket;

import org.json.JSONObject;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/websocket/endpoint")
public class WebSocketServerClass {

    private Session session;
    private static HashMap<String, Session> users = new HashMap<>();

    @OnOpen
    public void handleOpen(Session session) {

        try {

            JSONObject jo = new JSONObject();

            jo.put("socketId", session.getId());

            jo.put("type", "socketId");

            users.put(session.getId(), session);

            session.getBasicRemote().sendText(String.valueOf(jo));

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static void sendMessage(String id, HashMap<String, Object> message) {

        try {

            JSONObject jo = new JSONObject(message);

            users.get(id).getBasicRemote().sendText(jo.toString());

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public static void sendBroadcast(HashMap<String, String> message) {

        try {

            JSONObject jo = new JSONObject(message);

            for (String id : users.keySet()) {
                users.get(id).getBasicRemote().sendText(jo.toString());
            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    @OnMessage

    public String handleMessage(String message) {
        return message + " [SERVER]";
    }

    @OnClose
    public void handleClose(Session session) {
        users.remove(session.getId());
    }

    @OnError
    public void handleError(Throwable throwable) {
        throwable.printStackTrace();
    }

}

