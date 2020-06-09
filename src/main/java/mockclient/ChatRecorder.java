package mockclient;

import java.util.Collection;

public interface ChatRecorder {
    Collection<ServerMessage> getServerMessages();

    Collection<ChatClient> getChatClients();
}
