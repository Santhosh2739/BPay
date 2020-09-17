package coreframework.processing.Registration_processing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.registration.OoredooRegistration;
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;

import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.changes.ErrorDialog_DeviceIdChange;
import com.bookeey.wallet.live.changes.ErrorDialog_MobileNumberChange;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.registration.OoredooActivation;
import com.bookeey.wallet.live.registration.Ooredoo_NotApprovedActivity;
import com.bookeey.wallet.live.registration.Ooredoo_RejectedActivity;

import newflow.MainActivityNewFlow;
import newflow.OoredooRegistrationNewFlow;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.registration.CustomerMobileNumberRequest;
import ycash.wallet.json.pojo.registration.CustomerMobileNumberRequestResponse;

/**
 * Created by 30099 on 11/18/2015.
 */
public class CustomerMobileValidationProcessing implements UserInterfaceBackgroundProcessing {
    private CustomerMobileNumberRequest request = null;
    private String response_json = null;
    private CoreApplication application;
    private GenericResponse response;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    private CustomerMobileNumberRequestResponse activationRequestResponse = null;

    public CustomerMobileValidationProcessing(CustomerMobileNumberRequest request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;
    }

    @Override
    public String captureURL() {
        this.request.setG_transType(TransType.CHECK_BY_MOBILE_NUMBER.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.CHECK_BY_MOBILE_NUMBER.getURL());
        String json = new Gson().toJson(request);
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(request)));
        String serverURL = buffer.toString();
        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            response = new Gson().fromJson(network_response, GenericResponse.class);
            if (network_response != null & !network_response.isEmpty()) {
                if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.CHECK_BY_MOBILE_NUMBER_RESPONSE.name())) {
                    this.response_json = network_response;
                    activationRequestResponse = new Gson().fromJson(this.response_json, CustomerMobileNumberRequestResponse.class);
                    this.success = true;
                } else {
                    error_text_header = response.getG_errorDescription();
                    error_text_details = response.getG_status_description();
                /*error_text_header = "Failure general server";
                error_text_details = "Failure general server";*/
                }
            }else {
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
            if (response.getG_status() == 95) {
                CustomSharedPreferences.saveStringData(activity, activationRequestResponse.getName(), CustomSharedPreferences.SP_KEY.NAME);
                CustomSharedPreferences.saveIntData(application, CustomSharedPreferences.APP_STATUS_ACTIVATED, CustomSharedPreferences.SP_KEY.APP_STATUS);
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
            } else if (response.getG_status() == 96) {
                Intent intent = new Intent(activity, Ooredoo_NotApprovedActivity.class);
                activity.startActivity(intent);
                activity.finish();
            } else if (response.getG_status() == 98) {
                Intent intent = new Intent(activity, Ooredoo_RejectedActivity.class);
                activity.startActivity(intent);
                activity.finish();
            } else if (response.getG_status() == 97) {
                activity.startActivity(new Intent(activity, OoredooActivation.class));
                activity.finish();
            } else if (response.getG_status() == 83) {
                //   Intent intent = new Intent(activity, OoredooRegistrationNew.class);

                //Jan 08 START
//                Intent intent = new Intent(activity, OoredooRegistration.class);
//                activity.startActivity(intent);
                //Jan 08 END

                showRegistrationAlertDialogue(activity);

            } else if (response.getG_status() == 209) {
                Intent intent = new Intent(activity, ErrorDialog_DeviceIdChange.class);
                activity.startActivity(intent);
                activity.finish();
            } else if (response.getG_status() == 206) {
                Intent intent = new Intent(activity, ErrorDialog_MobileNumberChange.class);
                activity.startActivity(intent);
                activity.finish();
            } else {
                Toast toast = Toast.makeText(activity, response.getG_status_description(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
            }
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
                case "Services are down":
                    toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.services_are_down), Toast.LENGTH_SHORT);
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

    void showRegistrationAlertDialogue(final Activity activity) {
        final Dialog promptsView = new Dialog(activity);
        promptsView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promptsView.setContentView(R.layout.mainmenu_not_registered_alert);

        final Button btn_yes = (Button) promptsView.findViewById(R.id.btn_yes);
        final Button btn_no = (Button) promptsView.findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();

                Intent intent = new Intent(activity, OoredooRegistration.class);
                activity.startActivity(intent);


            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                promptsView.dismiss();

                activity.finish();

            }
        });



        promptsView.show();
    }

}
