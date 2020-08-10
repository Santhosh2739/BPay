package newflow;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import coreframework.database.CustomSharedPreferences;
import coreframework.taskframework.GenericActivity;
import coreframework.taskframework.ProgressDialogFrag;
import coreframework.taskframework.YPCHeadlessCallback;
import newflow_processing.MobileBill_Denomiantions_ProcessingNewFlow;
import nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import nostra13.universalimageloader.core.DisplayImageOptions;
import nostra13.universalimageloader.core.ImageLoader;
import nostra13.universalimageloader.core.ImageLoaderConfiguration;
import nostra13.universalimageloader.core.assist.FailReason;
import nostra13.universalimageloader.core.assist.QueueProcessingType;
import nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticL1RequestPojo;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticRecharge;
import ycash.wallet.json.pojo.mobilebilloperator.DomesticRechargeResponse;

/**
 * Created by 10037 on 6/14/2017.
 */

public class MobileBill_List_ActivityNewFlow extends GenericActivity implements YPCHeadlessCallback {
    DomesticRechargeResponse domesticRechargeResponse;
    MobileAdapter adapter;
    ListView list;
    List<DomesticRecharge> details;
    DomesticRecharge cardDetails;
    private DisplayImageOptions options;
    PrepaidCardViewHolder holder;
    String response_str=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobilebill_list_activity_newflow);
        list = (ListView) findViewById(R.id.list);

        response_str = getIntent().getStringExtra("BILL_RESPONSE");
        CustomSharedPreferences.saveStringData(MobileBill_List_ActivityNewFlow.this,response_str, CustomSharedPreferences.SP_KEY.MOBILEBILL_RESPONSE);

        ImageView image_person = (ImageView) findViewById(R.id.sendmoney_image_id_1);
        String image = CustomSharedPreferences.getStringData(getBaseContext(), CustomSharedPreferences.SP_KEY.IMAGE);
        if (image.length() != 0) {
            image_person.setImageBitmap(stringToBitmap(image));
        }
        updateProfile(R.id.mobilebill_nameTextooredo, R.id.mobilebill_wallet_id, R.id.mobilebill_balance_id);

        domesticRechargeResponse = new Gson().fromJson(response_str, DomesticRechargeResponse.class);

        /*View mCustomView = getActionBar().getCustomView();
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        LinearLayout.LayoutParams txvPars = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        txvPars.gravity = Gravity.CENTER_HORIZONTAL;
        txvPars.width = metrics.widthPixels;
        mTitleTextView.setLayoutParams(txvPars);
        mTitleTextView.setGravity(Gravity.CENTER);
        mTitleTextView.setPadding(-80, 0, 0, 0);
        mTitleTextView.setText("MOBILE BILL");*/

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
        mTitleTextView.setText(getResources().getString(R.string.mobile_bill_title));

        ImageView home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        details = new ArrayList<DomesticRecharge>(domesticRechargeResponse.getOperatorList());
        adapter = new MobileAdapter(MobileBill_List_ActivityNewFlow.this, details);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                ((CoreApplication) getApplication()).setOperatorImage(details.get(position).getImagename());
                ((CoreApplication) getApplication()).setOperatorName(details.get(position).getOperatorName());
                ((CoreApplication) getApplication()).setDenominationsAmount(details.get(position).getDenomCollection());
                ((CoreApplication) getApplication()).setOperatorType(details.get(position).getOperatorType());
                denominations_Request(details.get(position).getOperatorName(),details.get(position).getOperatorType());
               /* Intent intent = new Intent(getBaseContext(), MobileBill_L1_Activity.class);
                startActivity(intent);*/

            }
        });

    }

    private void denominations_Request(String operatorName, String operatorType) {
        CoreApplication application = (CoreApplication) getApplication();
        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) application).getCustomerLoginRequestReponse();
        DomesticL1RequestPojo domesticL1RequestPojo=new DomesticL1RequestPojo();
        domesticL1RequestPojo.setOperatorName(operatorName);
        domesticL1RequestPojo.setOperatorType(operatorType);
        String uiProcessorReference = application.addUserInterfaceProcessor(new MobileBill_Denomiantions_ProcessingNewFlow(domesticL1RequestPojo, application, true));
        ProgressDialogFrag progress = new ProgressDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", uiProcessorReference);
        progress.setCancelable(true);
        progress.setArguments(bundle);
        progress.show(getSupportFragmentManager(), "progress_dialog");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }


    private class MobileAdapter extends ArrayAdapter<DomesticRecharge> {
        public MobileAdapter(MobileBill_List_ActivityNewFlow context, List<DomesticRecharge> cc) {
            super(context, R.layout.virtual_card_row, cc);
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.no_image)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            initImageLoader(getBaseContext());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            cardDetails = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.virtual_card_row, parent, false);
                holder = new PrepaidCardViewHolder();
                holder.virtual_prepaid_img = (ImageView) convertView.findViewById(R.id.prepaid_virtual_card_image);
                convertView.setTag(holder);
            } else {
                holder = (PrepaidCardViewHolder) convertView.getTag();
            }

            ImageLoader.getInstance()
                    .displayImage(cardDetails.getImagename(), holder.virtual_prepaid_img, options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        }
                    });
            return convertView;
        }


    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }*/

    public class PrepaidCardViewHolder {
        ImageView virtual_prepaid_img;

    }

    public void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }


}
