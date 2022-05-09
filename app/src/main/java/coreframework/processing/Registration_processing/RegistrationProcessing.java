package coreframework.processing.Registration_processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.login.LoginActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;

import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.registration.RegistrationSuccess;

import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.registration.CustomerRegistrationRequest;
import ycash.wallet.json.pojo.registration.RegisterNUpdateResponse;

/**
 * Created by 30099 on 10/29/2015.
 */
public class RegistrationProcessing implements UserInterfaceBackgroundProcessing {

    private CustomerRegistrationRequest request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private GenericResponse response;
    private String error_text_details = "";
    private RegisterNUpdateResponse serverRegistrationResponse = null;

    public RegistrationProcessing(CustomerRegistrationRequest request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;
    }

    @Override
    public String captureURL() {

        this.request.setG_transType(TransType.REGISTER.name());

//        this.request.setWalletUser("MuneeOIPUYTRF");

       // this.request.setWalletUser("BySAQWERTYUI");




        Log.e("Register request data: ", "" + new Gson().toJson(this.request));
        Log.e("Register request data: ", this.request.toString());

        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.REGISTER.getURL());

        String json_data = new Gson().toJson(request);

        Log.e("Register request JSON: ", "" + new Gson().toJson(request));

        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(request)));
        String serverURL = buffer.toString();

        Log.e("Register request URL: ", "" + serverURL);

        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();

            Log.e("Register response: ", "" + network_response);

            response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.REGISTER_RESPONSE.name()) && response.getG_status() == 0) {
                CustomSharedPreferences.saveIntData(application, CustomSharedPreferences.APP_STATUS_REQ_SENT, CustomSharedPreferences.SP_KEY.APP_STATUS);
                this.response_json = network_response;
                serverRegistrationResponse = new Gson().fromJson(this.response_json, RegisterNUpdateResponse.class);
                this.success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.REGISTER_RESPONSE.name()) && response.getG_status() != 0) {
                error_text_header = response.getG_status_description();
                error_text_details = response.getG_errorDescription();
            } else {
                if(response!=null) {
                    error_text_header = response.getG_errorDescription();
                    error_text_details = response.getG_status_description();
                }
                error_text_header ="Error!";
                error_text_details = "System Error!";

            }
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
            error_text_header = (String) application.getApplicationContext().getString(R.string.failure_general_server_error);
            error_text_details = (String) application.getApplicationContext().getString(R.string.failure_general_server_error);
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
            error_text_header = (String) application.getApplicationContext().getString(R.string.failure_network_error);
            error_text_details = (String) application.getApplicationContext().getString(R.string.failure_network_error);
        }
    }

    @Override
    public void performUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment) {
        if (success) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();
            dialogueFragment.dismiss();
        } else {
            switch (error_text_header) {
                case "Failure general server":
                    Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    break;
                case "Failure network error":
                    toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    break;
                case "Session expired":
                    toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.session_expired), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    break;
                default:
                    toast = Toast.makeText(activity, error_text_header, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    break;
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
