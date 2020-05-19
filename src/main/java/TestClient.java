import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

public class TestClient extends WebSocketClient {
    private static URI serverUri;
    private static int instanceCounter = 0;

    static {
        try {
            serverUri = new URI("ws://localhost:8080/ws/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private TestClient(URI serverUri) {
        super(serverUri);
    }

    private final String clientId = UUID.randomUUID().toString();
    private final String nick = "Bot " + ("" + (1000 + ++instanceCounter)).substring(1);
    private int frameId = 0;

    static TestClient newInstance() {
        return new TestClient(serverUri);
    }

    private void sendTypedMessage(String type, String payload) {
        ClientMessageModel msg = new ClientMessageModel();
        msg.setFrameId(frameId++);
        msg.setClientId(clientId);
        msg.setUserNick(nick);
        msg.setType(type);
        msg.setPayload(payload);
        Gson gson = new Gson();
        send(gson.toJson(msg));
    }

    private void updateClientDetails() {
        sendTypedMessage("updateMe", "");
    }

    void setTyping() {
        sendTypedMessage("setTyping", "");
    }

    void sendMsg(String text) {
        sendTypedMessage("msg", text);
    }

    @Override
    public void onMessage(String message) {
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        updateClientDetails();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if (remote)
            System.out.println("You have been disconnected from: " + getURI() + "; Code: " + code + " " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Exception occurred ...\n" + ex + "\n");
    }
}
