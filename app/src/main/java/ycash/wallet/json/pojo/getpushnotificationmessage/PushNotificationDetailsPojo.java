package ycash.wallet.json.pojo.getpushnotificationmessage;

import java.util.List;

import ycash.wallet.json.pojo.generic.GenericRequest;

public class PushNotificationDetailsPojo extends GenericRequest {

    public List<PushNotificationMessageResponse> getPushList() {
        return pushList;
    }

    public void setPushList(List<PushNotificationMessageResponse> pushList) {
        this.pushList = pushList;
    }

    private List<PushNotificationMessageResponse> pushList;



}
