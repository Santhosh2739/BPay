package ycash.wallet.json.pojo.getpushnotificationmessage;

import java.io.Serializable;

import ycash.wallet.json.pojo.generic.GenericRequest;

public class GetPushNotificationMessageRequest extends GenericRequest implements Serializable {


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    private String deviceId;

}
