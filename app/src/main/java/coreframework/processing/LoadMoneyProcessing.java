package coreframework.processing;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;
import android.util.Log;
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

import com.bookeey.wallet.live.PayUWebPage;
import com.bookeey.wallet.live.application.CoreApplication;

import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.loadmoney.PaymentForm;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;

/**
 * Created by 30099 on 1/26/2016.
 */
public class LoadMoneyProcessing implements UserInterfaceBackgroundProcessing {
    private CoreApplication application;
    private String userName;
    private String password;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    private PaymentForm paymentForm;
    //    private String loginResponse = null;
    private GenericResponse response;
    private String temp_response;

    private String json_login_request_to_be_removed = CustomSharedPreferences.SIMPLE_NULL;

    public LoadMoneyProcessing(PaymentForm paymentForm, CoreApplication application, boolean isPost) {
        this.paymentForm = paymentForm;
        this.isPost = isPost;
        this.application = application;
    }

    @Override
    public String captureURL() {
        //SET MORE PARAMS HERE STARTS
        //GET APP VERSION
        PackageInfo p_info;
        double appVersionDouble = 0d;
        try {
            p_info = this.application.getPackageManager().getPackageInfo(this.application.getPackageName(), 0);
            appVersionDouble = Double.parseDouble(p_info.versionName);
        } catch (PackageManager.NameNotFoundException e) {

        }
        String deviceVersion = Build.VERSION.RELEASE;
        String deviceID = ((CoreApplication) application).getThisDeviceUniqueAndroidId();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        paymentForm.setG_transType(TransType.LOADMONEY_REQUEST.name());
        paymentForm.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.LOADMONEY_REQUEST.getURL());
//        buffer.append("http://192.168.1.8:8088/kmnoadmin/proceed");
//       buffer.append("http://192.168.1.8:8088/kmnoadmin/proceed");

        Log.e("LoadMoneyRQD",new Gson().toJson(paymentForm));
        buffer.append("?data=" + URLUTF8Encoder.encode(new Gson().toJson(paymentForm)));
        String serverURL = buffer.toString();
        Log.e("LoadMoneyURL",serverURL);
        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            Log.e("LoadMoneyRSP",network_response);
            response = new Gson().fromJson(network_response, GenericResponse.class);

            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.LOADMONEY_RESPONSE.name()) && response.getG_status() == 1) {
                this.success = true;

            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.LOADMONEY_RESPONSE.name()) && response.getG_status() != 1) {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
            } else {
               /* error_text_header = "Failure general server";
                error_text_details = "Failure general server";*/
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
            Intent intent = new Intent(activity, PayUWebPage.class);
            intent.putExtra("temp_response", response.getKnetUrl());
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
                    case "Mobile Not Register":
                        Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Mobile_Not_Register), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Please Enter minimum Amount":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Please_Enter_minimum_Amount), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Maximum Amount exeeded":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Maximum_Amount_exeeded), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Daily txn value exceeded":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Daily_txn_value_exceeded), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Daily txn Limit count exceeded":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Daily_txn_Limit_count_exceeded), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Monthly txn value exceeded":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Monthly_txn_value_exceeded), Toast.LENGTH_SHORT);
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
