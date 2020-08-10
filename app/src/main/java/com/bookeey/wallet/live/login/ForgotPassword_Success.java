package com.bookeey.wallet.live.login;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.SyncService;

/**
 * Created by 30099 on 1/21/2016.
 */
public class ForgotPassword_Success extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.forgot_password_success_new);
        findViewById(R.id.activity_reset_dialog_confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword_Success.this, SyncService.class);
                stopService(intent);
                //To find service is running or not
                boolean isRunning=isServiceRunning();
                //Toast.makeText(ForgotPassword_Success.this,""+isRunning,Toast.LENGTH_SHORT).show();
                ForgotPassword_Success.this.finish();

            }
        });
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.example.MyNeatoIntentService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;

    }
}