package coreframework.processing.prepaidcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.prepaidcard.VirtualPrepaidMainActivityNew;
import com.bookeey.wallet.live.prepaidcard.VirtualPrepaid_final_Activity;
import com.bookeey.wallet.live.recharge.TopUpInitialActivity;
import com.bookeey.wallet.live.recharge.TopupL1ScreenActvity;
import com.google.gson.Gson;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticL2RequestPojo;

/**
 * Created by 10037 on 6/19/2017.
 */

public class VocherL2Processing implements UserInterfaceBackgroundProcessing {

    private DomesticL2RequestPojo request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    CustomerLoginRequestReponse customerLoginRequestReponse = null;

    public VocherL2Processing(DomesticL2RequestPojo request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;

    }

    @Override
    public String captureURL() {
        customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        this.request.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        this.request.setG_transType(TransType.VOCHER_RECHARGE_L2_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.VOCHER_RECHARGE_L2_REQUEST.getURL());
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
                if (response.getG_response_trans_type().equalsIgnoreCase(TransType.VOCHER_RECHARGE_L2_RESPONSE.name()) && response.getG_status() == 1) {
                    this.response_json = network_response;
                    this.success = true;
                } else if (response.getG_response_trans_type().equalsIgnoreCase(TransType.VOCHER_RECHARGE_L2_RESPONSE.name()) && response.getG_status() != 1) {
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
            Intent intent = new Intent(activity, VirtualPrepaid_final_Activity.class);
            intent.putExtra("CARD_FINAL_RESPONSE", this.response_json);
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
                String prepaidcard_res= CustomSharedPreferences.getStringData(activity, CustomSharedPreferences.SP_KEY.PREPAID_RESPONSE);

                switch (error_text_header) {
                    case "Duplicate Transaction ":
                        Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Duplicate_Transaction), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        break;
                    case "There was no transaction Found with the transaction":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.There_was_no_transaction_Found_with_the_transaction), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        break;
                    case "Service Temporarily Unavailable":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Service_Temporarily_Unavailable), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        break;
                    case "Monthly Recharge Limits Exceeding":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Monthly_Recharge_Limits_Exceeding), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        break;
                    case "Daily Recharge Limits Exceeding":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Daily_Recharge_Limits_Exceeding), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        break;
                    case "Recharge Amount Min & Max":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Recharge_Amount_Min_Max), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        break;
                    case "Daily & Monthly Limits Not Available":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Daily_Monthly_Limits_Not_Available), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        break;
                    case "wrong tpin please enter correct tpin":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.wrong_tpin_please_enter_correct_tpin), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        break;
                    case "Customer Was not Register":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Customer_Was_not_Register), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        break;
                    case "Failure general server":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        break;
                    case "Failure network error":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                        break;
                    default:
                        toast = Toast.makeText(activity, error_text_header, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        intent = new Intent(activity, VirtualPrepaidMainActivityNew.class);
                        intent.putExtra("CARD_RESPONSE", prepaidcard_res);
                        intent.putExtra("BACK", "back");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
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
