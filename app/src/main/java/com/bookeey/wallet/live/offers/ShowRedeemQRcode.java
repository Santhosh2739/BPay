package com.bookeey.wallet.live.offers;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import coreframework.barcodeclient.QRBitmap_Generator;
import coreframework.taskframework.GenericActivity;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.offers.OfferPreviewResponse;
/**
 * Created by 30099 on 4/26/2016.
 */
public class ShowRedeemQRcode extends GenericActivity {
    ImageView redeem_qrcode_image;
    TextView redeem_qrcode_text;
    OfferPreviewResponse offerPreviewResponse= null;
    Button show_redeem_close_btn;
    String offerid= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_redeem_qrcode);

        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("REDEMPTION");*/

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
        mTitleTextView.setText(getResources().getString(R.string.offers_qrcode_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        redeem_qrcode_image     = (ImageView) findViewById(R.id.redeem_qrcode_image);
        redeem_qrcode_text      = (TextView) findViewById(R.id.redeem_qrcode_text);
        offerPreviewResponse= ((CoreApplication)getApplication()).getMyoOfferPreviewResponse();
        redeem_qrcode_text.setText(getResources().getString(R.string.offers_qrcode_scanned_at)+"\n"+((CoreApplication)getApplication()).getOffer_merchantName()+getResources().getString(R.string.offers_qrcode_for_redemption));
        CustomerLoginRequestReponse customerLoginRequestReponse= ((CoreApplication)getApplication()).getCustomerLoginRequestReponse();
        offerid= ((CoreApplication)getApplication()).getOfferId();
        String customerId= customerLoginRequestReponse.getUniqueCustomerId();
        String barcode_data_redeem= null;
        barcode_data_redeem = customerLoginRequestReponse.getOauth_2_0_client_token()+"-"+offerid+"-"+customerId;
        redeem_qrcode_image.setImageBitmap(QRBitmap_Generator.getQrBitmap(barcode_data_redeem, getBaseContext()));
        show_redeem_close_btn     = (Button) findViewById(R.id.show_redeem_close_btn);
        show_redeem_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getBaseContext(), NewOffersActivity.class);
                startActivity(intent);
                ShowRedeemQRcode.this.finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(getBaseContext(), NewOffersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}