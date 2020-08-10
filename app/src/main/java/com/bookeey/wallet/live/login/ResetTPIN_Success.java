package com.bookeey.wallet.live.login;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.bookeey.wallet.live.R;

/**
 * Created by 10037 on 7/19/2017.
 */
public class ResetTPIN_Success extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.reset_tpin_success_new);
        findViewById(R.id.activity_reset_tpin_dialog_confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetTPIN_Success.this.finish();
            }
        });
    }
}