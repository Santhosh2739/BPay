package newflow;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;

import java.util.Date;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.GenericActivity;
import coreframework.utils.PriceFormatter;
import coreframework.utils.TimeUtils;
import ycash.wallet.json.pojo.Internationaltopup.InternationalRechargeInitiationResponsePojo;

/**
 * Created by 30099 on 4/29/2016.
 */
public class TopupL1ScreenActvityNewFlow extends GenericActivity {
    InternationalRechargeInitiationResponsePojo internationalRechargeInitiationResponsePojo = null;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topup_leg1_layout_newflow);
        progress = new ProgressDialog(TopupL1ScreenActvityNewFlow.this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("Int'l Top Up");*/

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
        mTitleTextView.setText(getResources().getString(R.string.intl_top_up_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.topup_l1_image_person);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            imageView.setImageBitmap(stringToBitmap(image));
        }
        updateProfile(R.id.topup_l1_nameTextooredo, R.id.topup_l1_wallet_id, R.id.topup_l1_balance_id);
        internationalRechargeInitiationResponsePojo = ((CoreApplication) getApplication()).getInternationalRechargeInitiationResponsePojo();
        ((TextView) findViewById(R.id.topup_leg1_value_right0_tv)).setText(internationalRechargeInitiationResponsePojo.getRecipientMobile());
        if (internationalRechargeInitiationResponsePojo.getDenominationAmt() != 0) {
            ((TextView) findViewById(R.id.topup_leg1_value_right1_tv)).setText(internationalRechargeInitiationResponsePojo.getCurrencyName() + " " + PriceFormatter.format(internationalRechargeInitiationResponsePojo.getDenominationAmt(), 2, 2));
        } else {
            ((TextView) findViewById(R.id.topup_leg1_value_right1_tv)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.topup_leg1_left1_tv)).setVisibility(View.GONE);
            ((View) findViewById(R.id.topup_leg1__horizantal1_view)).setVisibility(View.GONE);
            ((View) findViewById(R.id.topup_leg1_vertical1_view)).setVisibility(View.GONE);
        }

        if (internationalRechargeInitiationResponsePojo.getDenominationAmtinKWD() != 0) {
            ((TextView) findViewById(R.id.topup_leg1_value_right2_tv)).setText(PriceFormatter.format(internationalRechargeInitiationResponsePojo.getDenominationAmtinKWD(), 3, 3));
        } else {
            ((TextView) findViewById(R.id.topup_leg1_value_right2_tv)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.topup_leg1_left2_tv)).setVisibility(View.GONE);
            ((View) findViewById(R.id.topup_leg1__horizantal2_view)).setVisibility(View.GONE);
            ((View) findViewById(R.id.topup_leg1_vertical2_view)).setVisibility(View.GONE);
        }
        ((TextView) findViewById(R.id.topup_leg1_value_right3_tv)).setText(internationalRechargeInitiationResponsePojo.getCustomerID());
        ((TextView) findViewById(R.id.topup_leg1_value_right4_tv)).setText(internationalRechargeInitiationResponsePojo.getTransactionID());
        ((TextView) findViewById(R.id.topup_leg1_value_right5_tv)).setText(TimeUtils.getDisplayableDateWithSeconds(((CoreApplication) getApplication()).getCustomerLoginRequestReponse().getG_servertime(), new Date(internationalRechargeInitiationResponsePojo.getServerTime())));

        ((Button) findViewById(R.id.topup_leg1_confirm_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                /*
                CoreApplication application = (CoreApplication) getApplication();
                CustomerLoginRequestReponse customerLoginRequestReponse = application.getCustomerLoginRequestReponse();
                InternationalRechargeRequestPojo request = new InternationalRechargeRequestPojo();
                request.setG_transType(TransType.NEW_INTERNATIONAL_RECHARGE_L2_REQUEST.name());
                request.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
                request.setTxnId(internationalRechargeInitiationResponsePojo.getTransactionID());
                String json = new Gson().toJson(request);
                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.NEW_INTERNATIONAL_RECHARGE_L2_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(json));

                android.os.Handler messageHandler = new android.os.Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        hideIfVisible();
                        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                            GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                            if (null != response && response.getG_response_trans_type().equalsIgnoreCase(TransType.INTERNATIONAL_RECHARGE_L2_RESPONSE.name())) {
                                internationalRechargeInitiationResponsePojo = new Gson().fromJson((String) msg.obj, InternationalRechargeInitiationResponsePojo.class);
                                ((CoreApplication) getApplication()).setInternationalRechargeInitiationResponsePojo(internationalRechargeInitiationResponsePojo);
                                Intent intent = new Intent(getBaseContext(), TopupFinalScreenActivity.class);
                                startActivity(intent);
                            } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                                Toast toast = Toast.makeText(TopupL1ScreenActvityNewFlow.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                Intent intent = new Intent(TopupL1ScreenActvityNewFlow.this, LoginActivity.class);
                                startActivity(intent);
                                TopupL1ScreenActvityNewFlow.this.finish();
                                return;
                            } else if (null != response && response.getG_status() != 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.INTERNATIONAL_RECHARGE_L2_RESPONSE.name())) {
                                switch (response.getG_errorDescription()) {
                                    case "Problem occurs while validating. Please try again":
                                        Toast toast = Toast.makeText(TopupL1ScreenActvityNewFlow.this, getResources().getString(R.string.Problem_occurs_while_validating_Please_try_again), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        Intent intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                                        intent.putExtra("BACK", "back");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        break;
                                    case "Monthly Recharge Limits Exceeding":
                                        toast = Toast.makeText(TopupL1ScreenActvityNewFlow.this, getResources().getString(R.string.Monthly_Recharge_Limits_Exceeding), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                                        intent.putExtra("BACK", "back");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        break;
                                    case "Daily Recharge Limits Exceeding":
                                        toast = Toast.makeText(TopupL1ScreenActvityNewFlow.this, getResources().getString(R.string.Daily_Recharge_Limits_Exceeding), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                                        intent.putExtra("BACK", "back");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        break;
                                    case "Recharge Amount Min & Max":
                                        toast = Toast.makeText(TopupL1ScreenActvityNewFlow.this, getResources().getString(R.string.Recharge_Amount_Min_Max), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                                        intent.putExtra("BACK", "back");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        break;
                                    case "Daily & Monthly Limits Not Available":
                                        toast = Toast.makeText(TopupL1ScreenActvityNewFlow.this, getResources().getString(R.string.Daily_Monthly_Limits_Not_Available), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                                        intent.putExtra("BACK", "back");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        break;
                                    case "Number is InActive":
                                        toast = Toast.makeText(TopupL1ScreenActvityNewFlow.this, getResources().getString(R.string.Number_is_InActive), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                                        intent.putExtra("BACK","back") ;
                                        startActivity(intent);
                                        TopupL1ScreenActvityNewFlow.this.finish();
                                        break;
                                    case "Number not Registered":
                                        toast = Toast.makeText(TopupL1ScreenActvityNewFlow.this, getResources().getString(R.string.number_not_registered), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                                        intent.putExtra("BACK", "back");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        break;
                                    case "Data Entry Error!Please Check":
                                        toast = Toast.makeText(TopupL1ScreenActvityNewFlow.this, getResources().getString(R.string.Data_Entry_Error_Please_Check), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                                        intent.putExtra("BACK", "back");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        break;
                                    case "FAILED":
                                        toast = Toast.makeText(TopupL1ScreenActvityNewFlow.this, getResources().getString(R.string.FAILED), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                                        intent.putExtra("BACK", "back");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        break;
                                    default:
                                        toast = Toast.makeText(TopupL1ScreenActvityNewFlow.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                        toast.show();
                                        intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                                        intent.putExtra("BACK", "back");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        break;
                                }
                                return;
                            } else {
                                Toast toast = Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                toast.show();
                                Intent intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                                intent.putExtra("BACK", "back");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                return;
                            }
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                            intent.putExtra("BACK", "back");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TopupL1ScreenActvityNewFlow.this, TopUpInitialActivity.class);
                            intent.putExtra("BACK", "back");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            return;
                        }
                    }
                };
                new Thread(new ServerConnection(0, messageHandler, buffer.toString(), true)).start();
                showIfNotVisible("");


                */

            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private Bitmap stringToBitmap(String encodedString) {
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
}
