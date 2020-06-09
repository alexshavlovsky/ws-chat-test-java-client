package mockclient;

import java.util.Collection;

interface ChatSnapshotHandler {
    Collection<ChatClient> getClients();

    void handleSnapshot(ChatSnapshot snapshot);

    void handleSnapshotUpdate(ChatSnapshotUpdate update);
}
