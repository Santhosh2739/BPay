package coreframework.processing;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
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
import com.bookeey.wallet.live.paytomerchant.ShowQRCode;

import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.paytomerchant.P2MBarcodeGenInitValidationRequest;

/**
 * Created by 30099 on 1/22/2016.
 */
public class PayToMerchantValidationProcessing implements UserInterfaceBackgroundProcessing {
    private CoreApplication application;
    private String userName;
    private String password;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    //    private String loginResponse = null;
    private GenericResponse response;
    private String barcodeStrngInBigInt;

    private P2MBarcodeGenInitValidationRequest request;

    public PayToMerchantValidationProcessing(P2MBarcodeGenInitValidationRequest request, CoreApplication application, String barcode_big_int, boolean isPost) {
        this.request = request;
        this.isPost = isPost;
        this.application = application;
        this.barcodeStrngInBigInt = barcode_big_int;
    }

    @Override
    public String captureURL() {
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        this.request.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        request.setG_transType(TransType.P2M_BARCODEGEN_VALIDATION_REQUEST.name());
        StringBuffer buffer = new StringBuffer();

        buffer.append(TransType.P2M_BARCODEGEN_VALIDATION_REQUEST.getURL());


//        buffer.append(TransType.P2M_BARCODEGEN_WARBA_VALIDATION_REQUEST.getURL());


        Log.e("QRCodeValidation Data: ",""+new Gson().toJson(request));

        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(request)));
        String serverURL = buffer.toString();

        Log.e("QRCodeValidation URL: ",""+serverURL);

        return serverURL;
    }

    @Override
    public void processResponse(Message msg) {
        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();

            Log.e("QRCodeValidation Res: ",""+network_response);

            response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.P2M_BARCODEGEN_VALIDATION_RESPONSE.name()) && response.getG_status() == 1) {
                this.success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.P2M_BARCODEGEN_VALIDATION_RESPONSE.name()) && response.getG_status() != 1) {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
            } else {
                error_text_header = response.getG_errorDescription();
                error_text_details = response.getG_status_description();
               /* error_text_header = "Failure general server";
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
            Intent intent = new Intent(activity, ShowQRCode.class);
            intent.putExtra("barcode_data", barcodeStrngInBigInt);
            intent.putExtra("payamount", "" + request.getTxnAmount());
            intent.putExtra("barcodedata", "" + request.getBarcodeData());
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
                    case "Tpin cannot be empty":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Tpin_cannot_be_empty), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Daily Txn Count Exceeding":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Daily_Txn_Count_Exceeding), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Monthly Txn Limits Exceeding":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Monthly_Txn_Limits_Exceeding), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Amount is greater than accepted daily transaction limit":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Amount_is_greater_than_accepted_daily_transaction_limit), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Amount is greater or lesser than daily transaction limit":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Amount_is_greater_or_lesser_than_daily_transaction_limit), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "No data found":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.No_data_found), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Account not active":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Account_not_active), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Insufficient balance":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Insufficient_balance), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Customer Auth Failed":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Customer_Auth_Failed), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Invalid token":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Invalid_token), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        break;
                    case "Duplicate Barcode data":
                        toast = Toast.makeText(activity, (String) activity.getResources().getString(R.string.Duplicate_Barcode_data), Toast.LENGTH_SHORT);
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
