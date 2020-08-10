package wheretopaynew;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookeey.wallet.live.Merchant_Map_Activity;
import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.forceface.TransTypeInterface;
import com.bookeey.wallet.live.login.LoginActivity;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.bookeey.wallet.live.wheretopay.MerchantMapsActivity;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.List;

import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericListActivity;
import coreframework.utils.URLUTF8Encoder;
import nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import nostra13.universalimageloader.core.DisplayImageOptions;
import nostra13.universalimageloader.core.ImageLoader;
import nostra13.universalimageloader.core.ImageLoaderConfiguration;
import nostra13.universalimageloader.core.assist.FailReason;
import nostra13.universalimageloader.core.assist.QueueProcessingType;
import nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
import ycash.wallet.json.pojo.wheretopay.MerchantCategoryDetailsListPojo;
import ycash.wallet.json.pojo.wheretopay.MerchantListResponse;
import ycash.wallet.json.pojo.wheretopay.MerchnatListRequest;

public class MerchantListCatogorieyActivityNewUI extends GenericListActivity {
    ListView merchantdetails_list_name;
    MerchantCategoryDetailsListPojo merchantCategoryDetailsListPojo;
    MerchantListResponse merchantListResponse;
    private CustomListAdapter adapter = null;
    ViewHolder holder;
    private DisplayImageOptions options;
    ImageView mapbutton, merchant_category_screen_wallet_logo_back, home_up_back;
    String categoryId, categoryName;
    ProgressDialog progress = null;

