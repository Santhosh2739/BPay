package coreframework.processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.registration.CustomerActivationRequest;

/**
 * Created by 10030 on 12/6/2016.
 */
public class ChangePinProcessing implements UserInterfaceBackgroundProcessing {

    private CustomerActivationRequest request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    private String _temp_response;

    public ChangePinProcessing(CustomerActivationRequest request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;

    }

    @Override
    public String captureURL() {
        this.request.setG_transType(TransType.CHANGEPIN_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.CHANGEPIN_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(request)));
        String serverURL = buffer.toString();
        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.CHANGEPIN_RESPONSE.name()) && response.getG_status() == 0) {
                CustomSharedPreferences.saveIntData(application, CustomSharedPreferences.APP_STATUS_ACTIVATED, CustomSharedPreferences.SP_KEY.APP_STATUS);
                _temp_response = network_response;
                CustomSharedPreferences.saveStringData(this.application.getBaseContext(), request.getNewPin(), CustomSharedPreferences.SP_KEY.PIN);
                this.success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.CHANGEPIN_RESPONSE.name()) && response.getG_status() != 0) {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
            } else {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
               /* error_text_header = "Failure general server";
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
        dialogueFragment.dismiss();
        if (success) {
            Toast toast = Toast.makeText(activity, "Your password has been changed successfully", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
            toast.show();

            activity.finish();
        } else {
            if (error_text_header.equalsIgnoreCase("Session expired")) {
                Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
            } else if (error_text_header.equalsIgnoreCase("Received Password entered wrong")) {
                Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Received_Password_entered_wrong), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
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
       /* else {
            Toast toast= Toast.makeText(activity, error_text_header, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
            toast.show();
        }*/
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
