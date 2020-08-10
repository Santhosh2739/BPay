package newflow_processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.google.gson.Gson;

import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;
import newflow.MobileBill_L1_ActivityNewFlow;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticL1RequestPojo;

/**
 * Created by 10037 on 6/19/2017.
 */

public class MobileBill_Denomiantions_ProcessingNewFlow implements UserInterfaceBackgroundProcessing {

    private DomesticL1RequestPojo request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    CustomerLoginRequestReponse customerLoginRequestReponse = null;

    public MobileBill_Denomiantions_ProcessingNewFlow(DomesticL1RequestPojo request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;

    }

    @Override
    public String captureURL() {
        customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        this.request.setG_oauth_2_0_client_token("");
        this.request.setG_transType(TransType.NEW_DOMESTIC_RECHARGE_DENOMINATION_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.NEW_DOMESTIC_RECHARGE_DENOMINATION_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(this.request)));
        String serverURL = buffer.toString();
        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            if (!network_response.isEmpty()) {
                GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
                if (response.getG_response_trans_type().equalsIgnoreCase(TransType.NEW_DOMESTIC_RECHARGE_DENOMINATION_RESPONSE.name()) && response.getG_status() == 1) {
                    this.response_json = network_response;
                    this.success = true;
                } else if (response.getG_response_trans_type().equalsIgnoreCase(TransType.NEW_DOMESTIC_RECHARGE_DENOMINATION_RESPONSE.name()) && response.getG_status() != 1) {
                    error_text_header = response.getG_errorDescription();
                    error_text_details = response.getG_status_description();
                } else {
                    error_text_header = response.getG_errorDescription();
                    error_text_details = response.getG_status_description();
               /* error_text_header = "Failure general server";
                error_text_details = "Failure general server";*/
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
            Intent intent = new Intent(activity, MobileBill_L1_ActivityNewFlow.class);
            intent.putExtra("DENOMINATIONS_RESPONSE", this.response_json);
            activity.startActivity(intent);
            // activity.finish();
        } else {
            if (error_text_header.equalsIgnoreCase("Session expired")) {
                Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
            } else {
                if (error_text_header.equalsIgnoreCase("Denomination Not available")) {
                    Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Denominations_are_not_available), Toast.LENGTH_SHORT);
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
                        case "Services are down":
                            toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.services_are_down), Toast.LENGTH_SHORT);
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
