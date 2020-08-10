package merchant;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.google.gson.Gson;

import coreframework.network.ServerConnection;
import coreframework.taskframework.BackgroundProcessingAbstractFilter;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantRequest;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantStaticQRConfirm;

public class PayToMerchantStaticQRCodeConfirmProcessing extends BackgroundProcessingAbstractFilter {

    private PayToMerchantStaticQRConfirm request;
    private String response_json = null;
    private CoreApplication application;
    private boolean isPost = false;
    private boolean success = false;
    private String error_text_header = "";
    private String error_text_details = "";
    CustomerLoginRequestReponse customerLoginRequestReponse = null;


    public PayToMerchantStaticQRCodeConfirmProcessing(PayToMerchantStaticQRConfirm request, CoreApplication application, boolean isPost) {
        this.request = request;
        this.application = application;
        this.isPost = isPost;

    }

    @Override
    public String captureURL() {

        customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        this.request.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());

        this.request.setG_transType(TransType.SCAN_STATIC_QR_CODE_CONFIRMATION_REQUEST.name());
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.SCAN_STATIC_QR_CODE_CONFIRMATION_REQUEST.getURL());

        Log.e("Static Confirm data: ", "" + new Gson().toJson(this.request));


        buffer.append("?d=" + URLUTF8Encoder.encode(new Gson().toJson(this.request)));
        String static_qr_code_url = buffer.toString();

        Log.e("Static Confirm Url: ", "" + static_qr_code_url);

        return static_qr_code_url;
    }

    @Override
    public void processResponse(Message msg) {

        Log.e("Static Confirm Resp: "," "+((String)msg.obj).trim());

//        {"transactionId":"P2M19001045","balance":101.05,"processingfee":0.0,"total":6.0,"serverTime":1575529199512,"serverTimeL2":1575529199512,"txnAmount":6.0,"customerMobileNumber":"98037947","merchant_balance":0.0,"totalAmountDebittedFromCust":0.0,"totalAmountCreditedToMerchant":0.0,"discountPrice":0.0,"totalPrice":6.0,"g_status":1,"g_status_description":"Transaction success","g_errorDescription":"","g_response_trans_type":"SCAN_STATIC_QR_CODE_RESPONSE"}

        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
            String network_response = ((String) msg.obj).trim();
            GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
            if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.SCAN_STATIC_QR_CODE_CONFIRMATION_RESPONSE.name()) && response.getG_status() != -1) {
                this.response_json = network_response;
                this.success = true;
            } else if (response != null && response.getG_response_trans_type().equalsIgnoreCase(TransType.SCAN_STATIC_QR_CODE_CONFIRMATION_RESPONSE.name())) {
                error_text_header = response.getG_response_trans_type();
                error_text_details = response.getG_errorDescription();
            } else {
                error_text_header = "ServerConnection.OPERATION_FAILURE_GENERAL_SERVER";
                error_text_details = "ServerConnection.OPERATION_FAILURE_GENERAL_SERVER";
            }
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
            error_text_header = "ServerConnection.OPERATION_FAILURE_GENERAL_SERVER";
            error_text_details = "ServerConnection.OPERATION_FAILURE_GENERAL_SERVER";
        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
            error_text_header = "ServerConnection.OPERATION_FAILURE_NETWORK";
            error_text_details = "ServerConnection.OPERATION_FAILURE_NETWORK";
        }
    }

    @Override
    public void performUserInterfaceAndDismiss(Activity activity, ProgressDialogFrag dialogueFragment) {
        dialogueFragment.dismiss();

//        Toast.makeText(activity, "Response: " + this.response_json, Toast.LENGTH_LONG).show();


        try {


            if (success) {


                Intent intent = new Intent(activity, StaticQRCodePaymentFinalScreenCX.class);
                intent.putExtra("response", this.response_json);
                activity.startActivity(intent);
                activity.finish();

            } else {


                //Handle Any types of failure here:
                if (activity instanceof GenericActivity) {

                    Toast.makeText(application, error_text_details, Toast.LENGTH_LONG).show();


                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();

                } else {
                    Toast.makeText(activity, error_text_header + "-" + error_text_details, Toast.LENGTH_SHORT).show();
                }



            }


        } catch (Exception e) {
            Toast.makeText(application, "System Error! " + e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public boolean isPost() {
        return false;
    }

    @Override
    public boolean isLocalProcess() {
        return false;
    }

    @Override
    public void performTask() {

    }
}
