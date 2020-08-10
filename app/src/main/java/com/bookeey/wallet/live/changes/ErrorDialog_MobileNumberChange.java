package com.bookeey.wallet.live.changes;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
/**
 * Created by 30099 on 1/25/2016.
 */
public class ErrorDialog_MobileNumberChange extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //android O fix bug orientation
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        setContentView(R.layout.errordialog_mobilechange);

        ((Button)findViewById(R.id.Oorredoo_dialog_change_device_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getBaseContext(), MobChangeMobileNumber.class);
                startActivity(intent);
                ErrorDialog_MobileNumberChange.this.finish();
            }
        });
    }
}