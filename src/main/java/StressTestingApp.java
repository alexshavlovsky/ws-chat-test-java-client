import org.java_websocket.enums.ReadyState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
        System.out.println("Initial bots: " + botsInitialNum);
        System.out.println("Instantiating bots...");
        while (bots.size() < botsInitialNum) addBot();
        System.out.println("Target bots: " + botsMaxNum);
        while (bots.size() < botsMaxNum) {
            Thread.sleep(100);
            int i = new Random().nextInt(bots.size());
            TestClient bot = bots.get(i);
            if (bot.getReadyState() == ReadyState.OPEN) {
                bot.setTyping();
                if (new Random().nextInt(2) == 0) bot.sendMsg(UUID.randomUUID().toString());
            }
            addBot();
            if (bots.size() % 10 == 0) System.out.println("Total bots: " + bots.size() + "/" + botsMaxNum);
        }
        System.out.println("Disconnecting bots...");
        for (TestClient bot : bots) if (bot.getReadyState() == ReadyState.OPEN) bot.closeBlocking();
    }
}
