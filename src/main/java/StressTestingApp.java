import lombok.extern.slf4j.Slf4j;
import mockclient.MockChatClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class StressTestingApp {

    private static int botsInitialNum = 100;
    private static int botsMaxNum = 500;

    private static List<TestClient> bots = new ArrayList<>();

    private static void addBot() throws InterruptedException {
        TestClient client = WsTestClient.newInstance("ws://localhost:8080/ws/", true);
        bots.add(client);
        client.connect();
    }

    public static void main(String[] args) throws InterruptedException {
        log.info("Initial bots: {}", botsInitialNum);
        log.info("Instantiating bots...");
        while (bots.size() < botsInitialNum) addBot();
        Thread.sleep(1000);
        log.info("Target bots: " + botsMaxNum);
        int typingMesCount = 0;
        int textMesCount = 0;
        while (bots.size() < botsMaxNum) {
            Thread.sleep(100);
            int i = new Random().nextInt(bots.size());
            MockChatClient chat = bots.get(i).getChat();
            chat.sendSetTyping();
            typingMesCount++;
            if (new Random().nextInt(2) == 0) {
                chat.sendMsg(UUID.randomUUID().toString());
                textMesCount++;
            }
            addBot();
            if (bots.size() % 10 == 0)
                log.info("Bots (actual/target): {}/{}, messages (text/typing): {}/{}", bots.size(), botsMaxNum, textMesCount, typingMesCount);
        }
        int msgCount = bots.stream().map(b -> b.getChat().getServerMessages().size()).mapToInt(x -> x).sum();
        log.info("Disconnecting bots...");
        for (TestClient bot : bots) bot.close();
        log.info("Total messages processed by server: {}", msgCount);
    }
}
