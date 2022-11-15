package coreframework.processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.showpushnotificationmessage.ShowPushNotificationMessageDialogActivity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.getpushnotificationmessage.GetPushNotificationMessageRequest;
import ycash.wallet.json.pojo.getpushnotificationmessage.PushNotificationDetailsPojo;

public class GetPushNotificationMessageProcessing implements UserInterfaceBackgroundProcessing {

    private GetPushNotificationMessageRequest request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";

    public GetPushNotificationMessageProcessing(GetPushNotificationMessageRequest request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;

    }

    @Override
    public String captureURL() {

        this.request.setG_transType(TransType.SHOW_PUSHNOTIFICATION_MESSAGE_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.SHOW_PUSHNOTIFICATION_MESSAGE_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(this.request)));
        String send_money_url = buffer.toString();
        return send_money_url;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.SHOW_PUSHNOTIFICATION_MESSAGE_RESPONSE.name()) && response.getG_status() == 1) {
                this.response_json = network_response;
                this.success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.SHOW_PUSHNOTIFICATION_MESSAGE_RESPONSE.name()) && response.getG_status() != 1) {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
            } else {
//                error_text_header = response.getG_errorDescription();
//                error_text_details = response.getG_status_description();
                error_text_header = "Failure general server";
                error_text_details = "Failure general server";
            }
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
            error_text_header = "Failure general server";
            error_text_details = "Failure general server";
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
            error_text_header = "Failure network error";
            error_text_details = "Failure network error";
        }
    }

    @Override
    public void performUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment) {
        dialogueFragment.dismiss();
        if (success) {




            try {
                PushNotificationDetailsPojo responsePojo = new Gson().fromJson(response_json, PushNotificationDetailsPojo.class);




                JSONObject finalJsonObject = new JSONObject();

                JSONArray pushMessageObjectJsonArray = new JSONArray();

                for (int i = 0; i < responsePojo.getPushList().size(); i++) {



                    JSONObject pushMessageJsonObject = new JSONObject();

                    Charset charset = Charset.forName("ISO-8859-6");
                    CharsetDecoder decoder = charset.newDecoder();
                    ByteBuffer buf = ByteBuffer.wrap(responsePojo.getPushList().get(i).getMessage());
                    CharBuffer cbuf = decoder.decode(buf);
                    CharSequence pushNotificationMessage = java.nio.CharBuffer.wrap(cbuf);

                    pushMessageJsonObject.put("message", pushNotificationMessage);
                    pushMessageJsonObject.put("time", responsePojo.getPushList().get(i).getTime());

                    pushMessageObjectJsonArray.put(pushMessageJsonObject);



                }

                finalJsonObject.put("list", pushMessageObjectJsonArray);

//                    Charset charset = Charset.forName("ISO-8859-6");
//                    CharsetDecoder decoder = charset.newDecoder();
//                    ByteBuffer buf = ByteBuffer.wrap(finalJsonObject.toString().getBytes());
//                    CharBuffer cbuf = decoder.decode(buf);
//                    CharSequence pushNotificationMessage = java.nio.CharBuffer.wrap(cbuf);

//                    Toast toast = Toast.makeText(activity, finalJsonObject.toString(), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                    toast.show();


                Intent intent = new Intent(activity, ShowPushNotificationMessageDialogActivity.class);
                intent.putExtra(ShowPushNotificationMessageDialogActivity.KEY_PUSH_NOTIFICATION_MESSAGES, finalJsonObject.toString());
                activity.startActivity(intent);



            } catch (Exception e) {

                Log.e("Push Notifica Msg Ex:", "" + e.getMessage());

                Toast toast = Toast.makeText(activity, "System Error! " + e.getMessage(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();



            }

        } else {
            Toast toast = Toast.makeText(activity, error_text_header, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
            toast.show();
        }

    }

    @Override
    public boolean isPost() {
        return this.isPost;
    }

    @Override
    public void preProcessResponse(Message msg) {

    }

    @Override
    public void prePerformUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment) {

    }

    @Override
    public boolean isLocalProcess() {
        return false;
    }

    @Override
    public void performTask() {

    }

    @Override
    public void handleSessionInvalid(Activity activity, ProgressDialogFrag dialogueFragment) {

    }
}
