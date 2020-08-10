package com.bookeey.wallet.live.registration;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.bookeey.wallet.live.R;
/**
 * Created by 30099 on 12/31/2015.
 */
public class Ooredoo_NotApprovedActivity extends Activity {
    Button ok_alert_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //android O fix bug orientation
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.not_yet_approved_new);

        ok_alert_btn= (Button)findViewById(R.id.ok_alert_btn);
        ok_alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ooredoo_NotApprovedActivity.this.finish();
            }
        });
    }
}