package coreframework.processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

//import com.bookeey.wallet.live.Merchant_Map_Activity;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.google.gson.Gson;

import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.wheretopay.MerchantListResponse;

/**
 * Created by 30099 on 3/22/2016.
 */
public class MerchantListProcessing implements UserInterfaceBackgroundProcessing {


    private GenericRequest request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    GenericResponse response;
    CustomerLoginRequestReponse customerLoginRequestReponse = null;
    MerchantListResponse merchantlistresponse = null;

    public MerchantListProcessing(CoreApplication application, boolean isPost) {

        this.application = application;
        this.isPost = isPost;

    }

    @Override
    public String captureURL() {
        request = new GenericRequest();
        customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        request.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        request.setG_transType(TransType.WHERE_TO_PAY_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.WHERE_TO_PAY_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(request)));
        String merchant_url = buffer.toString();
        return merchant_url;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response.getG_response_trans_type().equalsIgnoreCase(TransType.WHERE_TO_PAY_RESPONSE.name()) && response.getG_status() == 1) {
                this.response_json = network_response;
                merchantlistresponse = new Gson().fromJson(network_response, MerchantListResponse.class);
                this.success = true;
            } else if (response.getG_response_trans_type().equalsIgnoreCase(TransType.WHERE_TO_PAY_RESPONSE.name()) && response.getG_status() != 1) {
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
            /*((Merchant_Map_Activity)activity).updateAddress(merchantlistresponse);
            dialogueFragment.dismiss();*/
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
