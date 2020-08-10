package coreframework.processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.login.LoginActivity;
import com.google.gson.Gson;

import coreframework.network.ServerConnection;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.UserInterfaceBackgroundProcessing;
import coreframework.utils.URLUTF8Encoder;

import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.sendmoney.SendMoneyFinalScreen;

import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.sendmoney.PeertoPeerCommitRequest;

/**
 * Created by 30099 on 1/8/2016.
 */
public class SendmoneyConformationProcessing implements UserInterfaceBackgroundProcessing {

    private PeertoPeerCommitRequest request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    GenericResponse response = null;

    public SendmoneyConformationProcessing(PeertoPeerCommitRequest request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;
    }

    @Override
    public String captureURL() {
        //Load Security Params to the request
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        this.request.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
       /* this.request.setG_password("836778");
        this.request.setG_security_counter(1001);*/
        this.request.setG_transType(TransType.PEER_TO_PEER_COMMIT.name());
//        this.request.setG_userId("124000099");//124000093//mySigma

        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.PEER_TO_PEER_COMMIT.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(this.request)));
        String send_money_url = buffer.toString();
        return send_money_url;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response.getG_response_trans_type().equalsIgnoreCase(TransType.PEER_TO_PEER_COMMIT_RESPONSE.name()) && response.getG_status() == 0) {
                this.response_json = network_response;
                this.success = true;
            } else if (response.getG_response_trans_type().equalsIgnoreCase(TransType.PEER_TO_PEER_COMMIT_RESPONSE.name()) && response.getG_status() != 0) {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
            } else {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
               /* error_text_header = "Failure general server";
                error_text_details = "Failure general server";*/
            }
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
            error_text_header = (String)application.getApplicationContext().getString(R.string.failure_general_server_error);
            error_text_details = (String)application.getApplicationContext().getString(R.string.failure_general_server_error);
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
            error_text_header = (String)application.getApplicationContext().getString(R.string.failure_network_error);
            error_text_details = (String)application.getApplicationContext().getString(R.string.failure_network_error);
        }
    }

    @Override
    public void performUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment) {
        dialogueFragment.dismiss();
        if (success) {
            Intent intent = new Intent(activity, SendMoneyFinalScreen.class);
            intent.putExtra("response", this.response_json);
            activity.startActivity(intent);
            activity.finish();
        } else {
            if (error_text_header.equalsIgnoreCase("Session expired")) {
               Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.session_expired), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                toast.show();
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
            } else {
                switch (error_text_header) {
                    case "Invalid Password":
                        Toast toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Invalid_Password), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Invalid tpin":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Invalid_tpin), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Problem in processing":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Problem_in_processing), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Receiver Monthly Txn Limits Exceeding":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Receiver_Monthly_Txn_Limits_Exceeding), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Sender Monthly Txn Limits Exceeding":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Sender_Monthly_Txn_Limits_Exceeding), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Receiver Daily Txn Count Exceeding":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Receiver_Daily_Txn_Count_Exceeding), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Sender Daily Txn Count Exceeding":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Sender_Daily_Txn_Count_Exceeding), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Receiver Daily Txn Limits Exceeding":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Receiver_Daily_Txn_Limits_Exceeding), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Sender Daily Txn Limits Exceeding":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Sender_Daily_Txn_Limits_Exceeding), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Receiver Txn Amount Min &Max":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Receiver_Txn_Amount_Min_Max), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Sender Txn Amount Min & Max":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Sender_Txn_Amount_Min_Max), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Daily & Monthly Limits Not Available":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Daily_Monthly_Limits_Not_Available), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Number not Registered.":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.number_not_registered), Toast.LENGTH_SHORT);
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
          /* Toast toast= Toast.makeText(activity, error_text_header, Toast.LENGTH_LONG);
           toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
           toast.show();*/
        }


    }

    @Override
    public boolean isPost() {
        return false;
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
