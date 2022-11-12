package coreframework.processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
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
import ycash.wallet.json.pojo.generic.BioMetricRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;

/**
 * Created by 10030 on 12/6/2016.
 */
public class BiometricProcessing implements UserInterfaceBackgroundProcessing {

    private BioMetricRequest request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    private String _temp_response;

    public BiometricProcessing(BioMetricRequest request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;
    }

    @Override
    public String captureURL() {
        this.request.setG_transType(TransType.BIO_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.BIO_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(request)));
        String serverURL = buffer.toString();
        Log.e("BIO_REQUEST", serverURL);
        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            Log.e("BIO_RESPONSE", "->" + network_response);
            GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.BIO_RESPONSE.name()) && response.getG_status() == 1) {
                _temp_response = network_response;
                Log.e("BIO_RESPONSE", "->" + request.isBiometric());
                CustomSharedPreferences.saveBooleanData(application, request.isBiometric() ,CustomSharedPreferences.SP_KEY.BIOMETRIC_ENABLED);
                this.success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.BIO_RESPONSE.name()) && response.getG_status() != 1) {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
            } else {
                assert response != null;
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
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
            String msg = request.isBiometric() ? "Biometric enabled successfully" : "Biometric disabled successfully";
            Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
            toast.show();
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
