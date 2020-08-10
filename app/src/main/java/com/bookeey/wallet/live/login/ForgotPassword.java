package com.bookeey.wallet.live.login;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;

import coreframework.database.CustomSharedPreferences;
import coreframework.processing.ForgotPasswordProcessing;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;

import ycash.wallet.json.pojo.forgotPassword.ForgotPinRequest;

/**
 * Created by 30099 on 1/20/2016.
 */
public class ForgotPassword extends FragmentActivity implements YPCHeadlessCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //android O fix bug orientation
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        setContentView(R.layout.forgot_password_new);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        findViewById(R.id.reset_password_confirm_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpasswordProcessing();
            }
        });
        findViewById(R.id.reset_password_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPassword.this.finish();
            }
        });
    }

    void forgotpasswordProcessing() {
        String deviceId = ((CoreApplication) getApplication()).getThisDeviceUniqueAndroidId();
        String mobilenumber = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.MOBILE_NUMBER);
        ForgotPinRequest forgotPinRequest = new ForgotPinRequest();
        forgotPinRequest.setDeviceID(deviceId);
        forgotPinRequest.setMobileNumber(mobilenumber);
        CoreApplication application = (CoreApplication) getApplication();
        String uiProcessorReference = application.addUserInterfaceProcessor(new ForgotPasswordProcessing(forgotPinRequest, application, true));
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
        Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
        startActivity(intent);
        ForgotPassword.this.finish();
    }
}