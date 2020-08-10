package com.bookeey.wallet.live;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;
import coreframework.taskframework.GenericActivity;
/**
 * Created by 30099 on 11/5/2015.
 */
public class OoredooWelcome extends GenericActivity {
    TextView welcome_screen_english_language_text, welcome_screen_arabic_language_text;
    Locale myLocale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ooredoo_welcome);
        ActionBar actionBar = getActionBar();
        actionBar.hide();
        welcome_screen_english_language_text = (TextView) findViewById(R.id.splash_screen_english_language_text);
        welcome_screen_arabic_language_text = (TextView) findViewById(R.id.splash_screen_arabic_language_text);
        welcome_screen_english_language_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("en");
            }
        });
        welcome_screen_arabic_language_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("ar");
            }
        });
    }
    public void setLocale(String lang) {
        try{
            myLocale = new Locale(lang);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, OoredooValidation.class);
            refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(refresh);
        }catch(Exception e){
            Log.e("", "");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}