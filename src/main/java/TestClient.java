import mockclient.MockChatClient;

public interface TestClient {
    void connect() throws InterruptedException;

    void close() throws InterruptedException;

    MockChatClient getChat();
}
