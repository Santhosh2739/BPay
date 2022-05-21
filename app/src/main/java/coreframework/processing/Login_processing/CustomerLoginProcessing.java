package coreframework.processing.Login_processing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.changes.ErrorDialog_DeviceIdChange;
import com.bookeey.wallet.live.changes.ErrorDialog_MobileNumberChange;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.registration.OoredooActivation;
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.processing.BiometricProcessing;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;

import com.bookeey.wallet.live.application.CoreApplication;

import org.json.JSONObject;

import java.math.BigDecimal;

import ycash.wallet.json.pojo.generic.BioMetricRequest;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequest;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;

/**
 * Created by mohit on 16-06-2015.
 */
public class CustomerLoginProcessing implements UserInterfaceBackgroundProcessing {
    private CoreApplication application;
    private String userName;
    private String password;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    private CustomerLoginRequest loginRequest;
    private GenericResponse response;
    private String json_login_request_to_be_removed = CustomSharedPreferences.SIMPLE_NULL;

    public CustomerLoginProcessing(CustomerLoginRequest loginRequest, boolean isPost, CoreApplication application) {
        this.loginRequest = loginRequest;
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


        loginRequest.setDeviceIdNumber(deviceID);
        loginRequest.setIpAddress("");
        loginRequest.setDeviceType(5);//For Android Only|For Windows & IOS It will be different
        loginRequest.setLat(Double.toString(0.00d));//TODO:
        loginRequest.setLon(Double.toString(0.00d));//TODO:
        String gcmid = CustomSharedPreferences.getGCMRegId(application, CustomSharedPreferences.SP_KEY.GCM_REG_ID);
        CustomSharedPreferences.saveStringData(application, loginRequest.getLogin_pin(), CustomSharedPreferences.SP_KEY.TEST_PIN);
        loginRequest.setGcmRegistrationId(gcmid);
        loginRequest.setWalletApplicationVersion(Double.toString(appVersionDouble));
        loginRequest.setDeviceOsVersionDetails1(deviceVersion);
        loginRequest.setDeviceOsVersionDetails2("Android");
        loginRequest.setG_transType(TransType.LOGIN_CUSTOMER.name());

        Log.e("Login request data: ", "" + new Gson().toJson(this.loginRequest));

        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.LOGIN_CUSTOMER.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(loginRequest)));
        String serverURL = buffer.toString();

        Log.e("Login request URL: ", "" + serverURL);

        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();

            Log.e("Login Response: ", "" + network_response);

            response = new Gson().fromJson(network_response, GenericResponse.class);


