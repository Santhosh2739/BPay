package com.bookeey.wallet.live;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import coreframework.taskframework.GenericActivity;
/**
 * Created by 30099 on 1/28/2016.
 */
public class LoadMoneyProgressActivity extends GenericActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadmoney_success_new);
        View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0,0,0);
        mTitleTextView.setText("Load Money Receipt");
        ((TextView)findViewById(R.id.loadmoney_success_paymentID_value_txt)).setText("");
        ((TextView)findViewById(R.id.loadmoney_success_track_id_value_txt)).setText("");
        ((TextView)findViewById(R.id.loadmoney_success_transaction_id_value_txt)).setText("");
        ((TextView)findViewById(R.id.loadmoney_success_auth_value_txt)).setText("");
        ((TextView)findViewById(R.id.loadmoney_success_post_date_value_txt)).setText("");
        ((TextView)findViewById(R.id.loadmoney_success_result_code_value_txt)).setText("");
        ((TextView)findViewById(R.id.loadmoney_success_ref_no_value_txt)).setText("");
        if(((TextView)findViewById(R.id.loadmoney_success_result_code_value_txt)).getText().toString().trim().equals("CAPTURED")){
            ((ImageView)findViewById(R.id.loadmoney_success_status_img)).setImageDrawable(getResources().getDrawable(R.drawable.tickk));
            ((TextView)findViewById(R.id.loadmoney_success_status_value_txt)).setText("Success");
        }else{
            ((ImageView)findViewById(R.id.loadmoney_success_status_img)).setImageDrawable(getResources().getDrawable(R.drawable.mer_exc));
            ((TextView)findViewById(R.id.loadmoney_success_status_value_txt)).setText("Failure");
        }
    }
}