    private FirebaseAnalytics firebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchants_list_by_category);

        // Obtain the Firebase Analytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);


        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        getActionBar().setLogo(R.drawable.bookeey_latest_icon);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.specialactionbar, null);
        mActionBar.setDisplayShowCustomEnabled(true);

        categoryId = ((CoreApplication) getApplication()).getCategoryId();
        categoryName = ((CoreApplication) getApplication()).getCategoryname();
        ActionBar.LayoutParams params = new
                ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mActionBar.setCustomView(mCustomView, params);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.merchant_category_screen_title_text);
        mTitleTextView.setText(categoryName);
        progress = new ProgressDialog(this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        merchantListResponse = ((CoreApplication) getApplication()).getMerchantListResponse_1();
        adapter = new CustomListAdapter(this, merchantListResponse.getMerchantDetails());
        setListAdapter(adapter);

        merchant_category_screen_wallet_logo_back = (ImageView) mCustomView.findViewById(R.id.merchant_category_screen_wallet_logo_back);
        merchant_category_screen_wallet_logo_back.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mapbutton = (ImageView) mCustomView.findViewById(R.id.merchant_category_screen_image_logo);
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
                MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
                merchnatListRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
                merchnatListRequest.setG_transType(TransType.WHERE_TO_PAY_MAPVIEW_REQUEST.name());
                merchnatListRequest.setCategoryID(categoryId);
                String json = new Gson().toJson(merchnatListRequest);
                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(json));
                android.os.Handler messageHandler = new android.os.Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        hideifVisible();
                        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                            try {
                                GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                                if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.WHERE_TO_PAY_MAPVIEW_RESPONSE.name())) {
                                    merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);
                                    ((CoreApplication) getApplication()).setMerchantListResponse(merchantListResponse);
                                    ((CoreApplication) getApplication()).setImagePath(merchantCategoryDetailsListPojo.getPath());
                                    if (merchantListResponse.getMerchantDetails().size() == 0) {
                                        Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(getBaseContext(), MerchantMapsActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } catch (Exception e) {
                                Log.e("Error", "" + e);
                                return;
                            }
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                };
                new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
                showIfnotVisible("");
            }
        });


        createHorizontalList();
    }




    public void createHorizontalList(){


        CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getApplicationContext()).getCustomerLoginRequestReponse();
        MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
        merchnatListRequest.setG_transType(TransType.WHERE_TO_PAY_REQUEST.name());
        merchnatListRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
        String json = new Gson().toJson(merchnatListRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.WHERE_TO_PAY_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    try {
                        GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                        if (null != response && response.getG_status() == 1) {
                            merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);

                            String listItems[] = new String[merchantListResponse.getCategoryDetails().size()];

                            for (int i = 0; i < merchantListResponse.getCategoryDetails().size(); i++) {
                                listItems[i] = merchantListResponse.getCategoryDetails().get(i).getCategoryName();
                            }

                            HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.mscroll_view);

                            LinearLayout topLinearLayout = new LinearLayout(MerchantListCatogorieyActivityNewUI.this);
                            // topLinearLayout.setLayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.FILL_PARENT);
                            topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);



                                for (int i = 0; i < merchantListResponse.getCategoryDetails().size(); i++) {

                                    listItems[i] = merchantListResponse.getCategoryDetails().get(i).getCategoryName();


                                    final ImageView imageView = new ImageView(MerchantListCatogorieyActivityNewUI.this);
                                    final TextView textView = new TextView(MerchantListCatogorieyActivityNewUI.this);


                                    final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.custom_category_view);

                                    String categoryCode = merchantListResponse.getCategoryDetails().get(i).getCategoryCode();
                                    String categoryName =  merchantListResponse.getCategoryDetails().get(i).getCategoryName();



                                    imageView.setTag(categoryCode+"-"+categoryName);


                                    if (categoryName.equals("All")) {
                                        imageView.setImageResource(R.drawable.all);
                                        textView.setText(categoryName);

                                    } else if (categoryName.equals("Food & Beverage")) {
                                        imageView.setImageResource(R.drawable.foodandbeverage);
                                        textView.setText(categoryName);

                                    } else if (categoryName.equals("Fashion")) {
                                        imageView.setImageResource(R.drawable.fashion);
                                        textView.setText(categoryName);



                                    } else if (categoryName.equals("Retail")) {
                                        imageView.setImageResource(R.drawable.retail);
                                        textView.setText(categoryName);


                                    } else if (categoryName.equals("Medical Care")) {
                                        imageView.setImageResource(R.drawable.medical_care);
                                        textView.setText(categoryName);


                                    } else if (categoryName.equals("Health & Beauty")) {
                                        imageView.setImageResource(R.drawable.healthbeauty);



                                    } else if (categoryName.equals("Hotels & Accommodations")) {
                                        imageView.setImageResource(R.drawable.hotelsandaccomodation);
                                        textView.setText(categoryName);


                                    } else if (categoryName.equals("Transportation")) {
                                        imageView.setImageResource(R.drawable.transportation);
                                        textView.setText(categoryName);


                                    } else if (categoryName.equals("Groceries & Hypermarkets")) {
                                        imageView.setImageResource(R.drawable.groceries);
                                        textView.setText(categoryName);


                                    } else if (categoryName.equals("Electronics & Computers")) {
                                        imageView.setImageResource(R.drawable.electronics);
                                        textView.setText(categoryName);


                                    } else if (categoryName.equals("Games & Toys")) {
                                        imageView.setImageResource(R.drawable.toys);
                                        textView.setText(categoryName);


                                    } else if (categoryName.equals("Services")) {
                                        imageView.setImageResource(R.drawable.services);
                                        textView.setText(categoryName);


                                    } else if (categoryName.equals("Automotive")) {
                                        imageView.setImageResource(R.drawable.automotive);
                                        textView.setText(categoryName);


                                    }

                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                                    layoutParams.setMargins(25,25,25,25);
                                    imageView.setLayoutParams(layoutParams);





                                    topLinearLayout.addView(imageView);
                                    topLinearLayout.addView(textView);

                                    imageView.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewUI.this, ""+imageView.getTag(), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                                            toast.show();
                                            refreshMerchantsForACategory("" + imageView.getTag());
                                        }
                                    });


                            }

                            scrollView.addView(topLinearLayout);

                        } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewUI.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(MerchantListCatogorieyActivityNewUI.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewUI.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();

                            //  Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } catch (Exception e) {
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(MerchantListCatogorieyActivityNewUI.this, getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(MerchantListCatogorieyActivityNewUI.this,getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
        showIfNotVisible("");


        refreshMerchantsForACategory("0-All");
    }


    public void refreshMerchantsForACategory(String _categoryCodeAndCategoryName){

        String[] categoryCodeAndCategoryName = _categoryCodeAndCategoryName.split("-");

        final String categoryCode =  categoryCodeAndCategoryName[0];
        final String categoryname =  categoryCodeAndCategoryName[1];

        CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication)getApplicationContext()).getCustomerLoginRequestReponse();
        final MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
        final MerchantListResponse merchantListResponse = (((CoreApplication)getApplicationContext()).getMerchantListResponse_category());
        merchnatListRequest.setG_transType(TransType.WHERE_TO_PAY_MERCHANTLIST_REQUEST.name());
        merchnatListRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
        merchnatListRequest.setCategoryID(categoryCode);
        String json = new Gson().toJson(merchnatListRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.WHERE_TO_PAY_MERCHANTLIST_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideIfVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {

//                    Toast toast1 = Toast.makeText(MerchantListCatogorieyActivityNewUI.this, (String) msg.obj, Toast.LENGTH_LONG);
//                    toast1.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                    toast1.show();

                    GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                    if (null != response && response.getG_status() == 1) {
                         MerchantListResponse merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);
                        if (merchantListResponse.getCategoryDetails().size() == 0) {
                            ((CoreApplication) getApplication()).setMerchantListResponse_1(merchantListResponse);
                        }
                        ((CoreApplication) getApplicationContext()).setCategoryname(categoryname);
                        ((CoreApplication) getApplicationContext()).setCategoryId(merchnatListRequest.getCategoryID());
                        if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewUI.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(MerchantListCatogorieyActivityNewUI.this, LoginActivity.class);
                            startActivity(intent);
                           finish();
                        }


                            adapter.clear();
                            adapter = new CustomListAdapter(MerchantListCatogorieyActivityNewUI.this, merchantListResponse.getMerchantDetails());
                            setListAdapter(adapter);


                    } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewUI.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(MerchantListCatogorieyActivityNewUI.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        /*Toast.makeText(getActivity(), "No merchnats available", Toast.LENGTH_SHORT).show();
                        return;*/
                        Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewUI.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                    finish();
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(MerchantListCatogorieyActivityNewUI.this, getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(MerchantListCatogorieyActivityNewUI.this, getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
        showIfNotVisible("");

    }


    private void hideIfVisible() {
        if (progress.isShowing()) {
            progress.hide();
        }
    }

    public void reset() {
        adapter.equals(null);
        adapter.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();

        //Facebook
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Page listing merchants under a specific category");

        //Firebase
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, 20);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Page listing merchants under a specific category");
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        Log.e("Firebase ", " Event 20 logged");
    }

    private void showIfnotVisible(String title) {
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

    private void hideifVisible() {
        if (progress.isShowing()) {
            progress.hide();
        }
    }


    @Override
    protected void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);

        categoryId = ((CoreApplication) getApplication()).getCategoryId();
        categoryName = ((CoreApplication) getApplication()).getCategoryname();

        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
        final MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
        merchantListResponse = ((CoreApplication) getApplication()).getMerchantListResponse_1();
        merchnatListRequest.setCategoryID(categoryId);
        merchnatListRequest.setMerchantName(merchantListResponse.getMerchantDetails().get(position).getMerchantName());
        merchnatListRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
        merchnatListRequest.setG_transType(TransType.WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.name());
        String json = new Gson().toJson(merchnatListRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.getURL());
        buffer.append("?d=" + URLUTF8Encoder.encode(json));
        android.os.Handler messageHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                hideifVisible();
                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                    try {
                        GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                        if (null != response && response.getG_status() == 1) {
                            merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);
                            if (merchantListResponse.getMerchantDetails().size() == 0) {
                                Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                ((CoreApplication) getApplication()).setMerchantName(merchantListResponse.getMerchantDetails().get(0).getMerchantName());
                                ((CoreApplication) getApplication()).setMerchantListResponse(merchantListResponse);
                                ((CoreApplication) getApplication()).setImagePath(merchantListResponse.getMerchantDetails().get(0).getPath());
                                Intent intent = new Intent(getBaseContext(), Merchant_Map_Activity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
        showIfnotVisible("");
    }

    class CustomListAdapter extends ArrayAdapter<MerchantCategoryDetailsListPojo> {
        public CustomListAdapter(Activity context, List<MerchantCategoryDetailsListPojo> genericResponses) {
            super(context, R.layout.merchnat_list_row, genericResponses);
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.new_logo_grey)
                    .showImageForEmptyUri(R.drawable.new_logo_grey)
                    .showImageOnFail(R.drawable.new_logo_grey)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            initImageLoader(getBaseContext());
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            merchantCategoryDetailsListPojo = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.merchnat_list_row, parent, false);
                holder = new ViewHolder();
                holder.text_name = (TextView) convertView.findViewById(R.id.merchant_title_name);
                // holder.merchant_contact_img = (ImageView) convertView.findViewById(R.id.merchant_contact_img);
                holder.icon = (ImageView) convertView.findViewById(R.id.picture_icon);
               /* holder.merchant_contact_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("android.intent.action.DIAL");
                        intent.setData(Uri.parse("tel:" + merchantListResponse.getMerchantDetails().get(position).getMobileNumber()));
                        startActivity(intent);
                    }
                });*/
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance()
                    .displayImage(merchantCategoryDetailsListPojo.getPath(), holder.icon, options, new SimpleImageLoadingListener() {
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
            holder.text_name.setText(merchantCategoryDetailsListPojo.getMerchantName());
            return convertView;
        }
    }

    public class ViewHolder {
        TextView text_name;
        TextView address;
        ImageView icon, merchant_contact_img;
        TransTypeInterface transTypeInterface;

        public TransTypeInterface getTransTypeInterface() {
            return transTypeInterface;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}