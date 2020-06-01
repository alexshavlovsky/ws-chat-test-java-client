import lombok.Data;

@Data
class ClientMessageModel {
    int frameId;
    String clientId;
    String nick;
    String type;
    String payload;
}
