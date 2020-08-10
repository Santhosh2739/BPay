package com.bookeey.wallet.live;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookeey.wallet.live.txnhistory.About_Us_Activity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.w3c.dom.Text;

import java.util.Locale;

import coreframework.taskframework.GenericActivity;
/**
 * Created by 30099 on 7/1/2015.
 */
public class Contact_Us_Activity extends GenericActivity {

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("CONTACT US");*/

        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.map_specialactionbar, null);
        mActionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mActionBar.setCustomView(mCustomView, params);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.merchant_category_screen_title_text);
        mTitleTextView.setText(getResources().getString(R.string.contact_us_title));
        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ((Button)findViewById(R.id.contact_us_screen_connect_executive_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phnumber = ((TextView)findViewById(R.id.contact_us_screen_mobile_number_text)).getText().toString().trim();
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:" + phnumber));
                startActivity(intent);
            }
        });


        ((ImageView)findViewById(R.id.contact_us_call)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phnumber = ((TextView)findViewById(R.id.contact_us_screen_mobile_number_text)).getText().toString().trim();
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:" + phnumber));
                startActivity(intent);
            }
        });

        ((TextView)findViewById(R.id.contact_us_screen_mobile_number_text)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phnumber = ((TextView)findViewById(R.id.contact_us_screen_mobile_number_text)).getText().toString().trim();
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse("tel:" + phnumber));
                startActivity(intent);

            }
        });






                ((TextView)findViewById(R.id.contact_us_social_bookeeywallet)).setText("@BookeeyPay");





                ((ImageView)findViewById(R.id.contact_us_location_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", 29.382653, 47.986163);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);

            }
        });

        ((ImageView)findViewById(R.id.contact_us_facebook_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String url = "https://www.facebook.com/bookeeywallet/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        ((ImageView)findViewById(R.id.contact_us_instagram_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "https://www.instagram.com/bookeeywallet/?hl=en";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });


        ((ImageView)findViewById(R.id.contact_us_snapchat_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://www.snapchat.com/add/bookeeywallet";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);


            }
        });

        ((ImageView)findViewById(R.id.contact_us_youtube_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "https://www.youtube.com/channel/UC8bS6FVe_Sz1cBdZOMLznjw";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        ((ImageView)findViewById(R.id.contact_us_linkedin_icon)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "https://www.linkedin.com/showcase/bookeey/about/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Contact us page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 31);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Contact us page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 31 logged");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}