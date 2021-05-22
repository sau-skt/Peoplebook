package appdev.com.peoplebook.Models;

public class MessageModel {
    String message, messagekey, profileimage, sender;

    public MessageModel() {
    }

    public MessageModel(String message, String messagekey, String profileimage, String sender) {
        this.message = message;
        this.messagekey = messagekey;
        this.profileimage = profileimage;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessagekey() {
        return messagekey;
    }

    public void setMessagekey(String messagekey) {
        this.messagekey = messagekey;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
