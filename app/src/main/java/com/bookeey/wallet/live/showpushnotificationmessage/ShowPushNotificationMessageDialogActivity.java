package com.bookeey.wallet.live.showpushnotificationmessage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.registration.Ooredoo_RejectedActivity;
import com.bookeey.wallet.live.registration.RejectEditOoredooRegistration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.utils.URLUTF8Encoder;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.registration.CustomerActivationRequest;
import ycash.wallet.json.pojo.registration.CustomerRegistrationRequest;
import ycash.wallet.json.pojo.registration.UpdateCustomerDetailsResponse;

public class ShowPushNotificationMessageDialogActivity extends Activity {
    Button ok_alert_btn_rejected, ok_alert_btn_edit;
    ProgressDialog progress = null;
    private String response_json = null;

    public static final String KEY_PUSH_NOTIFICATION_MESSAGES = "KEY_PUSH_NOTIFICATION_MESSAGES";
    private TextView push_notification_message ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //android O fix bug orientation
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.show_push_notification_message_from_server);
        progress = new ProgressDialog(this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        ok_alert_btn_rejected = (Button) findViewById(R.id.ok_alert_btn_rejected);
        ok_alert_btn_edit = (Button) findViewById(R.id.ok_alert_btn_edit);


        push_notification_message  = (TextView) findViewById(R.id.push_notification_message);

        Bundle bundle =  getIntent().getExtras();

        if(bundle!=null && bundle.containsKey(KEY_PUSH_NOTIFICATION_MESSAGES)){

//            push_notification_message.setText(bundle.getString(KEY_PUSH_NOTIFICATION_MESSAGES));

            LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.push_notification_message_ll);


          String pushMessages =  bundle.getString(KEY_PUSH_NOTIFICATION_MESSAGES);

          try {
              JSONObject whole_jo = new JSONObject(pushMessages);
              JSONArray ja = whole_jo.getJSONArray("list");

              ScrollView push_notification_message_sl  = (ScrollView)findViewById(R.id.push_notification_message_sl) ;


if(ja.length()>2) {
    push_notification_message_sl.getLayoutParams().height = 600;
    push_notification_message_sl.requestLayout();
}



              for (int i = 0; i < ja.length(); i++) {


                  View row_layout = LayoutInflater.from(this).inflate(R.layout.push_notification_message_row, mLinearLayout, false);
                  TextView push_notification_message = (TextView) row_layout.findViewById(R.id.push_notification_message);

                  TextView push_notification_message_duration_ago = (TextView) row_layout.findViewById(R.id.push_notification_message_duration_ago);


                  JSONObject jo = ja.getJSONObject(i);
                  if(i==ja.length()-1) {

                      if(ja.length()==1) {
                          push_notification_message.setText(jo.getString("message") +"\n");
                      }else{
                          push_notification_message.setText(jo.getString("message") + "\n\n\n\n");
                      }

                  }else{
                      push_notification_message.setText(jo.getString("message"));
                  }

                  if(i>0) {
                      push_notification_message_duration_ago.setText("\n"+jo.getString("time"));
                  }else{
                      push_notification_message_duration_ago.setText(jo.getString("time"));
                  }

                  if(i==ja.length()-1){

                      View horizontal_line = (View)row_layout.findViewById(R.id.horizontal_line);
                      horizontal_line.setVisibility(View.INVISIBLE);

                  }

//                  if(ja.length()==1){
//
//                      View horizontal_line = (View)row_layout.findViewById(R.id.horizontal_line);
//                      horizontal_line.setVisibility(View.VISIBLE);
//
//                  }

                  mLinearLayout.addView(row_layout);

              }



          }catch (Exception e){

              Toast.makeText(ShowPushNotificationMessageDialogActivity.this, "System Erroe! "+e.getMessage(),Toast.LENGTH_LONG).show();

          }


        }else{
            push_notification_message.setVisibility(View.GONE);
        }



        ok_alert_btn_rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ok_alert_btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        ImageView  btn_close = (ImageView)findViewById(R.id.btn_close);

        btn_close .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }


}