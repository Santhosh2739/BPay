package coreframework.processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.ForgotPassword_Success;
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.forgotPassword.ForgotPinRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;

public class ForgotPasswordDeviceChangeProcessing implements UserInterfaceBackgroundProcessing {

    private ForgotPinRequest request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";

    public ForgotPasswordDeviceChangeProcessing(ForgotPinRequest request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;

    }

    @Override
    public String captureURL() {

        this.request.setG_transType(TransType.FORGOT_DEVICECHANGEPASSWORD_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.FORGOT_DEVICECHANGEPASSWORD_REQUEST.getURL());


        Log.e("FrgPass Device change","Data"+new Gson().toJson(this.request));

        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(this.request)));
        String send_money_url = buffer.toString();

        Log.e("FrgPass Device Change: ",""+send_money_url);

        return send_money_url;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();

            Log.e("FrgPass Device Res: ",""+network_response);

            GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);

            if(response!=null) {

                if (response.getG_response_trans_type().equalsIgnoreCase(TransType.FORGOT_DEVICECHANGEPASSWORD_RESPONSE.name()) && response.getG_status() == 1) {
                    this.response_json = network_response;
                    this.success = true;
                } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.FORGOT_DEVICECHANGEPASSWORD_RESPONSE.name()) && response.getG_status() != 1) {
                /*error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();*/
                    error_text_header = response.getG_errorDescription();
                    error_text_details = response.getG_status_description();
                } else {

                    error_text_header = response.getG_errorDescription();
                    error_text_details = response.getG_status_description();
                }

            }else{

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
        if (activity instanceof GenericActivity) {
            if (success) {
                Intent intent = new Intent(application, ForgotPassword_Success.class);
                CustomSharedPreferences.saveIntData(application, CustomSharedPreferences.APP_STATUS_REQ_SENT, CustomSharedPreferences.SP_KEY.APP_STATUS);
                activity.startActivity(intent);
                activity.finish();
            } else {
                Toast toast = Toast.makeText(activity, error_text_header, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
            }
            dialogueFragment.dismiss();
        } else {
            if (success) {
                Intent intent = new Intent(application, ForgotPassword_Success.class);
                CustomSharedPreferences.saveIntData(application, CustomSharedPreferences.APP_STATUS_REQ_SENT, CustomSharedPreferences.SP_KEY.APP_STATUS);
                activity.startActivity(intent);
                activity.finish();
            } else {
                Toast toast = Toast.makeText(activity, error_text_header, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
            }
            dialogueFragment.dismiss();
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
