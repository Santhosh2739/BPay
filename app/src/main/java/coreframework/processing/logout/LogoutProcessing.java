package coreframework.processing.logout;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.login.LoginActivity;
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;

import com.bookeey.wallet.live.application.CoreApplication;

import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;


/**
 * Created by mohit on 16-06-2015.
 */
public class LogoutProcessing implements UserInterfaceBackgroundProcessing {
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    private GenericResponse response;

    private String json_login_request_to_be_removed = CustomSharedPreferences.SIMPLE_NULL;

    public LogoutProcessing(CoreApplication application, boolean isPost) {
        this.isPost = isPost;
        this.application = application;
    }

    @Override
    public String captureURL() {
        GenericRequest genericRequest = new GenericRequest();
        genericRequest.setG_transType(TransType.LOGOUT_CUSTOMER.name());
        genericRequest.setG_oauth_2_0_client_token(application.getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.LOGOUT_CUSTOMER.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(genericRequest)));
        String serverURL = buffer.toString();
        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.LOGOUT_CUSTOMER_RESPONSE.name()) || response.getG_status() == 0) {
                success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.LOGIN_CUSTOMER_RESPONSE.name()) || response.getG_status() != 0) {
                /*error_text_header = response.getG_response_trans_type();
                error_text_details = response.getG_errorDescription();*/
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
            } else {
                /*error_text_header = "Failure general server";
                error_text_details = "Failure general server";*/
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
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
            Toast.makeText(activity, success ? "Log out successful" : "Log out", Toast.LENGTH_LONG).show();
            application.setIsUserLoggedIn(false);
            application.setCustomerLoginRequestReponse(new CustomerLoginRequestReponse());
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            System.exit(0);
            activity.startActivity(intent);
            activity.finish();

//            activity.startActivity(i);
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
        /*else {
            Toast toast = Toast.makeText(activity, error_text_header, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
            toast.show();
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }*/
    }

    @Override
    public boolean isPost() {
        return isPost;
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