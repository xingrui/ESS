package src.share;

/**
 * The message type of the messages in the queue. The one of the most important
 * interfaces in the elevator simulation system.
 */
public interface Message {
    int getNumber();

    Type getType();
}