package ycash.wallet.json.pojo.getpushnotificationmessage;

public class PushNotificationMessageResponse {

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    private byte[] message;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
}
