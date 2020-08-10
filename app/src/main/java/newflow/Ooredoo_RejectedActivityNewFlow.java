package newflow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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
import com.bookeey.wallet.live.registration.Ooredoo_RejectedActivity;
import com.bookeey.wallet.live.registration.RejectEditOoredooRegistration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.registration.CustomerActivationRequest;
import ycash.wallet.json.pojo.registration.CustomerRegistrationRequest;
import ycash.wallet.json.pojo.registration.UpdateCustomerDetailsResponse;

public class Ooredoo_RejectedActivityNewFlow extends Activity {
    Button ok_alert_btn_rejected, ok_alert_btn_edit;
    ProgressDialog progress = null;
    private String response_json = null;
    private TextView tv_rejected_reason ;
    public static final String KEY_REJECTED_REASON = "KEY_REJECTED_REASON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //android O fix bug orientation
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.rejected_case_new);
        progress = new ProgressDialog(Ooredoo_RejectedActivityNewFlow.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ok_alert_btn_rejected = (Button) findViewById(R.id.ok_alert_btn_rejected);
        ok_alert_btn_edit = (Button) findViewById(R.id.ok_alert_btn_edit);

        tv_rejected_reason  = (TextView) findViewById(R.id.tv_rejected_reason);

        Bundle bundle =  getIntent().getExtras();

        if(bundle!=null && bundle.containsKey(KEY_REJECTED_REASON)){


            String rejected_reason = bundle.getString(KEY_REJECTED_REASON).trim();



            if(bundle.getString(KEY_REJECTED_REASON).toLowerCase().contains("Invalid Data".toLowerCase())){
                tv_rejected_reason.setText(getString(R.string.rejected_reason)+" : "+getString(R.string.invalid_data));

            }else if(bundle.getString(KEY_REJECTED_REASON).toLowerCase().contains("Not clear ID".toLowerCase())){

                tv_rejected_reason.setText(getString(R.string.rejected_reason)+" : "+getString(R.string.not_clear_id));

            }else if(bundle.getString(KEY_REJECTED_REASON).toLowerCase().contains("Invalid Name".toLowerCase())){

                tv_rejected_reason.setText(getString(R.string.rejected_reason)+" : "+getString(R.string.invalid_name));

            }else if(bundle.getString(KEY_REJECTED_REASON).toLowerCase().contains("Invalid email Address".toLowerCase())){

                tv_rejected_reason.setText(getString(R.string.rejected_reason)+" : "+getString(R.string.invalid_email_address));

            }else if(bundle.getString(KEY_REJECTED_REASON).toLowerCase().contains("Not proper Response".toLowerCase())){

                tv_rejected_reason.setText(getString(R.string.rejected_reason)+" : "+getString(R.string.not_proper_response));

            }else if(bundle.getString(KEY_REJECTED_REASON).trim().toLowerCase().contains("Civil ID image not available".toLowerCase())){

                tv_rejected_reason.setText(getString(R.string.rejected_reason)+" : "+getString(R.string.civil_id_image_not_available));

            }else{

                tv_rejected_reason.setText(getString(R.string.rejected_reason)+" : "+rejected_reason);

            }


        }else{
            tv_rejected_reason.setVisibility(View.GONE);
        }


        ok_alert_btn_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ooredoo_RejectedActivityNewFlow.this.finish();
            }
        });
        ok_alert_btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchOldRecords();
            }
        });
    }

    private void fetchOldRecords() {

        String mobilenumber = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
        CustomerActivationRequest crr = new CustomerActivationRequest();
        crr.setMobileNumber(mobilenumber);
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        crr.setG_transType(TransType.REJECT_EDIT_REQUEST.name());

        String json = new Gson().toJson(crr);

        Log.e("Server","Data: "+json);

        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.REJECT_EDIT_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));


        Log.e("Server","URL: "+buffer.toString());


        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Log.e("Server","Res: "+(String) msg.obj);

                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    String network_response = ((String) msg.obj).trim();
                    GenericResponse response = new Gson().fromJson(network_response, GenericResponse.class);
                    if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.REJECT_EDIT_RESPONSE.name())) {
                        response_json = network_response;
                        try {
                            // Configure GSON
                            final GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(CustomerRegistrationRequest.class, new UpdateCustomerDetailsResponse());
                            final Gson gson = gsonBuilder.create();

                            UpdateCustomerDetailsResponse customerRejectResponse = gson.fromJson(response_json, UpdateCustomerDetailsResponse.class);
                            String fname = customerRejectResponse.getCustomerRegistrationRequest().getFirstName();
                            String lname = customerRejectResponse.getCustomerRegistrationRequest().getLastName();
                            String civilid = customerRejectResponse.getCustomerRegistrationRequest().getCivilID();
                            String emailid = customerRejectResponse.getCustomerRegistrationRequest().getEmailID();
                            CustomSharedPreferences.saveStringData(getApplicationContext(), fname, CustomSharedPreferences.SP_KEY.FNAME);
                            CustomSharedPreferences.saveStringData(getApplicationContext(), lname, CustomSharedPreferences.SP_KEY.LNAME);
                            CustomSharedPreferences.saveStringData(getApplicationContext(), civilid, CustomSharedPreferences.SP_KEY.CIVILDID);
                            CustomSharedPreferences.saveStringData(getApplicationContext(), emailid, CustomSharedPreferences.SP_KEY.EMIALID);

                            Intent intent = new Intent(Ooredoo_RejectedActivityNewFlow.this, RejectEditOoredooRegistration.class);
                            //intent.putExtra("reject_edit_respose", updateCustomerDetailsResponse);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Ooredoo_RejectedActivityNewFlow.this.startActivity(intent);
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (null != response && response.getG_status() == -1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.REJECT_EDIT_RESPONSE.name())) {
                        Toast toast = Toast.makeText(Ooredoo_RejectedActivityNewFlow.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();

                    } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(Ooredoo_RejectedActivityNewFlow.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                    } else {
                        Toast.makeText(Ooredoo_RejectedActivityNewFlow.this, getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(Ooredoo_RejectedActivityNewFlow.this, getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(Ooredoo_RejectedActivityNewFlow.this, getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
        showIfNotVisible("");

    }

    private void hideIfVisible() {
        if (progress.isShowing()) {
            progress.hide();
        }
    }

    private void showIfNotVisible(String title) {
        if (!progress.isShowing()) {
            progress.setTitle(title);
            progress.show();
            progress.isShowing();
        } else {
            progress.setTitle(title);
            progress.show();
            progress.isShowing();
        }
    }
}
