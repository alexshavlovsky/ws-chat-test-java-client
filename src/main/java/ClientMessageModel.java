import lombok.Data;

@Data
class ClientMessageModel {
    int frameId;
    String clientId;
    String userNick;
    String type;
    String payload;
}
