package newflow_processing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.changes.ErrorDialog_DeviceIdChange;
import com.bookeey.wallet.live.registration.OoredooActivation;
import com.bookeey.wallet.live.registration.Ooredoo_RejectedActivity;
import com.bookeey.wallet.live.registration.RegistrationSuccess;
import com.bookeey.wallet.live.registration.RegistrationSuccessNewFlow;
import com.google.gson.Gson;

import org.json.JSONObject;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;
import newflow.LoginActivityFromSplashNewFlow;
import newflow.LoginActivityNewFlow;
import newflow.MainActivityNewFlow;
import newflow.OoredooRegistrationNewFlow;
import newflow.OoredooValidateFromGuestMainMenu;
import newflow.Ooredoo_RejectedActivityNewFlow;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.registration.CustomerMobileNumberRequest;

//import com.bookeey.wallet.live.login.LoginActivityNewFlow;

public class DeviceIDSplashCheckProcessingNewFlow implements UserInterfaceBackgroundProcessing {
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    private CustomerMobileNumberRequest mobileNumberRequest;
    private GenericResponse response;
    private String networkResponse;
    private boolean isFromMainNewFlow;
    private boolean guestSignUp;
    private boolean guestLogin;

    public DeviceIDSplashCheckProcessingNewFlow(CustomerMobileNumberRequest loginRequest, boolean isPost, CoreApplication application, boolean _isFromMainNewFlow, boolean _guestSignUp, boolean _guestLogin) {
        this.mobileNumberRequest = loginRequest;
        this.isPost = isPost;
        this.application = application;
        this.isFromMainNewFlow = _isFromMainNewFlow;
        this.guestSignUp = _guestSignUp;
        this.guestLogin = _guestLogin;
    }


