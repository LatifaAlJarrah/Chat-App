package com.example.chitchat.models;

public class Call {
    private byte[] image;
    private String name;

    public Call() {
    }

    public Call(byte[] image, String name) {
        this.image = image;
        this.name = name;
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
}
