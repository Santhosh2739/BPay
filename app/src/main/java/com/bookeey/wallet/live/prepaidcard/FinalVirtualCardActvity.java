package com.bookeey.wallet.live.prepaidcard;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;

import coreframework.taskframework.GenericActivity;
import ycash.wallet.json.pojo.virtualprepaidcards.RequestCardResponse;

/**
 * Created by 30099 on 6/20/2016.
 */
public class FinalVirtualCardActvity extends GenericActivity {
    Button final_virtual_prepaid_close_btn;
    WebView final_virtual_prepaid_web_view;
    String mimeType= "text/html";
    String encoding= "utf-8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_virtual_prepaid);
        View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText(((CoreApplication) getApplication()).getCardName() + "- Card");

        final_virtual_prepaid_close_btn         = (Button) findViewById(R.id.final_virtual_prepaid_close_btn);
        final_virtual_prepaid_web_view          = (WebView) findViewById(R.id.final_virtual_prepaid_web_view);
        RequestCardResponse requestCardResponse= ((CoreApplication)getApplication()).getRequestCardResponse();
        final_virtual_prepaid_web_view.getSettings().setJavaScriptEnabled(true);
        final_virtual_prepaid_web_view.getSettings().setSupportZoom(false);

        final_virtual_prepaid_web_view.getSettings().setBuiltInZoomControls(false);
        final_virtual_prepaid_web_view.loadDataWithBaseURL("", requestCardResponse.getPrepaidhtml(), mimeType, encoding, "");

        final_virtual_prepaid_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), VirtualPrepaidcardListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(getBaseContext(), VirtualPrepaidcardListActivity.class);
        startActivity(intent);
    }
}
