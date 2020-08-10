package coreframework.processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.login.LoginActivity;
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;

import com.bookeey.wallet.live.ViewProfileActivity;
import com.bookeey.wallet.live.application.CoreApplication;

import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;

/**
 * Created by 30099 on 1/21/2016.
 */
public class ViewProfileProcessing implements UserInterfaceBackgroundProcessing {

    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    private GenericResponse response;
    private String response_pojo;

    private String json_login_request_to_be_removed = CustomSharedPreferences.SIMPLE_NULL;

    public ViewProfileProcessing(CoreApplication application, boolean isPost) {
        this.isPost = isPost;
        this.application = application;
    }

    @Override
    public String captureURL() {
        GenericRequest genericRequest = new GenericRequest();
        genericRequest.setG_transType(TransType.VIEW_PROFILE_REQUEST.name());
        genericRequest.setG_oauth_2_0_client_token(application.getCustomerLoginRequestReponse().getOauth_2_0_client_token());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.VIEW_PROFILE_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(genericRequest)));
        String serverURL = buffer.toString();
        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.VIEW_PROFILE_RESPONSE.name()) && response.getG_status() == 1) {
                this.response_pojo = network_response;
                success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.VIEW_PROFILE_RESPONSE.name()) && response.getG_status() != 1) {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
                /*
                error_text_header = response.getG_response_trans_type();
                error_text_details = response.getG_errorDescription();*/
            } else {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
              /*  error_text_header = "Failure general server";
                error_text_details = "Failure general server";*/
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
            dialogueFragment.dismiss();
            Intent intent = new Intent(activity, ViewProfileActivity.class);
            intent.putExtra("view_profile", response_pojo);
            activity.startActivity(intent);

        } else {
            if (error_text_header.equalsIgnoreCase("Session expired")) {
                Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
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
                    default:
                        toast = Toast.makeText(activity, error_text_header, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                }
            }
        }
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
