package com.bookeey.wallet.live;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.registration.TermsAndConditions;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.GenericActivity;
import coreframework.utils.LocaleHelper;

/**
 * Created by 30099 on 2/25/2016.
 */
public class CheckForUpdatesActivity extends GenericActivity {
    String versioncode_str, newVersion = null;
    int versioncode = 0;
    String versionname;
    ProgressDialog dialog = null;
    TextView check_for_updates_up_to_date, check_for_updates_version_no;
    ProgressDialog progress;
    private String selectedLanguage = null;

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkforupdates);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        dialog = new ProgressDialog(CheckForUpdatesActivity.this, R.style.MyTheme2);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        check_for_updates_up_to_date = (TextView) findViewById(R.id.check_for_updates_up_to_date);
        check_for_updates_version_no = (TextView) findViewById(R.id.check_for_updates_version_no);

        selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(CheckForUpdatesActivity.this, selectedLanguage);
        }
        ((Button) findViewById(R.id.check_for_updates_close_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckForUpdatesActivity.this.finish();
            }
        });
        showMenu(false);
        progress = new ProgressDialog(CheckForUpdatesActivity.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

       /* View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("CHECKING FOR UPDATE");*/

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
        mTitleTextView.setText(getResources().getString(R.string.check_for_update_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        versionname = getIntent().getStringExtra("VersionName");

        VersionChecker versionChecker = new VersionChecker();
        versionChecker.execute();
        //old code by krishna
        // Update task = new Update();
        //task.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Check for updates page");


        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 32);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Check for updates page");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase "," Event 32 logged");
    }

    public class Update extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                //old way to get app version
                /*newVersion = Jsoup
                        .connect(
                                "https://play.google.com/store/apps/details?id="
                                        + "com.bookeey.wallet.live" + "&hl=en")
                        .timeout(30000)
                        .userAgent(
                                "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com").get()
                        .select("div[itemprop=softwareVersion]").first()
                        .ownText();*/

                //New way to get app version

                /*newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "trai.gov.in.dnd" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".xyOfqd .hAyfc:nth-child(4) .htlgb span")
                        .get(0)
                        .ownText();*/
                return newVersion;
            } catch (Exception e) {
                CheckForUpdatesActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CheckForUpdatesActivity.this, "Unknown error occurred..", Toast.LENGTH_LONG).show();
                    }
                });
                //  Toast.makeText(CheckForUpdatesActivity.this, ""+e,Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return e;

            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                dialog.dismiss();
                if (o != null) {
                    Double d_new = new Double((String) o);
                    Double d_old = new Double(versionname);
                    if (d_old < d_new) {

                        check_for_updates_up_to_date.setText(getResources().getString(R.string.check_not_up_to_date));
                        check_for_updates_version_no.setText(getResources().getString(R.string.current_version) + versionname);

                        LayoutInflater li = LayoutInflater.from(CheckForUpdatesActivity.this);
                        View promptsView = li.inflate(R.layout.custom_update_playstore_dialog, null);
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckForUpdatesActivity.this);
                        alertDialog.setView(promptsView);
                        alertDialog.setPositiveButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()));
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                        alertDialog.show();
                    } else {
                        check_for_updates_up_to_date.setText(getResources().getString(R.string.check_up_to_date));
                        check_for_updates_version_no.setText(getResources().getString(R.string.current_version) + versionname);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public class VersionChecker extends AsyncTask<String, String, String> {
        private String newVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showIfNotVisible("");

        }

        @Override
        protected void onPostExecute(String latestVersion) {
            hideIfVisible();

            if (latestVersion != null && !latestVersion.isEmpty()) {
                double live_version = Double.parseDouble(latestVersion);
                double local_version = Double.parseDouble(versionname);

                if (local_version < live_version) {

                    check_for_updates_up_to_date.setText(getResources().getString(R.string.check_not_up_to_date));
                    check_for_updates_version_no.setText(getResources().getString(R.string.current_version) + local_version);

                    LayoutInflater li = LayoutInflater.from(CheckForUpdatesActivity.this);
                    View promptsView = li.inflate(R.layout.custom_update_playstore_dialog, null);
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckForUpdatesActivity.this);
                    alertDialog.setView(promptsView);


                    alertDialog.setPositiveButton(getResources().getString(R.string.update), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });


                    alertDialog.setNegativeButton(getResources().getString(R.string.no_newflow), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });



                    alertDialog.setCancelable(false);
                    alertDialog.show();

                } else {
                    check_for_updates_up_to_date.setText(getResources().getString(R.string.check_up_to_date));
                    check_for_updates_version_no.setText(getResources().getString(R.string.current_version) + versionname);
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                //new way to get version number
               /* newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "trai.gov.in.dnd" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".xyOfqd .hAyfc:nth-child(4) .htlgb span")
                        .get(0)
                        .ownText();*/

                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;
        }


    }

    private void hideIfVisible() {
        if (progress.isShowing()) {
            progress.hide();
        }
    }

    private void showIfNotVisible(String title) {
        if (!progress.isShowing()) {
            progress.setTitle(title);
            progress.show();
            progress.isShowing();
        } else {
            progress.setTitle(title);
            progress.show();
            progress.isShowing();
        }
    }

}