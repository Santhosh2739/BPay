package coreframework.processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;

import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;

import ycash.wallet.json.pojo.changes.UpdateCustMobilenumberRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;

/**
 * Created by 30099 on 1/25/2016.
 */
public class MobileNumberChangeProcessing implements UserInterfaceBackgroundProcessing {
    private UpdateCustMobilenumberRequest request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";

    public MobileNumberChangeProcessing(UpdateCustMobilenumberRequest request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;

    }

    @Override
    public String captureURL() {

        this.request.setG_transType(TransType.UPDATE_MOBILENUMBER_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.UPDATE_MOBILENUMBER_REQUEST.getURL());

        Log.e("MobileNo","Data: "+new Gson().toJson(this.request));

        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(this.request)));


        String mob_change_url = buffer.toString();

        Log.e("MobileNo","URL: "+mob_change_url);

        return mob_change_url;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();

            Log.e("MobileNo","Res: "+network_response);

            GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response.getG_response_trans_type().equalsIgnoreCase(TransType.UPDATE_MOBILENUMBER_RESPONSE.name()) && response.getG_status() == 1) {
                this.response_json = network_response;
                this.success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.UPDATE_MOBILENUMBER_RESPONSE.name()) && response.getG_status() != 1) {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
            } else {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
            }
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
            error_text_header = "Failure general server";
            error_text_details = "Failure general server";
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
            error_text_header = "Failure general server";
            error_text_details = "Failure general server";
        }
    }

    @Override
    public void performUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment) {
        dialogueFragment.dismiss();
        if (success) {
            Intent intent = new Intent(application, LoginActivity.class);
            CustomSharedPreferences.saveStringData(application, request.getNewmobilenumber(), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
            activity.startActivity(intent);
            activity.finish();
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