    @Override
    public String captureURL() {

        String deviceID = ((CoreApplication) application).getThisDeviceUniqueAndroidId();
        mobileNumberRequest.setDeviceId(deviceID);
        mobileNumberRequest.setG_transType(TransType.DEVICEID_LOGIN_CHECK_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.DEVICEID_LOGIN_CHECK_REQUEST.getURL());

        Log.e("DEVICEID", new Gson().toJson(mobileNumberRequest));
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(mobileNumberRequest)));
        String serverURL = buffer.toString();
        Log.e("DEVICEID", serverURL);
        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {

        Log.e("Splash", "Test");

        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();

            Log.e("DEVICEID", network_response);

            response = new Gson().fromJson(network_response, GenericResponse.class);

            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.DEVICEID_LOGIN_CHECK_RESPONSE.name()) && response.getG_status() == 1) {
                this.success = true;
                networkResponse = network_response;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.DEVICEID_LOGIN_CHECK_RESPONSE.name()) && response.getG_status() == -1) {
                this.success = true;

            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.DEVICEID_LOGIN_CHECK_RESPONSE.name()) && response.getG_status() == 211) {
                this.success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.DEVICEID_LOGIN_CHECK_RESPONSE.name()) && response.getG_status() == 209) {
                this.success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.DEVICEID_LOGIN_CHECK_RESPONSE.name()) && response.getG_status() == 299) {
                this.success = true;
                networkResponse = network_response;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.DEVICEID_LOGIN_CHECK_RESPONSE.name()) && response.getG_status() == 0) {
                this.success = true;
                networkResponse = network_response;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.DEVICEID_LOGIN_CHECK_RESPONSE.name()) && (response.getG_status() != 0 && response.getG_status() != 211)) {
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

    private Activity _activity;

    @Override
    public void performUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment) {
        _activity = activity;

        dialogueFragment.dismiss();




        if (success) {

            CustomSharedPreferences.saveStringData(activity, response.getModule(), CustomSharedPreferences.SP_KEY.MODULE);
            if (response.getG_status() == 1) {

                try {

                    CustomSharedPreferences.saveStringData(activity, ""+response.getNotificationCount(), CustomSharedPreferences.SP_KEY.NOTIFICATION_MSG_COUNT);

//                    {"lastSuccessLoginTime":0,"enc_key":"","mac_key":"","mobileNumber":"60064534","custFirstName":"Basha",
//                            "walletBalance":0.0,"oauth_2_0_client_token":"","uniqueCustomerId":"","custLastName":"A","g_status":1,
//                            "g_status_description":"Success","g_errorDescription":"",
//                            "g_response_trans_type":"DEVICEID_LOGIN_CHECK_RESPONSE","g_servertime":"GMT+0300"}

                    JSONObject responseJo = new JSONObject(networkResponse);


                    CustomSharedPreferences.saveStringData(activity, responseJo.getString("mobileNumber"), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
                    CustomSharedPreferences.saveStringData(activity, responseJo.getString("custFirstName"), CustomSharedPreferences.SP_KEY.NAME);

                    CustomSharedPreferences.saveIntData(application, CustomSharedPreferences.APP_STATUS_ACTIVATED, CustomSharedPreferences.SP_KEY.APP_STATUS);


                    Intent intent = new Intent(activity, LoginActivityFromSplashNewFlow.class);

                    //Test
//                    Intent intent = new Intent(activity, MainActivityNewFlow.class);


                    activity.startActivity(intent);
                    activity.finish();

                } catch (Exception e) {

                    Toast toast = Toast.makeText(activity, "System Error!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();

                }

            } else if (response.getG_status() == -1) {

//                Toast toast = Toast.makeText(activity, "Please register to login", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                toast.show();


                if (guestSignUp && isFromMainNewFlow) {
                    Intent in = new Intent(activity, OoredooRegistrationNewFlow.class);
                    activity.startActivity(in);
                } else if (guestLogin & isFromMainNewFlow) {

//                    Intent in = new Intent(activity, LoginActivityNewFlow.class);
//                    activity.startActivity(in);

//                    Toast toast = Toast.makeText(activity, "Please SignUp to Login", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                    toast.show();


                    //Jan 07 Original START
                    Intent in = new Intent(activity, OoredooValidateFromGuestMainMenu.class);
                    activity.startActivity(in);

//                    Jan 19
//                    activity.finish();


                    //Jan 07 Original END


//                   showRegistrationAlertDialogue(_activity);

                } else {
                   //TEST Remove it
//                    Toast toast = Toast.makeText(activity, response.getG_errorDescription(), Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                    toast.show();
                    //TEST Remove it

                    Intent intent = new Intent(activity, MainActivityNewFlow.class);
                    activity.startActivity(intent);
                    activity.finish();
                }


            } else if (response.getG_status() == 211) {
                Intent intent = new Intent(activity, OoredooActivation.class);
                activity.startActivity(intent);
                activity.finish();

            } else if (response.getG_status() == 209) {
                Intent intent = new Intent(activity, ErrorDialog_DeviceIdChange.class);
                activity.startActivity(intent);

//                Jan 19
//                activity.finish();

            } else if (response.getG_status() == 299) {



//                {"lastSuccessLoginTime":0,"enc_key":"","mac_key":"","mobileNumber":"36325555","custFirstName":"",
//                        "walletBalance":0.0,"oauth_2_0_client_token":"","uniqueCustomerId":"","custLastName":"",
//                        "g_status":299,"g_status_description":"Dear Customer, Your Bookeey wallet is rejected." +
//                        " Please modify your info by providing correct data. for any inquiries Please send email " +
//                        "to customercare@bookeey.com","g_errorDescription":"","g_response_trans_type":"DEVICEID_LOGIN_CHECK_RESPONSE",
//                        "g_servertime":"GMT+0300"}


                try {
                    JSONObject responseJo = new JSONObject(networkResponse);


                    CustomSharedPreferences.saveStringData(activity, responseJo.getString("mobileNumber"), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
                    CustomSharedPreferences.saveStringData(activity, responseJo.getString("custFirstName"), CustomSharedPreferences.SP_KEY.NAME);

                    CustomSharedPreferences.saveIntData(application, CustomSharedPreferences.APP_STATUS_ACTIVATED, CustomSharedPreferences.SP_KEY.APP_STATUS);



//                if (!isFromMainNewFlow) {

//                    Intent intent = new Intent(activity, MainActivityNewFlow.class);
//                    activity.startActivity(intent);
//                }




                if(!isFromMainNewFlow) {
                    Intent intent = new Intent(activity, MainActivityNewFlow.class);
                    activity.startActivity(intent);
                    activity.finish();
                }

                Intent intentRejected = new Intent(activity, Ooredoo_RejectedActivityNewFlow.class);

                if(responseJo.has("reason")) {
                    intentRejected.putExtra(Ooredoo_RejectedActivityNewFlow.KEY_REJECTED_REASON, responseJo.getString("reason"));
                }

                //TEST
//                    intentRejected.putExtra(Ooredoo_RejectedActivityNewFlow.KEY_REJECTED_REASON,"ID is not clear");

                activity.startActivity(intentRejected);

                } catch (Exception e) {

                    Toast.makeText(activity, "System Error!", Toast.LENGTH_LONG).show();

                }


            } else if (response.getG_status() == 0) {


//                if (!isFromMainNewFlow) {
//                    Intent intent = new Intent(activity, MainActivityNewFlow.class);
//                    activity.startActivity(intent);
//                }



                if(!isFromMainNewFlow) {
                    Intent intent = new Intent(activity, MainActivityNewFlow.class);
                    activity.startActivity(intent);
                    activity.finish();
                }

                Intent intentRegSuccess = new Intent(activity, RegistrationSuccessNewFlow.class);
                activity.startActivity(intentRegSuccess);


            } else {

                Toast toast = Toast.makeText(activity, response.getG_status_description(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
            }
        } else {
            switch (error_text_header) {
                case "Please call to customer care to unlock wallet account":
                    Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Please_call_to_customer_care_to_unlock), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();


                    if(!isFromMainNewFlow) {
                        Intent intent = new Intent(activity, MainActivityNewFlow.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }

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


    void showRegistrationAlertDialogue(final Activity _activity) {
        final Dialog promptsView = new Dialog(_activity);
        promptsView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promptsView.setContentView(R.layout.mainmenu_not_registered_alert);

        final Button btn_yes = (Button) promptsView.findViewById(R.id.btn_yes);
        final Button btn_no = (Button) promptsView.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();

                Intent in = new Intent(_activity, OoredooRegistrationNewFlow.class);
                _activity.startActivity(in);


            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();

            }
        });



        promptsView.show();
    }


}

