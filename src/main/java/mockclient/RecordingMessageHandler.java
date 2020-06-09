package mockclient;

interface RecordingMessageHandler extends ChatRecorder {
    void handleJsonMessage(String json);
}
