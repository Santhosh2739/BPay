package com.bookeey.wallet.live.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;

import coreframework.processing.ResetTPinProcessing;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.registration.CustomerActivationRequest;

/**
 * Created by 10037 on 7/219/2017.
 */
public class ResetTPIN extends FragmentActivity implements YPCHeadlessCallback {
    Button reset_tpin_confirm_btn, reset_tpin_close_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reset_tpin_new);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        reset_tpin_close_btn = (Button) findViewById(R.id.reset_tpin_close_btn);
        reset_tpin_confirm_btn = (Button) findViewById(R.id.reset_tpin_confirm_btn);
        reset_tpin_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTPin();
            }
        });
        reset_tpin_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetTPIN.this.finish();
            }
        });
    }

    private void resetTPin() {
        CustomerActivationRequest customerActivationRequest = new CustomerActivationRequest();
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        customerActivationRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        String uiProcessorReference = application.addUserInterfaceProcessor(new ResetTPinProcessing(customerActivationRequest, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
    }

    @Override
    public void onProgressUpdate(int progress) {
    }

    @Override
    public void onProgressComplete() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}