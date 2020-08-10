package com.bookeey.wallet.live.registration;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.bookeey.wallet.live.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class RegistrationSuccessNewFlow extends Activity {



    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        //android O fix bug orientation
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.registration_success_new);


        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SIGN_UP_METHOD, "SignUp");
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);



        ((Button) findViewById(R.id.ooredoo_success_screen_close_button)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

             finish();

            }
        });
    }
}