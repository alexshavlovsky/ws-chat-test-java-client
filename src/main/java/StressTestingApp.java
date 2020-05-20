import lombok.extern.slf4j.Slf4j;
import org.java_websocket.enums.ReadyState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class StressTestingApp {

    private static int botsInitialNum = 200;
    private static int botsMaxNum = 500;

    private static List<TestClient> bots = new ArrayList<>();

    private static void addBot() throws InterruptedException {
        TestClient client = TestClient.newInstance();
        bots.add(client);
        client.connectBlocking();
    }

    public static void main(String[] args) throws InterruptedException {
        log.info("Initial bots: {}", botsInitialNum);
        log.info("Instantiating bots...");
        while (bots.size() < botsInitialNum) addBot();
        Thread.sleep(1000);
        bots.forEach(TestClient::startRecording);
        log.info("Target bots: " + botsMaxNum);
        int typingMesCount = 0;
        int textMesCount = 0;
        while (bots.size() < botsMaxNum) {
            Thread.sleep(100);
            int i = new Random().nextInt(bots.size());
            TestClient bot = bots.get(i);
            if (bot.getReadyState() == ReadyState.OPEN) {
                bot.setTyping();
                typingMesCount++;
                if (new Random().nextInt(2) == 0) {
                    bot.sendMsg(UUID.randomUUID().toString());
                    textMesCount++;
                }
            }
            addBot();
            if (bots.size() % 10 == 0)
                log.info("Bots (actual/target): {}/{}, messages (text/typing): {}/{}", bots.size(), botsMaxNum, textMesCount, typingMesCount);
        }
        int msgCount = bots.stream().map(TestClient::getIncomingMessagesCount).mapToInt(x -> x).sum();
        log.info("Disconnecting bots...");
        for (TestClient bot : bots) if (bot.getReadyState() == ReadyState.OPEN) bot.closeBlocking();
        log.info("Total messages processed by server: {}", msgCount);
    }
}
