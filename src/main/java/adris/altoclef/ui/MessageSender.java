package adris.altoclef.ui;

import adris.altoclef.Debug;
import adris.altoclef.util.csharpisbetter.Timer;
import net.minecraft.client.MinecraftClient;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * We can't send messages immediately as the server will kick us.
 * As such, we will send messages in a delayed queued fashion.
 */
public class MessageSender {

    // How many messages can we send quickly before giving a little pause?
    private static final int FAST_LIMIT = 6;
    private static final int SLOW_LIMIT = 3;

    private final PriorityQueue<BaseMessage> _whisperQueue = new PriorityQueue<>(
            Comparator.comparingInt((BaseMessage msg) -> msg.priority.getImportance())
                    .thenComparingInt(msg -> msg.index)
    );
    //private final Queue<Whisper> _whisperQueue = new ArrayDeque<>();

    private final Timer _fastSendTimer = new Timer(0.3f);
    private final Timer _bigSendTimer = new Timer(3.5);
    private final Timer _bigBigSendTimer = new Timer(10);

    private int _messageCounter = 0;

    private int _fastCount;
    private int _slowCount;

    public void tick() {
        if (_bigBigSendTimer.elapsed()) {
            if (_bigSendTimer.elapsed()) {
                if (_fastSendTimer.elapsed()) {
                    if (!_whisperQueue.isEmpty()) {
                        _fastSendTimer.reset();
                        BaseMessage msg = _whisperQueue.poll();
                        assert msg != null;
                        sendChatInstant(msg.getChatInput());
                        _fastCount++;
                        if (_fastCount >= FAST_LIMIT) {
                            _bigSendTimer.reset();
                            _fastCount = 0;
                            _slowCount++;
                            if (_slowCount >= SLOW_LIMIT) {
                                _bigBigSendTimer.reset();
                                _slowCount = 0;
                            }
                        }
                    }
                }
            }
        }
    }
    public void enqueueWhisper(String username, String message, MessagePriority priority) {
        _whisperQueue.add(new Whisper(username, message, priority, _messageCounter++));
    }
    public void enqueueChat(String message, MessagePriority priority) {
        _whisperQueue.add(new ChatMessage(message, priority, _messageCounter++));
    }


    private void sendChatInstant(String message) {
        if (MinecraftClient.getInstance().player == null) {
            Debug.logError("Failed to send chat message as no client loaded.");
            return;
        }
        MinecraftClient.getInstance().player.sendChatMessage(message);
    }

    private static abstract class BaseMessage {
        public MessagePriority priority;
        public int index;

        public BaseMessage(MessagePriority priority, int index) {
            this.priority = priority;
            this.index = index;
        }

        public abstract String getChatInput();
    }

    private static class Whisper extends BaseMessage {
        public String username;
        public String message;

        public Whisper(String username, String message, MessagePriority priority, int index) {
            super(priority, index);
            this.username = username;
            this.message = message;
        }

        @Override
        public String getChatInput() {
            return "/msg " + username + " " + message;
        }
    }
    private static class ChatMessage extends BaseMessage {

        public String message;

        public ChatMessage(String message, MessagePriority priority, int index) {
            super(priority, index);
            this.message = message;

        }
        @Override
        public String getChatInput() {
            return message;
        }
    }
}
