package com.apps.talkit.classes;

public class Messages {
    String message;
    int check; //0=my message, 1 = received message

    public Messages(String message, int check) {
        this.message = message;
        this.check = check;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
