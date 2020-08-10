package coreframework.processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.prepaidcard.VirtualPrepaidcardListActivity;
import com.google.gson.Gson;

import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;

/**
 * Created by 30099 on 6/29/2016.
 */
public class VirtualPrepaidCardsListProcessing implements UserInterfaceBackgroundProcessing {

    private String request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    GenericResponse response;
    CustomerLoginRequestReponse customerLoginRequestReponse = null;

    public VirtualPrepaidCardsListProcessing(String request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;

    }

    @Override
    public String captureURL() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.PREPAID_CATEGORYLIST_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(request));
        String send_money_url = buffer.toString();
        return send_money_url;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response.getG_response_trans_type().equalsIgnoreCase(TransType.PREPAID_CATEGORYLIST_RESPONSE.name()) && response.getG_status() == 1) {
                this.response_json = network_response;

                this.success = true;
            } else if (response.getG_response_trans_type().equalsIgnoreCase(TransType.PREPAID_CATEGORYLIST_RESPONSE.name()) && response.getG_status() != 1) {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
            } else {
                error_text_header = "Failure general server";
                error_text_details = "Failure general server";
            }
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
            error_text_header = "Failure general server";
            error_text_details = "Failure general server";
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {

            error_text_header = "Check your internet connection and try again";
            error_text_details = "Check your internet connection and try again";
        }
    }

    @Override
    public void performUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment) {
        if (success) {
            Intent intent = new Intent(application, VirtualPrepaidcardListActivity.class);
            intent.putExtra("VIRTUAL_CARD_RESPONSE", this.response_json);
            activity.startActivity(intent);
            activity.finish();
        } else {

            if (error_text_header.equalsIgnoreCase("Session expired")) {
                Toast toast = Toast.makeText(activity, error_text_header, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
            } else {
                Toast toast = Toast.makeText(activity, error_text_header, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
            }
        }
        dialogueFragment.dismiss();
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
