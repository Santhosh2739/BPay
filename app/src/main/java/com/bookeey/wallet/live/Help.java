package com.bookeey.wallet.live;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import coreframework.taskframework.GenericActivity;

/**
 * Created by 30099 on 2/8/2016.
 */
public class Help extends GenericActivity {


    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helplist);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        SimpleExpandableListAdapterWithEmptyGroups expListAdapter = new SimpleExpandableListAdapterWithEmptyGroups(
                this, createGroupList(), R.layout.help_new_group_row_exp,
                new String[]{"colorName"}, new int[]{R.id.groupTv},
                createChildList(), R.layout.help_new_child_row_exp,
                new String[]{"shadeName", "rgb"}, new int[]{R.id.child1Tv,
                R.id.child2Tv});

        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("HELP");*/


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
        mTitleTextView.setText(getResources().getString(R.string.help_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ExpandableListView exp_list = (ExpandableListView) findViewById(R.id.exp_list);
        exp_list.setAdapter(expListAdapter);
        ((Button) findViewById(R.id.faq_close_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Help.this.finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Help page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 27);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Help page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 27 logged");
    }

    private List<HashMap<String, String>> createGroupList() {
        String colors[] = {getResources().getString(R.string.bookey_fq1),
                getResources().getString(R.string.bookey_fq2),
                getResources().getString(R.string.bookey_fq3),
                getResources().getString(R.string.bookey_fq4),
                getResources().getString(R.string.bookey_fq5),
                getResources().getString(R.string.bookey_fq6),
                getResources().getString(R.string.bookey_fq7),
                getResources().getString(R.string.bookey_fq8),
                getResources().getString(R.string.bookey_fq9),
                getResources().getString(R.string.bookey_fq10),
                getResources().getString(R.string.bookey_fq11),
                getResources().getString(R.string.bookey_fq12),
                getResources().getString(R.string.bookey_fq13),
                getResources().getString(R.string.bookey_fq14),
                getResources().getString(R.string.bookey_fq15),
                getResources().getString(R.string.bookey_fq16),
                getResources().getString(R.string.bookey_fq17),
                getResources().getString(R.string.bookey_fq18),
                getResources().getString(R.string.bookey_fq19),

                getResources().getString(R.string.bookey_fq20),
                getResources().getString(R.string.bookey_fq21),
                getResources().getString(R.string.bookey_fq22),
                getResources().getString(R.string.bookey_fq23),
                getResources().getString(R.string.bookey_fq24),
                getResources().getString(R.string.bookey_fq25),
                getResources().getString(R.string.bookey_fq26),
                getResources().getString(R.string.bookey_fq27),
                getResources().getString(R.string.bookey_fq28),
                getResources().getString(R.string.bookey_fq29),
                getResources().getString(R.string.bookey_fq30),
                getResources().getString(R.string.bookey_fq31),


        };

        ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < colors.length; ++i) {
            HashMap<String, String> m = new HashMap<String, String>();
            m.put("colorName", colors[i]);
            result.add(m);
        }
        return result;
    }

    private List<ArrayList<HashMap<String, String>>> createChildList() {
        String shades[][] = {
                {getResources().getString(R.string.bookey_fa1), "",},
                {getResources().getString(R.string.bookey_fa2), "",},
                {getResources().getString(R.string.bookey_fa3), "",},
                {getResources().getString(R.string.bookey_fa4), ""},
                {getResources().getString(R.string.bookey_fa5), " "},
                {getResources().getString(R.string.bookey_fa6), ""},
                {getResources().getString(R.string.bookey_fa7), ""},
                {getResources().getString(R.string.bookey_fa8), ""},
                {getResources().getString(R.string.bookey_fa9), ""},
                {getResources().getString(R.string.bookey_fa10), ""},
                {getResources().getString(R.string.bookey_fa11), ""},
                {getResources().getString(R.string.bookey_fa12), ""},
                {getResources().getString(R.string.bookey_fa13), ""},
                {getResources().getString(R.string.bookey_fa14), ""},
                {getResources().getString(R.string.bookey_fa15), ""},
                {getResources().getString(R.string.bookey_fa16), ""},
                {getResources().getString(R.string.bookey_fa17), ""},
                {getResources().getString(R.string.bookey_fa18), ""},
                {getResources().getString(R.string.bookey_fa19), ""},

                {getResources().getString(R.string.bookey_fa20), ""},
                {getResources().getString(R.string.bookey_fa21), ""},
                {getResources().getString(R.string.bookey_fa22), ""},
                {getResources().getString(R.string.bookey_fa23), ""},
                {getResources().getString(R.string.bookey_fa24), ""},
                {getResources().getString(R.string.bookey_fa25), ""},
                {getResources().getString(R.string.bookey_fa26), ""},
                {getResources().getString(R.string.bookey_fa27), ""},
                {getResources().getString(R.string.bookey_fa28), ""},
                {getResources().getString(R.string.bookey_fa29), ""},
                {getResources().getString(R.string.bookey_fa30), ""},
                {getResources().getString(R.string.bookey_fa31), ""},


        };

        ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<ArrayList<HashMap<String, String>>>();
        for (int i = 0; i < shades.length; ++i) {
            ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
            for (int n = 0; n < shades[i].length; n += 2) {
                HashMap<String, String> child = new HashMap<String, String>();
                child.put("shadeName", shades[i][n]);
                child.put("rgb", shades[i][n + 1]);
                secList.add(child);
            }
            result.add(secList);
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}