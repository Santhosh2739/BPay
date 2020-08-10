package newflow;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bookeey.wallet.live.R;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.GenericActivity;
import coreframework.utils.LocaleHelper;

public class PayUWebPageNewFlow extends GenericActivity {
    private TextView title;
    ProgressBar progressBar;
    private ProgressDialog progress;
    @SuppressLint("SetJavaScriptEnabled") @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payuwebpagescrn_newflow);


        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0,0,0);
        mTitleTextView.setText("LOAD MONEY");*/

        String selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(PayUWebPageNewFlow.this, selectedLanguage);
        }
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
        mTitleTextView.setText(getResources().getString(R.string.knet_gateway));
        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView imageView= (ImageView)findViewById(R.id.payuweb_image);
        String image= CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if(image.length()!=0) {
            imageView.setImageBitmap(stringToBitmap(image));
        }
        updateProfile(R.id.payuweb_nameTextooredo, R.id.payuweb_wallet_id, R.id.payuweb_balance_id);


        String url= getIntent().getStringExtra("temp_response");
        WebView mWebView = (WebView) findViewById(R.id.payuwebpage);
        progressBar = (ProgressBar) findViewById(R.id.pbar);
        progressBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.knet));
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setBackgroundDrawable(getResources().getDrawable(R.drawable.knet));
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewSampleClient());
        mWebView.loadUrl(url);
        progress = new ProgressDialog(this);
        progress.setCancelable(true);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private class WebViewSampleClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            final String _url = url;

            if (_url.contains("success")) {
                ((LinearLayout)findViewById(R.id.close_button_liear_layout)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.payuwebclose)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(getBaseContext(), MainActivityNewFlow.class);
                        startActivity(intent);
                        PayUWebPageNewFlow.this.finish();
                    }
                });
            }else if(_url.contains("failure")){
                ((LinearLayout)findViewById(R.id.close_button_liear_layout)).setVisibility(View.VISIBLE);
                ((Button)findViewById(R.id.payuwebclose)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(getBaseContext(),MainActivityNewFlow.class);
                        startActivity(intent);
                        PayUWebPageNewFlow.this.finish();
                    }
                });
            }

            if (progress.isShowing()) {
                progress.dismiss();
            }

        }
        private void hideIfVisible() {
            if (progress.isShowing()) {
                progress.hide();
            }
        }
        private void showIfNotVisible(String title, String message) {
            if (!progress.isShowing()) {
                progress.setTitle(title);
                progress.setMessage(message);
                progress.show();
                progress.isShowing();
            } else {
                progress.setTitle(title);
                progress.setMessage(message);
                progress.show();
                progress.isShowing();
            }
        }
    }
    private Bitmap stringToBitmap(String encodedString){
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (NullPointerException e) {
            e.getMessage();
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent= new Intent(getBaseContext(), MainActivity.class );
        startActivity(intent);*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}