package ru.ilka.entity;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class Message {
    private int messageId;
    private int senderId;
    private int receiverId;
    private String text;
    private boolean readMark;
    private String sendTime;

    public Message(){

    }

    public Message(int messageId, int senderId, int receiverId, String text, boolean readMark, String sendTime) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.text = text;
        this.readMark = readMark;
        this.sendTime = sendTime;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isReadMark() {
        return readMark;
    }

    public void setReadMark(boolean readMark) {
        this.readMark = readMark;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (messageId != message.messageId) return false;
        if (senderId != message.senderId) return false;
        if (receiverId != message.receiverId) return false;
        if (readMark != message.readMark) return false;
        if (!text.equals(message.text)) return false;
        return sendTime.equals(message.sendTime);
    }

    @Override
    public int hashCode() {
        int result = messageId;
        result = 31 * result + senderId;
        result = 31 * result + receiverId;
        result = 31 * result + text.hashCode();
        result = 31 * result + (readMark ? 1 : 0);
        result = 31 * result + sendTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", text='" + text + '\'' +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }
}
