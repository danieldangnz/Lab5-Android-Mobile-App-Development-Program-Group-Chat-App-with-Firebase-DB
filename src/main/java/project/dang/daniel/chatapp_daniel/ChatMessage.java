package project.dang.daniel.chatapp_daniel;

import java.util.Date;

public class ChatMessage {
    //////////////////////////////////////////////////////////////////////////////////////
    //Declare class properties/data
    private String messageText;
    private String messageUser;
    private long messageTime;

    //////////////////////////////////////////////////////////////////////////////////////
    //Constructor 1
    public ChatMessage(String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //Constructor 2
    public ChatMessage() {

    }

    //////////////////////////////////////////////////////////////////////////////////////
    //Getter and setters
    public String getMessageText() {
        return messageText;
    }

    //
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    //
    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    //
    public long getMessageTime() {
        return messageTime;
    }

    //
    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
