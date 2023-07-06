package com.example.chitchat.models;

public class Chat {
    private String receiverUserID;
    private byte[] image;
    private String name;
    private String message;

    public Chat() {
    }


    public Chat(String receiverUserID, byte[] image, String name, String message) {
        this.receiverUserID = receiverUserID;
        this.image = image;
        this.name = name;
        this.message = message;
    }

    public String getReceiverUserID() {
        return receiverUserID;
    }

    public void setReceiverUserID(String receiverUserID) {
        this.receiverUserID = receiverUserID;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
