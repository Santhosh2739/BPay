package coreframework.processing.bweb_processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.invoice.Invoice_List_Activity;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.webotp.BWeb_ECom_OTP_List_Activity;
import com.google.gson.Gson;

import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;

/**
 * Created by 10037 on 22-Nov-17.
 */

public class BWebProcessing implements UserInterfaceBackgroundProcessing {

    private GenericRequest request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    CustomerLoginRequestReponse customerLoginRequestReponse = null;
    TextToSpeech tts;

    public BWebProcessing(GenericRequest request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;

    }

    @Override
    public String captureURL() {
        customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        this.request.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        this.request.setG_transType(TransType.ECOM_OTP_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.ECOM_OTP_REQUEST.getURL());

        Log.e("BWeb/EcomOTP","Data: "+new Gson().toJson(this.request));


        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(this.request)));
        String serverURL = buffer.toString();
        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();

            Log.e("BWeb/EcomOTP",""+network_response);

//         BWeb/EcomOTP: Data: {"g_oauth_2_0_client_token":"0b97046d34cf44087bfa4f19c73927f13701dba4b97652e0ed2437886e8e15be","g_security_counter":0,"g_transType":"ECOM_OTP_REQUEST","g_userId":"63636363"}
//         BWeb/EcomOTP: {"otpList":[{"ecomTxnId":7,"createdDate":"Dec 17, 2019 12:46:47 PM","merchantTxnRefNo":"5176575994995158","txnRefNo":"ECOM19001248","otp":"985947","mobileNumber":"63636363"}],"g_status":1,"g_status_description":"SUCCESS","g_response_trans_type":"ECOM_OTP_RESPONSE"}


            if (!network_response.isEmpty()) {
                GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
                if (response.getG_response_trans_type().equalsIgnoreCase(TransType.ECOM_OTP_RESPONSE.name()) && response.getG_status() == 1) {
                    this.response_json = network_response;
                    this.success = true;
                } else if (response.getG_response_trans_type().equalsIgnoreCase(TransType.ECOM_OTP_RESPONSE.name()) && response.getG_status() != 1) {
                    error_text_header = response.getG_errorDescription();
                    error_text_details = response.getG_status_description();
                } else {
                    error_text_header = response.getG_errorDescription();
                    error_text_details = response.getG_status_description();
                }
            } else {
                error_text_header = (String) application.getApplicationContext().getString(R.string.services_are_down);
                error_text_details = (String) application.getApplicationContext().getString(R.string.services_are_down);
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


//            Toast toast = Toast.makeText(activity, this.response_json, Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//            toast.show();
//
//            Toast toast1 = Toast.makeText(activity, this.response_json, Toast.LENGTH_LONG);
//            toast1.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//            toast1.show();


            Intent intent = new Intent(activity, BWeb_ECom_OTP_List_Activity.class);
            intent.putExtra("BWEB_ECOM_OTP_RESPONSE", this.response_json);
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
                    case "You have no invoices":
                        Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.You_have_no_invoices), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Failure general server":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT);
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

