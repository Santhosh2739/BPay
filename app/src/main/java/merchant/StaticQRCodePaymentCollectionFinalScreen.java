package merchant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.google.gson.Gson;

import java.io.InputStream;
import java.util.Date;

import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.YPCHeadlessCallback;
import coreframework.utils.PriceFormatter;
import coreframework.utils.TimeUtils;
import ycash.wallet.json.pojo.paytomerchant.PayToMerchantCommitRequestResponse;

public class StaticQRCodePaymentCollectionFinalScreen extends GenericActivity implements YPCHeadlessCallback, View.OnClickListener {

    private String response = null;
    private PayToMerchantCommitRequestResponse response_obj = null;
    TableRow payment_confirm_total_amt_row, payment_confirm_discount_amt_row;
    TextView payment_confirm_total_amt_value, payment_confirm_discount_amt_value;
    Button payment_confirm_print_btn;

    private boolean bloop = false;
    private boolean bthreadrunning = false;

    //For Bus


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_confirmation);

        ImageView ypcm_close = (ImageView) findViewById(R.id.payment_success_img_id);
        ypcm_close.setOnClickListener(this);

        TextView payment_confirm_status = (TextView) findViewById(R.id.payment_confirm_status);
        TextView payment_confirm_txn_id = (TextView) findViewById(R.id.payment_confirm_txn_id);
        TextView payment_confirm_date_id = (TextView) findViewById(R.id.payment_confirm_date_id);
        TextView payment_confirm_total_payment_id = (TextView) findViewById(R.id.payment_confirm_total_payment_id);
        TextView payment_confirm_wallet_number_id = (TextView) findViewById(R.id.payment_confirm_wallet_number_id);

        //offres
        payment_confirm_total_amt_row = (TableRow) findViewById(R.id.payment_confirm_total_amt_row);
        payment_confirm_discount_amt_row = (TableRow) findViewById(R.id.payment_confirm_discount_amt_row);
        payment_confirm_total_amt_value = (TextView) findViewById(R.id.payment_confirm_total_amt_value);
        payment_confirm_discount_amt_value = (TextView) findViewById(R.id.payment_confirm_discount_amt_value);


        //print
        payment_confirm_print_btn = (Button) findViewById(R.id.payment_confirm_print_btn);



        //This is the place where the response from leg1 is received as intent data
        //start
        response = getIntent().getExtras().getString("response");
        response_obj = new Gson().fromJson(response, PayToMerchantCommitRequestResponse.class);
        //end




        if(response_obj.getG_status_description().contains("success")){

            payment_confirm_status.setText("" + getString(R.string.transaction_success));



        }else{
            payment_confirm_status.setText("" + response_obj.getG_status_description());
        }

        TextView payment_confirm_wallet_number_header_id = (TextView) findViewById(R.id.payment_confirm_wallet_number_header_id);
        TextView payment_confirm_txn_header_id = (TextView) findViewById(R.id.payment_confirm_txn_header_id);

        if (response_obj.getTransactionId() == null || response_obj.getTransactionId().length() == 0) {
            payment_confirm_txn_header_id.setVisibility(View.GONE);
        }
        if (response_obj.getCustomerMobileNumber() == null || response_obj.getCustomerMobileNumber().length() == 0) {
            payment_confirm_wallet_number_header_id.setVisibility(View.GONE);
        }

        if (response_obj.getTotalPrice() != 0) {
            payment_confirm_total_amt_row.setVisibility(View.VISIBLE);
            payment_confirm_total_amt_value.setText("" + PriceFormatter.format(response_obj.getTotalPrice(), 3, 3) + " KWD");
        }
        if (response_obj.getDiscountPrice() != 0) {
            payment_confirm_discount_amt_row.setVisibility(View.VISIBLE);
            payment_confirm_discount_amt_value.setText("" + PriceFormatter.format(response_obj.getDiscountPrice(), 3, 3) + " KWD");
        }
        payment_confirm_txn_id.setText("" + response_obj.getTransactionId());
        payment_confirm_wallet_number_id.setText("" + response_obj.getCustomerMobileNumber());


        String time_zone_str = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime();


        payment_confirm_date_id.setText("" + TimeUtils.getDisplayableDateWithSeconds(time_zone_str, new Date(response_obj.getServerTime())));



        switch (response_obj.getG_status()) {
            case 1:
                ypcm_close.setBackgroundResource(R.drawable.success);
                break;
            case -1:
                ypcm_close.setBackgroundResource(R.drawable.red_cross);
                break;
        }

        payment_confirm_total_payment_id.setText(getString(R.string.total_paymnet) +"   KWD " + PriceFormatter.format(response_obj.getTxnAmount(), 3, 3));










    }


    @Override
    public void onResume() {
        super.onResume();



//        final ImageView back_logo = (ImageView) findViewById(R.id.back_logo);
//        back_logo.setImageBitmap(((CoreApplication) getApplication()).getMerchnat_logo());



    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            pd.show();
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public void onProgressUpdate(int progress) {

    }

    @Override
    public void onProgressComplete() {

    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("response", response);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if (null != savedInstanceState) {
            response = savedInstanceState.getString("response");
            response_obj = new Gson().fromJson(response, PayToMerchantCommitRequestResponse.class);
        }
    }




    private  String maskString(String strText, int start, int end, char maskChar)
    {

        if(strText == null || strText.equals(""))
            return "";

        if(start < 0)
            start = 0;

        if( end > strText.length() )
            end = strText.length();


        int maskLength = end - start;

        if(maskLength == 0)
            return strText;

        StringBuilder sbMaskString = new StringBuilder(maskLength);

        for(int i = 0; i < maskLength; i++){
            sbMaskString.append(maskChar);
        }

        return strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(start + maskLength);
    }
}