//            {"lastSuccessLoginTime":1559206301297,"enc_key":"b56b3109a78adac31354c1a2d97a9e2d","mac_key":"eeff71b3831007c449c819b06a1a9162","mobileNumber":"98037941",
//                    "custFirstName":"Rahman","walletBalance":138.25,"oauth_2_0_client_token":"6ce3bd79921643216ab66bf7fb2186d3a7bb25ccb06027bbdcbbee3eb8c9aac2",
//                    "uniqueCustomerId":"001363","custLastName":"AR","transferLimits":[{"tranType":"RECHARGE","subTranType":"LOCAL_RECHARGE","minValuePerTransaction":0.001,
//                    "maxValuePerTransaction":500.0,"tpinLimit":0.0,"g_status":0},{"tranType":"RECHARGE","subTranType":"INTL_RECHARGE","minValuePerTransaction":0.001,
//                    "maxValuePerTransaction":500.0,"tpinLimit":0.0,"g_status":0},{"tranType":"ECOMMERCE","subTranType":"ECOMMERCE","minValuePerTransaction":0.1,
//                    "maxValuePerTransaction":1000.0,"tpinLimit":0.0,"g_status":0},{"tranType":"PREPAIDCARDS","subTranType":"PREPAIDCARDS","minValuePerTransaction":0.001,
//                    "maxValuePerTransaction":70.0,"tpinLimit":0.0,"g_status":0},{"tranType":"LOADMONEY","subTranType":"CASHRECHARGE","minValuePerTransaction":1.0,
//                    "maxValuePerTransaction":250.0,"tpinLimit":0.0,"g_status":0},{"tranType":"LOADMONEY","subTranType":"PGRECHARGE","minValuePerTransaction":5.0,
//                    "maxValuePerTransaction":500.0,"tpinLimit":0.0,"g_status":0},{"tranType":"P2P","subTranType":"RECEIVER","minValuePerTransaction":2.0,
//                    "maxValuePerTransaction":500.0,"tpinLimit":0.0,"g_status":0},{"tranType":"P2P","subTranType":"SENDER","minValuePerTransaction":2.0,
//                    "maxValuePerTransaction":500.0,"tpinLimit":0.0,"g_status":0},{"tranType":"INVOICE","subTranType":"INVOICE","minValuePerTransaction":0.1,
//                    "maxValuePerTransaction":10000.0,"tpinLimit":0.0,"g_status":0},{"tranType":"P2M","subTranType":"P2M","minValuePerTransaction":0.1,
//                    "maxValuePerTransaction":500.0,"tpinLimit":0.0,"g_status":0},{"tranType":"RECHARGE","subTranType":"LOCAL_RECHARGE","minValuePerTransaction":0.0,
//                    "maxValuePerTransaction":0.0,"tpinLimit":15.0,"g_status":0},{"tranType":"RECHARGE","subTranType":"INTL_RECHARGE","minValuePerTransaction":0.0,
//                    "maxValuePerTransaction":0.0,"tpinLimit":15.0,"g_status":0},{"tranType":"LOAD_MONEY","subTranType":"PGRECHARGE","minValuePerTransaction":0.0,
//                    "maxValuePerTransaction":0.0,"tpinLimit":15.0,"g_status":0},{"tranType":"P2P","subTranType":"SENDER","minValuePerTransaction":0.0,"maxValuePerTransaction":0.0,
//                    "tpinLimit":10.0,"g_status":0},{"tranType":"P2M","subTranType":"P2M","minValuePerTransaction":0.0,"maxValuePerTransaction":0.0,"tpinLimit":15.0,"g_status":0},
//                {"tranType":"PREPAIDCARDS","subTranType":"PREPAIDCARDS","minValuePerTransaction":0.0,"maxValuePerTransaction":0.0,"tpinLimit":15.0,"g_status":0},
//                {"tranType":"ECOMMERCE","subTranType":"ECOMMERCE","minValuePerTransaction":0.0,"maxValuePerTransaction":0.0,"tpinLimit":15.0,"g_status":0}],
//                "domesticRechargeVersion":"1","speakstatus":false,"bannerDetails":["test"],"bpoints":13.500,"g_status":0,
//                    "g_status_description":"Success","g_errorDescription":"","g_response_trans_type":"LOGIN_CUSTOMER_RESPONSE","g_servertime":"GMT+0300","dueInvoiceCount":0}

            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.LOGIN_CUSTOMER_RESPONSE.name()) && (response.getG_status() == 0 || response.getG_status() == 211)) {
                this.success = true;
                response.setSpeakstatus(response.isSpeakstatus());
                CustomSharedPreferences.saveIntData(this.application.getBaseContext(), CustomSharedPreferences.APP_STATUS_LOGGEDIN, CustomSharedPreferences.SP_KEY.APP_STATUS);
                CustomSharedPreferences.saveStringData(this.application.getBaseContext(), loginRequest.getLogin_pin(), CustomSharedPreferences.SP_KEY.PIN);

                try {

                    //FOR BPOINTS
                    JSONObject loginResponseJo = new JSONObject(network_response);
                    Double bpoints = loginResponseJo.getDouble("bpoints");

                    Log.e("Bpoits : ",""+String.valueOf(bpoints));


                    application.setBpoints(new BigDecimal(String.valueOf(bpoints)));

                    CustomSharedPreferences.saveStringData(this.application.getBaseContext(), String.valueOf(bpoints), CustomSharedPreferences.SP_KEY.BPOINTS);

                }catch (Exception e){

                    Log.e("Bpoits Exception: ",""+e.getMessage());

                }


                application.setIsUserLoggedIn(true);
                application.setCustomerLoginRequestReponse(new Gson().fromJson(network_response, CustomerLoginRequestReponse.class));
               /* CustomerLoginRequestReponse customerLoginRequestReponse = new Gson().fromJson(network_response, CustomerLoginRequestReponse.class);
                long l = customerLoginRequestReponse.getLastSuccessLoginTime();*/
                application.getCustomerLoginRequestReponse().processFilteringOfLimitsInHashMap();
                application.setInvoices_count(application.getCustomerLoginRequestReponse().getDueInvoiceCount());
                application.setBannerDetails(application.getCustomerLoginRequestReponse().getBannerDetails());
                application.setSpeakstatus(application.isSpeakstatus());
                CustomSharedPreferences.saveStringData(application, application.getCustomerLoginRequestReponse().getCustFirstName(), CustomSharedPreferences.SP_KEY.NAME);

            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.LOGIN_CUSTOMER_RESPONSE.name()) && response.getG_status() == 211) {
                this.success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.LOGIN_CUSTOMER_RESPONSE.name()) && response.getG_status() == 209) {
                this.success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.LOGIN_CUSTOMER_RESPONSE.name()) && (response.getG_status() != 0 && response.getG_status() != 211)) {
                error_text_header = response.getG_status_description();
                error_text_details = response.getG_status_description();
            } else if (response == null) {
                error_text_header = (String) application.getApplicationContext().getString(R.string.failure_general_server_error);
                error_text_details = (String) application.getApplicationContext().getString(R.string.failure_general_server_error);
            } else {
                error_text_header = response.getG_status_description();
                error_text_details = response.getG_status_description();
                /*error_text_header = "Failure general server";
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
            if (response.getG_status() == 0 || response.getG_status() == 211) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("voice", response.isSpeakstatus());
//                intent.putExtra(MainActivity.KEY_FROM_LOGIN, true);
                CustomSharedPreferences.saveBooleanData(activity, true, CustomSharedPreferences.SP_KEY.COMING_FROM_LOGIN);
                CustomSharedPreferences.saveBooleanData(activity, true, CustomSharedPreferences.SP_KEY.SESSION_EXPIRED);
                intent.putExtra("invoice_count", application.getCustomerLoginRequestReponse().getDueInvoiceCount());
                activity.startActivity(intent);
                activity.finish();
            } /*else if (response.getG_status() == 211) {
                Intent intent = new Intent(activity, OoredooActivation.class);
                activity.startActivity(intent);
                activity.finish();
            }*/else if (response.getG_status() == 209) {
                Intent intent = new Intent(activity, ErrorDialog_DeviceIdChange.class);
                activity.startActivity(intent);
                activity.finish();
            }else {
                Toast toast = Toast.makeText(activity, response.getG_status_description(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
            }
        } else {
            switch (error_text_header) {
                case "Please call to customer care to unlock wallet account":
                    Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Please_call_to_customer_care_to_unlock), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    break;
                case "Invalid Password and you have 5 attempts remaining to lock wallet account":
                    toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Invalid_Password_and_you_have_5_attempts), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    break;
                case "Invalid Password and you have 4 attempts remaining to lock wallet account":
                    toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Invalid_Password_and_you_have_4_attempts), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    break;
                case "Invalid Password and you have 3 attempts remaining to lock wallet account":
                    toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Invalid_Password_and_you_have_3_attempts), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    break;
                case "Invalid Password and you have 2 attempts remaining to lock wallet account":
                    toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Invalid_Password_and_you_have_2_attempts), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                    break;
                case "Invalid Password and you have 1 attempts remaining to lock wallet account":
                    toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Invalid_Password_and_you_have_1_attempts), Toast.LENGTH_SHORT);
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