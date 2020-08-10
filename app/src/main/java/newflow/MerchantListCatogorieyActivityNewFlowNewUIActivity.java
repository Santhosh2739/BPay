package newflow;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.util.ArrayList;
import java.util.List;

import coreframework.database.CustomSharedPreferences;
import coreframework.network.ServerConnection;
import coreframework.taskframework.GenericActivity;
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

public class MerchantListCatogorieyActivityNewFlowNewUIActivity extends GenericActivity {
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
    private ListView list;

    EditText etSearch = null;
    TextView mTitleTextView = null;


    private TextView tv_a_d,tv_e_h , tv_i_l, tv_m_p , tv_q_t , tv_u_x ,tv_y_z, tv_0_9 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchants_list_by_category_activity_range);



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
        mTitleTextView = (TextView) mCustomView.findViewById(R.id.merchant_category_screen_title_text);
//        mTitleTextView.setText(categoryName);

        ImageView merchant_category_screen_wallet_logo_back = (ImageView) mCustomView.findViewById(R.id.merchant_category_screen_wallet_logo_back);
        merchant_category_screen_wallet_logo_back.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progress = new ProgressDialog(this, R.style.MyTheme2);
        progress.setCanceledOnTouchOutside(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        merchantListResponse = ((CoreApplication) getApplication()).getMerchantListResponse_1();
        adapter = new CustomListAdapter(this, merchantListResponse.getMerchantDetails());
//        setListAdapter(adapter);

        list = (ListView) findViewById(R.id.listView);

        list.setAdapter(adapter);



        // Add Text Change Listener to EditText
        etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Call back the Adapter with current character to Filter
                adapter.getFilter().filter(s.toString());



//                                textlength = etsearch.getText().length();
//                                array_sort.clear();
//                                for (int i = 0; i < movieNamesArrayList.size(); i++) {
//                                    if (textlength <= movieNamesArrayList.get(i).getMovieName().length()) {
//                                        Log.d("ertyyy",movieNamesArrayList.get(i).getMovieName().toLowerCase().trim());
//                                        if (movieNamesArrayList.get(i).getMovieName().toLowerCase().trim().contains(
//                                                etsearch.getText().toString().toLowerCase().trim())) {
//                                            array_sort.add(movieNamesArrayList.get(i));
//                                        }
//                                    }
//                                }
//
//                                adapter = new ListViewAdapter(MainActivity.this, array_sort);
//                                list.setAdapter(adapter);


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

//                adapter.getFilter().filter(s.toString());

            }
        });

        merchant_category_screen_wallet_logo_back = (ImageView) mCustomView.findViewById(R.id.merchant_category_screen_wallet_logo_back);
        merchant_category_screen_wallet_logo_back.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                finish();
            }
        });
        home_up_back = (ImageView) mCustomView.findViewById(R.id.home_up_back);
        home_up_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        mapbutton = (ImageView) mCustomView.findViewById(R.id.merchant_category_screen_image_logo);

//        mapbutton.setVisibility(View.GONE);

        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
//                CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
//                MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
////                merchnatListRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
//                merchnatListRequest.setG_transType(TransType.NEW_WHERE_TO_PAY_MAPVIEW_REQUEST.name());
//                merchnatListRequest.setCategoryID("0");
//                String json = new Gson().toJson(merchnatListRequest);
//                StringBuffer buffer = new StringBuffer();
//                buffer.append(TransType.NEW_WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.getURL());
//                buffer.append("?d=" + URLUTF8Encoder.encode(json));
//                android.os.Handler messageHandler = new android.os.Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        hideifVisible();
//                        if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
//                            try {
//                                GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
//                                if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.WHERE_TO_PAY_MAPVIEW_RESPONSE.name())) {
//                                    merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);
//                                    ((CoreApplication) getApplication()).setMerchantListResponse(merchantListResponse);
//                                    ((CoreApplication) getApplication()).setImagePath(merchantCategoryDetailsListPojo.getPath());
//                                    if (merchantListResponse.getMerchantDetails().size() == 0) {
//                                        Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Intent intent = new Intent(getBaseContext(), MerchantMapsActivity.class);
//                                        startActivity(intent);
//                                    }
//                                } else {
//                                    Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                            } catch (Exception e) {
//                                Log.e("Error", "" + e);
//                                return;
//                            }
//                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
//                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
//                        } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
//                            Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    }
//                };
//                new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
//                showIfnotVisible("");
//            }
//







            CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
            MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
                merchnatListRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
                merchnatListRequest.setG_transType(TransType.NEW_WHERE_TO_PAY_MAPVIEW_REQUEST.name());
                merchnatListRequest.setCategoryID(categoryId);
            String json = new Gson().toJson(merchnatListRequest);
            StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.NEW_WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.getURL());
                buffer.append("?d=" + URLUTF8Encoder.encode(json));
            android.os.Handler messageHandler = new android.os.Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    hideifVisible();
                    if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
                        try {
                            GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
                            if (null != response && response.getG_status() == 1 && response.getG_response_trans_type().equalsIgnoreCase(TransType.NEW_WHERE_TO_PAY_MAPVIEW_RESPONSE.name())) {
                                merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);
                                ((CoreApplication) getApplication()).setMerchantListResponse(merchantListResponse);
//                                ((CoreApplication) getApplication()).setImagePath(merchantCategoryDetailsListPojo.getPath());
                                if (merchantListResponse.getMerchantDetails().size() == 0) {
                                    Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                                } else {

//                                    Toast.makeText(getBaseContext(), "-> " +merchantListResponse.getMerchantDetails().size(), Toast.LENGTH_SHORT).show();

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
                new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
            showIfnotVisible("");
        }






        });





       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {





                categoryId = ((CoreApplication) getApplication()).getCategoryId();
                categoryName = ((CoreApplication) getApplication()).getCategoryname();



                CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();


                final MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
                merchantListResponse = ((CoreApplication) getApplication()).getMerchantListResponse_1();

                MerchantCategoryDetailsListPojo merchantCategoryDetailsListPojo =  mDisplayedValuesForListClick.get(position);


                //Old
//                merchnatListRequest.setCategoryID(categoryId);
//                merchnatListRequest.setMerchantName(merchantListResponse.getMerchantDetails().get(position).getMerchantName());


                //New
                merchnatListRequest.setCategoryID(merchantCategoryDetailsListPojo.getCategory());
                merchnatListRequest.setMerchantName(merchantCategoryDetailsListPojo.getMerchantName());

//                Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this," "+merchantCategoryDetailsListPojo.getCategory()+" "+merchantCategoryDetailsListPojo.getMerchantName(),Toast.LENGTH_LONG).show();



                merchnatListRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
                merchnatListRequest.setG_transType(TransType.NEW_WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.name());
                String json = new Gson().toJson(merchnatListRequest);
                StringBuffer buffer = new StringBuffer();
                buffer.append(TransType.NEW_WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.getURL());
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
                                        Toast.makeText(getBaseContext(), "No merchants available", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getBaseContext(), "No merchants available", Toast.LENGTH_SHORT).show();
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
                new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
                showIfnotVisible("");

            }
        });



       //New UI

        createHorizontalList();


//        final TextView tv_a =  (TextView)findViewById(R.id.tv_a);
//        tv_a.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_a.getText().toString());
//
//            }
//        });
//
//        final TextView tv_b =  (TextView)findViewById(R.id.tv_b);
//        tv_b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_b.getText().toString());
//
//            }
//        });
//
//        final TextView tv_c =  (TextView)findViewById(R.id.tv_c);
//        tv_c.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_c.getText().toString());
//
//            }
//        });
//        final TextView tv_d =  (TextView)findViewById(R.id.tv_d);
//        tv_d.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_d.getText().toString());
//
//            }
//        });
//
//        final TextView tv_e =  (TextView)findViewById(R.id.tv_e);
//        tv_e.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_e.getText().toString());
//
//            }
//        });
//
//        final TextView tv_f =  (TextView)findViewById(R.id.tv_f);
//        tv_f.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_f.getText().toString());
//
//            }
//        });
//
//        final TextView tv_g =  (TextView)findViewById(R.id.tv_g);
//        tv_g.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_g.getText().toString());
//
//            }
//        });
//
//        final TextView tv_h =  (TextView)findViewById(R.id.tv_h);
//        tv_h.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_h.getText().toString());
//
//            }
//        });
//
//        final TextView tv_i =  (TextView)findViewById(R.id.tv_i);
//        tv_i.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_i.getText().toString());
//
//            }
//        });
//
//        final TextView tv_j =  (TextView)findViewById(R.id.tv_j);
//        tv_j.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_j.getText().toString());
//
//            }
//        });
//
//        final TextView tv_k =  (TextView)findViewById(R.id.tv_k);
//        tv_k.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_k.getText().toString());
//
//            }
//        });
//
//        final TextView tv_l =  (TextView)findViewById(R.id.tv_l);
//        tv_l.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_l.getText().toString());
//
//            }
//        });
//
//        final TextView tv_m =  (TextView)findViewById(R.id.tv_m);
//        tv_m.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_m.getText().toString());
//
//            }
//        });
//
//        final TextView tv_n =  (TextView)findViewById(R.id.tv_n);
//        tv_n.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_n.getText().toString());
//
//            }
//        });
//
//        final TextView tv_o =  (TextView)findViewById(R.id.tv_o);
//        tv_o.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_o.getText().toString());
//
//            }
//        });
//
//        final TextView tv_p =  (TextView)findViewById(R.id.tv_p);
//        tv_p.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_p.getText().toString());
//
//            }
//        });
//
//        final TextView tv_q =  (TextView)findViewById(R.id.tv_q);
//        tv_q.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_q.getText().toString());
//
//            }
//        });
//
//        final TextView tv_r =  (TextView)findViewById(R.id.tv_r);
//        tv_r.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_r.getText().toString());
//
//            }
//        });
//
//        final TextView tv_s =  (TextView)findViewById(R.id.tv_s);
//        tv_s.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_s.getText().toString());
//
//            }
//        });
//
//        final TextView tv_t =  (TextView)findViewById(R.id.tv_t);
//        tv_t.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_t.getText().toString());
//
//            }
//        });
//
//        final TextView tv_u =  (TextView)findViewById(R.id.tv_u);
//        tv_u.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_u.getText().toString());
//
//            }
//        });
//
//        final TextView tv_v =  (TextView)findViewById(R.id.tv_v);
//        tv_v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_v.getText().toString());
//
//            }
//        });
//
//        final TextView tv_w =  (TextView)findViewById(R.id.tv_w);
//        tv_w.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_w.getText().toString());
//
//            }
//        });
//
//        final TextView tv_x =  (TextView)findViewById(R.id.tv_x);
//        tv_x.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_x.getText().toString());
//
//            }
//        });
//
//        final TextView tv_y =  (TextView)findViewById(R.id.tv_y);
//        tv_y.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_y.getText().toString());
//
//            }
//        });
//
//        final TextView tv_z =  (TextView)findViewById(R.id.tv_z);
//        tv_z.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                adapter.getFilter().filter(tv_z.getText().toString());
//
//            }
//        });


        //Range listeners

        tv_a_d =  (TextView)findViewById(R.id.tv_a_d);
         tv_e_h =  (TextView)findViewById(R.id.tv_e_h);
       tv_i_l =  (TextView)findViewById(R.id.tv_i_l);
         tv_m_p =  (TextView)findViewById(R.id.tv_m_p);
        tv_q_t =  (TextView)findViewById(R.id.tv_q_t);
        tv_u_x =  (TextView)findViewById(R.id.tv_u_x);
       tv_y_z =  (TextView)findViewById(R.id.tv_y_z);
         tv_0_9 =  (TextView)findViewById(R.id.tv_0_9);



        tv_a_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.getFilter().filter(tv_a_d.getText().toString());

                tv_a_d.setBackgroundResource(R.drawable.oval_background_selected);

                tv_e_h.setBackgroundResource(R.drawable.oval_background);
                tv_i_l.setBackgroundResource(R.drawable.oval_background);
                tv_m_p.setBackgroundResource(R.drawable.oval_background);
                tv_q_t.setBackgroundResource(R.drawable.oval_background);
                tv_u_x.setBackgroundResource(R.drawable.oval_background);
                tv_y_z.setBackgroundResource(R.drawable.oval_background);
                tv_0_9.setBackgroundResource(R.drawable.oval_background);

            }
        });


        tv_e_h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.getFilter().filter(tv_e_h.getText().toString());

                tv_e_h.setBackgroundResource(R.drawable.oval_background_selected);

                tv_a_d.setBackgroundResource(R.drawable.oval_background);
                tv_i_l.setBackgroundResource(R.drawable.oval_background);
                tv_m_p.setBackgroundResource(R.drawable.oval_background);
                tv_q_t.setBackgroundResource(R.drawable.oval_background);
                tv_u_x.setBackgroundResource(R.drawable.oval_background);
                tv_y_z.setBackgroundResource(R.drawable.oval_background);
                tv_0_9.setBackgroundResource(R.drawable.oval_background);

            }
        });


        tv_i_l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.getFilter().filter(tv_i_l.getText().toString());

                tv_i_l.setBackgroundResource(R.drawable.oval_background_selected);

                tv_a_d.setBackgroundResource(R.drawable.oval_background);
                tv_e_h.setBackgroundResource(R.drawable.oval_background);
                tv_m_p.setBackgroundResource(R.drawable.oval_background);
                tv_q_t.setBackgroundResource(R.drawable.oval_background);
                tv_u_x.setBackgroundResource(R.drawable.oval_background);
                tv_y_z.setBackgroundResource(R.drawable.oval_background);
                tv_0_9.setBackgroundResource(R.drawable.oval_background);

            }
        });


        tv_m_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.getFilter().filter(tv_m_p.getText().toString());

                tv_m_p.setBackgroundResource(R.drawable.oval_background_selected);

                tv_a_d.setBackgroundResource(R.drawable.oval_background);
                tv_e_h.setBackgroundResource(R.drawable.oval_background);
                tv_i_l.setBackgroundResource(R.drawable.oval_background);
                tv_q_t.setBackgroundResource(R.drawable.oval_background);
                tv_u_x.setBackgroundResource(R.drawable.oval_background);
                tv_y_z.setBackgroundResource(R.drawable.oval_background);
                tv_0_9.setBackgroundResource(R.drawable.oval_background);

            }
        });


        tv_q_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.getFilter().filter(tv_q_t.getText().toString());

                tv_q_t.setBackgroundResource(R.drawable.oval_background_selected);

                tv_a_d.setBackgroundResource(R.drawable.oval_background);
                tv_e_h.setBackgroundResource(R.drawable.oval_background);
                tv_i_l.setBackgroundResource(R.drawable.oval_background);
                tv_m_p.setBackgroundResource(R.drawable.oval_background);
                tv_u_x.setBackgroundResource(R.drawable.oval_background);
                tv_y_z.setBackgroundResource(R.drawable.oval_background);
                tv_0_9.setBackgroundResource(R.drawable.oval_background);

            }
        });



        tv_u_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.getFilter().filter(tv_u_x.getText().toString());

                tv_u_x.setBackgroundResource(R.drawable.oval_background_selected);

                tv_a_d.setBackgroundResource(R.drawable.oval_background);
                tv_e_h.setBackgroundResource(R.drawable.oval_background);
                tv_i_l.setBackgroundResource(R.drawable.oval_background);
                tv_m_p.setBackgroundResource(R.drawable.oval_background);
                tv_q_t.setBackgroundResource(R.drawable.oval_background);
                tv_y_z.setBackgroundResource(R.drawable.oval_background);
                tv_0_9.setBackgroundResource(R.drawable.oval_background);

            }
        });


        tv_y_z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.getFilter().filter(tv_y_z.getText().toString());

                tv_y_z.setBackgroundResource(R.drawable.oval_background_selected);

                tv_a_d.setBackgroundResource(R.drawable.oval_background);
                tv_e_h.setBackgroundResource(R.drawable.oval_background);
                tv_i_l.setBackgroundResource(R.drawable.oval_background);
                tv_m_p.setBackgroundResource(R.drawable.oval_background);
                tv_q_t.setBackgroundResource(R.drawable.oval_background);
                tv_u_x.setBackgroundResource(R.drawable.oval_background);
                tv_0_9.setBackgroundResource(R.drawable.oval_background);

            }
        });




        tv_0_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.getFilter().filter(tv_0_9.getText().toString());

                tv_0_9.setBackgroundResource(R.drawable.oval_background_selected);

                tv_a_d.setBackgroundResource(R.drawable.oval_background);
                tv_e_h.setBackgroundResource(R.drawable.oval_background);
                tv_i_l.setBackgroundResource(R.drawable.oval_background);
                tv_m_p.setBackgroundResource(R.drawable.oval_background);
                tv_q_t.setBackgroundResource(R.drawable.oval_background);
                tv_u_x.setBackgroundResource(R.drawable.oval_background);
                tv_y_z.setBackgroundResource(R.drawable.oval_background);

            }
        });




    }


    public void createHorizontalList() {


        CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getApplicationContext()).getCustomerLoginRequestReponse();
        MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
        merchnatListRequest.setG_transType(TransType.NEW_WHERE_TO_PAY_REQUEST.name());
        merchnatListRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
        String json = new Gson().toJson(merchnatListRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.NEW_WHERE_TO_PAY_REQUEST.getURL());
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

                           final  LinearLayout topLinearLayout = new LinearLayout(MerchantListCatogorieyActivityNewFlowNewUIActivity.this);
                            // topLinearLayout.setLayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.FILL_PARENT);
                            topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);


                            List<CategoryModel> categoryList = new ArrayList<>();
                            final List<String> categoryNameList = new ArrayList<>();

                            for (int i = 0; i < merchantListResponse.getCategoryDetails().size(); i++) {

                                listItems[i] = merchantListResponse.getCategoryDetails().get(i).getCategoryName();




                                LayoutInflater  mInflater = LayoutInflater.from(MerchantListCatogorieyActivityNewFlowNewUIActivity.this);
                               final View wholeView = mInflater.inflate(R.layout.custom_category_view, null);

                                final ImageView imageView = (ImageView)wholeView.findViewById(R.id.iv_category_image);
                                final TextView tvView = (TextView )wholeView.findViewById(R.id.tv_category_name);

                                imageView.setTag(merchantListResponse.getCategoryDetails().get(i).getCategoryCode());

                                String categoryCode = merchantListResponse.getCategoryDetails().get(i).getCategoryCode();
                                String categoryName = merchantListResponse.getCategoryDetails().get(i).getCategoryName();


                                imageView.setTag(categoryCode +"-"+categoryName);
                                imageView.setId(Integer.parseInt(categoryCode));
                                categoryNameList.add(categoryName);




                                if (categoryName.equals("All")) {
                                    imageView.setImageResource(R.drawable.all_cat);
//                                    tvView.setText(categoryName);

                                    tvView.setText(R.string.where_to_pay_all);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);

                                } else if (categoryName.equals("Food & Beverage")) {
                                    imageView.setImageResource(R.drawable.food_cat);
//                                    tvView.setText("Food");

                                    tvView.setText(R.string.where_to_pay_food);


                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);

                                } else if (categoryName.equals("Fashion")) {
                                    imageView.setImageResource(R.drawable.fashion_cat);
//                                    tvView.setText(categoryName);

                                    tvView.setText(R.string.where_to_pay_fashion);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);


                                } else if (categoryName.equals("Retail")) {
                                    imageView.setImageResource(R.drawable.retail_cat);
//                                    tvView.setText(categoryName);

                                    tvView.setText(R.string.where_to_pay_retail);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);


                                } else if (categoryName.equals("Medical Care")) {
                                    imageView.setImageResource(R.drawable.medical_cat);
//                                    tvView.setText(categoryName);

                                    tvView.setText(R.string.where_to_pay_medical);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);


                                } else if (categoryName.equals("Health & Beauty")) {
                                    imageView.setImageResource(R.drawable.beauty_cat);
//                                    tvView.setText("Beauty");

                                    tvView.setText(R.string.where_to_pay_health);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);


                                } else if (categoryName.equals("Hotels & Accommodations")) {
                                    imageView.setImageResource(R.drawable.hotels_cat);
//                                    tvView.setText("Hotels");

                                    tvView.setText(R.string.where_to_pay_hotels);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);


                                } else if (categoryName.equals("Transportation")) {
                                    imageView.setImageResource(R.drawable.transportation_cat);
//                                    tvView.setText(categoryName);

                                    tvView.setText(R.string.where_to_pay_trasportation);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);


                                } else if (categoryName.equals("Groceries & Hypermarkets")) {
                                    imageView.setImageResource(R.drawable.grocer_cat);
//                                    tvView.setText("Groceries");

                                    tvView.setText(R.string.where_to_pay_groceries_hypermarket);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);


                                } else if (categoryName.equals("Electronics & Computers")) {
                                    imageView.setImageResource(R.drawable.electronic_cat);
//                                    tvView.setText("Electronics");

                                    tvView.setText(R.string.where_to_pay_electronics);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);


                                } else if (categoryName.equals("Games & Toys")) {
                                    imageView.setImageResource(R.drawable.toys_cat);
//                                    tvView.setText("Games");

                                    tvView.setText(R.string.where_to_pay_games);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);


                                } else if (categoryName.equals("Services")) {
                                    imageView.setImageResource(R.drawable.services_cat);
//                                    tvView.setText(categoryName);

                                    tvView.setText(R.string.where_to_pay_service);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);


                                } else if (categoryName.equals("Automotive")) {
                                    imageView.setImageResource(R.drawable.auto_cat);
//                                    tvView.setText(categoryName);

                                    tvView.setText(R.string.where_to_pay_automotive);

                                    CategoryModel  cm  =  new CategoryModel(categoryName,R.drawable.all);
                                    categoryList.add(cm);


                                }

//                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
//                                layoutParams.setMargins(10,10,20,20);
//                                wholeView.setLayoutParams(layoutParams);

                                topLinearLayout.addView(wholeView);


                                //Default selection
                                ImageView all_image_View = (ImageView) topLinearLayout.findViewWithTag("0-All");
                                all_image_View.setImageResource(R.drawable.all_cat_clicked);
                                CustomSharedPreferences.saveStringData(getApplicationContext(),String.valueOf(all_image_View.getTag().toString()),CustomSharedPreferences.SP_KEY.PREVIOUS_CAT_TAG);






                                imageView.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {


                                        etSearch.setText("");
                                        etSearch.requestFocus();



                                        tv_a_d.setBackgroundResource(R.drawable.oval_background);
                                        tv_e_h.setBackgroundResource(R.drawable.oval_background);
                                        tv_i_l.setBackgroundResource(R.drawable.oval_background);
                                        tv_m_p.setBackgroundResource(R.drawable.oval_background);
                                        tv_q_t.setBackgroundResource(R.drawable.oval_background);
                                        tv_u_x.setBackgroundResource(R.drawable.oval_background);
                                        tv_y_z.setBackgroundResource(R.drawable.oval_background);
                                        tv_0_9.setBackgroundResource(R.drawable.oval_background);


//                                        Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, "Tag: " + imageView.getTag(), Toast.LENGTH_LONG);
//                                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
//                                        toast.show();

                                        refreshMerchantsForACategory("" + imageView.getTag());




                                        String previous_saved_tag = CustomSharedPreferences.getStringData(getApplicationContext(),CustomSharedPreferences.SP_KEY.PREVIOUS_CAT_TAG);

                                        if(previous_saved_tag.length()>0) {
                                            String[] previous_categoryCodeAndCategoryName = previous_saved_tag.split("-");

                                            final String previous_categoryCode = previous_categoryCodeAndCategoryName[0];

                                            final String previous_categoryname = previous_categoryCodeAndCategoryName[1];

//                                        ImageView  previous_imageViewFromWholeView  = (ImageView) topLinearLayout.findViewById(Integer.parseInt(previous_categoryCode));


                                            ImageView previous_imageViewFromWholeView = (ImageView) topLinearLayout.findViewWithTag(previous_saved_tag);


                                            switch (previous_categoryname) {
                                                case "All":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.all_cat);

                                                    break;
                                                case "Food & Beverage":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.food_cat);

                                                    break;
                                                case "Fashion":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.fashion_cat);

                                                    break;
                                                case "Retail":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.retail_cat);

                                                    break;

                                                case "Medical Care":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.medical_cat);

                                                    break;

                                                case "Health & Beauty":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.beauty_cat);

                                                    break;

                                                case "Hotels & Accommodations":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.hotels_cat);

                                                    break;

                                                case "Transportation":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.transportation_cat);

                                                    break;


                                                case "Groceries & Hypermarkets":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.grocer_cat);

                                                    break;

                                                case "Electronics & Computers":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.electronic_cat);

                                                    break;


                                                case "Games & Toys":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.toys_cat);

                                                    break;

                                                case "Services":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.services_cat);

                                                    break;

                                                case "Automotive":
                                                    previous_imageViewFromWholeView.setImageResource(R.drawable.auto_cat);

                                                    break;

                                            }


                                        }




                                        CustomSharedPreferences.saveStringData(getApplicationContext(),String.valueOf(imageView.getTag().toString()),CustomSharedPreferences.SP_KEY.PREVIOUS_CAT_TAG);
//
//                                        ImageView  imageViewFromWholeView  = wholeView.findViewWithTag(imageView.getTag());
//
//                                        imageViewFromWholeView.setImageResource(R.drawable.fashion_cat);

                                        String[] categoryCodeAndCategoryName = imageView.getTag().toString().split("-");

                                        final String categoryCode =  categoryCodeAndCategoryName[0];
                                        final String categoryname =  categoryCodeAndCategoryName[1];

                                        categoryId = categoryCode;


                                        switch(categoryname){
                                            case "All":
                                                imageView.setImageResource(R.drawable.all_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_all);

                                                break;
                                            case "Food & Beverage":
                                                imageView.setImageResource(R.drawable.food_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_food);

                                                break;
                                            case "Fashion":
                                                imageView.setImageResource(R.drawable.fashion_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_fashion);

                                                break;
                                            case "Retail":
                                                imageView.setImageResource(R.drawable.retail_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_retail);

                                                break;

                                            case "Medical Care":
                                                imageView.setImageResource(R.drawable.medical_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_medical);

                                                break;

                                            case "Health & Beauty":
                                                imageView.setImageResource(R.drawable.beauty_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_health);

                                                break;

                                            case "Hotels & Accommodations":
                                                imageView.setImageResource(R.drawable.hotels_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_hotels);

                                                break;

                                            case "Transportation":
                                                imageView.setImageResource(R.drawable.transportation_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_trasportation);

                                                break;


                                            case "Groceries & Hypermarkets":
                                                imageView.setImageResource(R.drawable.grocer_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_groceries_hypermarket);

                                                break;

                                            case "Electronics & Computers":
                                                imageView.setImageResource(R.drawable.electronic_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_electronics);

                                                break;


                                            case "Games & Toys":
                                                imageView.setImageResource(R.drawable.toys_cat_clickde);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_games);

                                                break;

                                            case "Services":
                                                imageView.setImageResource(R.drawable.services_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_service);

                                                break;

                                            case "Automotive":
                                                imageView.setImageResource(R.drawable.auto_cat_clicked);
                                                //Title
                                                mTitleTextView.setText(R.string.where_to_pay_automotive);

                                                break;

                                        }





                                    }
                                });


                            }

                            scrollView.addView(topLinearLayout);





                        } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();

                            //  Toast.makeText(getBaseContext(), "No merchnats available", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } catch (Exception e) {
                        return;
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
//        showIfNotVisible("");


        //For map Button
        categoryId="0";
        refreshMerchantsForACategory("0-All");

//        freshMerchantsForACategory("0-All");


    }


    public void refreshMerchantsForACategory(String _categoryCodeAndCategoryName) {

        mTitleTextView.setText(R.string.where_to_pay_all);

        String[] categoryCodeAndCategoryName = _categoryCodeAndCategoryName.split("-");

        final String categoryCode =  categoryCodeAndCategoryName[0];
        final String categoryname =  categoryCodeAndCategoryName[1];

        CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getApplicationContext()).getCustomerLoginRequestReponse();
        final MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
        final MerchantListResponse merchantListResponse = (((CoreApplication) getApplicationContext()).getMerchantListResponse_category());
        merchnatListRequest.setG_transType(TransType.NEW_WHERE_TO_PAY_MERCHANTLIST_REQUEST.name());
        merchnatListRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
        merchnatListRequest.setCategoryID(categoryCode);
        String json = new Gson().toJson(merchnatListRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.NEW_WHERE_TO_PAY_MERCHANTLIST_REQUEST.getURL());
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
                        ((CoreApplication) getApplicationContext()).setCategoryId(categoryCode);

                        if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }



                       //OLD
//                        adapter.clear();
//                        setListAdapter(adapter);


                        //Dec 12 code
                        adapter = new CustomListAdapter(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, merchantListResponse.getMerchantDetails());
                        list.setAdapter(adapter);

//                        adapter.notifyDataSetChanged();




                    } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        /*Toast.makeText(getActivity(), "No merchnats available", Toast.LENGTH_SHORT).show();
                        return;*/
                        Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        finish();
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
        showIfNotVisible("");




    }


    public void freshMerchantsForACategory(String _categoryCodeAndCategoryName) {

        mTitleTextView.setText(R.string.where_to_pay_all);

        String[] categoryCodeAndCategoryName = _categoryCodeAndCategoryName.split("-");

        final String categoryCode =  categoryCodeAndCategoryName[0];
        final String categoryname =  categoryCodeAndCategoryName[1];

        CustomerLoginRequestReponse onlineLoginResponse = ((CoreApplication) getApplicationContext()).getCustomerLoginRequestReponse();
        final MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
        final MerchantListResponse merchantListResponse = (((CoreApplication) getApplicationContext()).getMerchantListResponse_category());
        merchnatListRequest.setG_transType(TransType.NEW_WHERE_TO_PAY_MERCHANTLIST_REQUEST.name());
        merchnatListRequest.setG_oauth_2_0_client_token(onlineLoginResponse.getOauth_2_0_client_token());
        merchnatListRequest.setCategoryID(categoryCode);
        String json = new Gson().toJson(merchnatListRequest);
        StringBuffer buffer = new StringBuffer();
        buffer.append(TransType.NEW_WHERE_TO_PAY_MERCHANTLIST_REQUEST.getURL());
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
                        ((CoreApplication) getApplicationContext()).setCategoryId(categoryCode);

                        if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                            Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                            toast.show();
                            Intent intent = new Intent(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }



                        //OLD
//                        adapter.clear();
//                        setListAdapter(adapter);


                        //Dec 12 code
                        adapter = new CustomListAdapter(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, merchantListResponse.getMerchantDetails());
                        list.setAdapter(adapter);

//                        adapter.notifyDataSetChanged();




                    } else if (response.getG_errorDescription().equalsIgnoreCase("Session expired")) {
                        Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, getResources().getString(R.string.session_expired), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        Intent intent = new Intent(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        /*Toast.makeText(getActivity(), "No merchnats available", Toast.LENGTH_SHORT).show();
                        return;*/
                        Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, response.getG_errorDescription(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                        toast.show();
                        finish();
                    }
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
                    Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
                    Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        new Thread(new ServerConnection(0, messageHandler, buffer.toString(), getApplicationContext())).start();
//        showIfNotVisible("");




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



//        etSearch.setText("");
//        createHorizontalList();
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




//    @Override
//    protected void onListItemClick(ListView l, View v, final int position, long id) {
//        super.onListItemClick(l, v, position, id);
//
//
//        categoryId = ((CoreApplication) getApplication()).getCategoryId();
//        categoryName = ((CoreApplication) getApplication()).getCategoryname();
//
//
//
//        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getApplication()).getCustomerLoginRequestReponse();
//        final MerchnatListRequest merchnatListRequest = new MerchnatListRequest();
//        merchantListResponse = ((CoreApplication) getApplication()).getMerchantListResponse_1();
//        merchnatListRequest.setCategoryID(categoryId);
//        merchnatListRequest.setMerchantName(merchantListResponse.getMerchantDetails().get(position).getMerchantName());
//        merchnatListRequest.setG_oauth_2_0_client_token(customerLoginRequestReponse.getOauth_2_0_client_token());
//        merchnatListRequest.setG_transType(TransType.NEW_WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.name());
//        String json = new Gson().toJson(merchnatListRequest);
//        StringBuffer buffer = new StringBuffer();
//        buffer.append(TransType.NEW_WHERE_TO_PAY_MERCHANT_BRANCHVIEW_REQUEST.getURL());
//        buffer.append("?d=" + URLUTF8Encoder.encode(json));
//        android.os.Handler messageHandler = new android.os.Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                hideifVisible();
//                if (msg.arg1 == ServerConnection.OPERATION_SUCCESS) {
//                    try {
//                        GenericResponse response = new Gson().fromJson((String) msg.obj, GenericResponse.class);
//                        if (null != response && response.getG_status() == 1) {
//                            merchantListResponse = new Gson().fromJson((String) msg.obj, MerchantListResponse.class);
//                            if (merchantListResponse.getMerchantDetails().size() == 0) {
//                                Toast.makeText(getBaseContext(), "No merchants available", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                                startActivity(intent);
//                            } else {
//                                ((CoreApplication) getApplication()).setMerchantName(merchantListResponse.getMerchantDetails().get(0).getMerchantName());
//                                ((CoreApplication) getApplication()).setMerchantListResponse(merchantListResponse);
//                                ((CoreApplication) getApplication()).setImagePath(merchantListResponse.getMerchantDetails().get(0).getPath());
//                                Intent intent = new Intent(getBaseContext(), Merchant_Map_Activity.class);
//                                startActivity(intent);
//                            }
//                        } else {
//                            Toast.makeText(getBaseContext(), "No merchants available", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    } catch (Exception e) {
//                        Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_GENERAL_SERVER) {
//                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_general_server_error), Toast.LENGTH_SHORT).show();
//                } else if (msg.arg1 == ServerConnection.OPERATION_FAILURE_NETWORK) {
//                    Toast.makeText(getBaseContext(), getResources().getString(R.string.failure_network_error), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//        };
//        new Thread(new ServerConnection(0, messageHandler, buffer.toString(),getApplicationContext())).start();
//        showIfnotVisible("");
//    }


    private ArrayList<MerchantCategoryDetailsListPojo> mDisplayedValuesForListClick;
    class CustomListAdapter extends BaseAdapter implements Filterable {

        private ArrayList<MerchantCategoryDetailsListPojo> mOriginalValues; // Original Values
        private ArrayList<MerchantCategoryDetailsListPojo> mDisplayedValues;

        public CustomListAdapter(Activity context, ArrayList<MerchantCategoryDetailsListPojo> mCategoryArrayList) {
//            super(context, R.layout.merchnat_list_row, mCategoryArrayList);

            this.mOriginalValues = mCategoryArrayList;
            this.mDisplayedValues = mCategoryArrayList;

            mDisplayedValuesForListClick = mCategoryArrayList;

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

        @Override
        public int getCount() {
            return mDisplayedValues.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
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

            if (convertView == null) {
                convertView = LayoutInflater.from(MerchantListCatogorieyActivityNewFlowNewUIActivity.this).inflate(R.layout.merchnat_list_row, parent, false);
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


            if (mDisplayedValues.size() > position){


                ImageLoader.getInstance()
                        .displayImage(mDisplayedValues.get(position).getPath(), holder.icon, options, new SimpleImageLoadingListener() {
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

            holder.text_name.setText(mDisplayedValues.get(position).getMerchantName());


        }


            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,FilterResults results) {

                    mDisplayedValues = (ArrayList<MerchantCategoryDetailsListPojo>) results.values; // has the filtered values

                    if(mDisplayedValues.size()>0) {

                    adapter.notifyDataSetChanged();  // notifies the data with new filtered values

                    //For list click
                    mDisplayedValuesForListClick = mDisplayedValues;

//                    adapter = new CustomListAdapter(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, mDisplayedValues);
//                    list.setAdapter(adapter);
                }else{
                    Toast toast = Toast.makeText(MerchantListCatogorieyActivityNewFlowNewUIActivity.this, "No Merchants found", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
                    toast.show();
                }



                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<MerchantCategoryDetailsListPojo> FilteredArrList = new ArrayList<MerchantCategoryDetailsListPojo>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<MerchantCategoryDetailsListPojo>(mDisplayedValues); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {



                        constraint = constraint.toString().toLowerCase();


                        for (int i = 0; i < mOriginalValues.size(); i++) {

                            String data = mOriginalValues.get(i).getMerchantName();

                            if(constraint.toString().equalsIgnoreCase("A-D")) {

                                if (data.toLowerCase().startsWith("a") ||data.toLowerCase().startsWith("b") ||data.toLowerCase().startsWith("c") ||data.toLowerCase().startsWith("d")) {

                                    MerchantCategoryDetailsListPojo mcd = new MerchantCategoryDetailsListPojo();
                                    mcd.setMerchantName(mOriginalValues.get(i).getMerchantName());
                                    mcd.setPath(mOriginalValues.get(i).getPath());
                                    mcd.setCategory(mOriginalValues.get(i).getCategory());
                                    FilteredArrList.add(mcd);
                                }
                            }else  if(constraint.toString().equalsIgnoreCase("E-H")) {

                                if (data.toLowerCase().startsWith("e") ||data.toLowerCase().startsWith("f") ||data.toLowerCase().startsWith("g") ||data.toLowerCase().startsWith("h")) {

                                    MerchantCategoryDetailsListPojo mcd = new MerchantCategoryDetailsListPojo();
                                    mcd.setMerchantName(mOriginalValues.get(i).getMerchantName());
                                    mcd.setPath(mOriginalValues.get(i).getPath());
                                    mcd.setCategory(mOriginalValues.get(i).getCategory());
                                    FilteredArrList.add(mcd);
                                }
                            }else  if(constraint.toString().equalsIgnoreCase("I-L")) {

                                if (data.toLowerCase().startsWith("i") ||data.toLowerCase().startsWith("j") ||data.toLowerCase().startsWith("k") ||data.toLowerCase().startsWith("l")) {

                                    MerchantCategoryDetailsListPojo mcd = new MerchantCategoryDetailsListPojo();
                                    mcd.setMerchantName(mOriginalValues.get(i).getMerchantName());
                                    mcd.setPath(mOriginalValues.get(i).getPath());
                                    mcd.setCategory(mOriginalValues.get(i).getCategory());
                                    FilteredArrList.add(mcd);
                                }
                            }else  if(constraint.toString().equalsIgnoreCase("M-P")) {

                                if (data.toLowerCase().startsWith("m") ||data.toLowerCase().startsWith("n") ||data.toLowerCase().startsWith("o") ||data.toLowerCase().startsWith("p")) {

                                    MerchantCategoryDetailsListPojo mcd = new MerchantCategoryDetailsListPojo();
                                    mcd.setMerchantName(mOriginalValues.get(i).getMerchantName());
                                    mcd.setPath(mOriginalValues.get(i).getPath());
                                    mcd.setCategory(mOriginalValues.get(i).getCategory());
                                    FilteredArrList.add(mcd);
                                }
                            }else  if(constraint.toString().equalsIgnoreCase("Q-T")) {

                                if (data.toLowerCase().startsWith("q") ||data.toLowerCase().startsWith("r") ||data.toLowerCase().startsWith("s") ||data.toLowerCase().startsWith("t")) {

                                    MerchantCategoryDetailsListPojo mcd = new MerchantCategoryDetailsListPojo();
                                    mcd.setMerchantName(mOriginalValues.get(i).getMerchantName());
                                    mcd.setPath(mOriginalValues.get(i).getPath());
                                    mcd.setCategory(mOriginalValues.get(i).getCategory());
                                    FilteredArrList.add(mcd);
                                }
                            }else  if(constraint.toString().equalsIgnoreCase("U-X")) {

                                if (data.toLowerCase().startsWith("u") ||data.toLowerCase().startsWith("v") ||data.toLowerCase().startsWith("w") ||data.toLowerCase().startsWith("x")) {

                                    MerchantCategoryDetailsListPojo mcd = new MerchantCategoryDetailsListPojo();
                                    mcd.setMerchantName(mOriginalValues.get(i).getMerchantName());
                                    mcd.setPath(mOriginalValues.get(i).getPath());
                                    mcd.setCategory(mOriginalValues.get(i).getCategory());
                                    FilteredArrList.add(mcd);
                                }
                            }else  if(constraint.toString().equalsIgnoreCase("Y-Z")) {

                                if (data.toLowerCase().startsWith("y") ||data.toLowerCase().startsWith("z") ) {

                                    MerchantCategoryDetailsListPojo mcd = new MerchantCategoryDetailsListPojo();
                                    mcd.setMerchantName(mOriginalValues.get(i).getMerchantName());
                                    mcd.setPath(mOriginalValues.get(i).getPath());
                                    mcd.setCategory(mOriginalValues.get(i).getCategory());
                                    FilteredArrList.add(mcd);
                                }
                            }else  if(constraint.toString().equalsIgnoreCase("0-9")) {

                                if (data.toLowerCase().startsWith("0") ||data.toLowerCase().startsWith("1") ||data.toLowerCase().startsWith("2") ||data.toLowerCase().startsWith("3")||data.toLowerCase().startsWith("4")||data.toLowerCase().startsWith("5")||data.toLowerCase().startsWith("6")||data.toLowerCase().startsWith("7")||data.toLowerCase().startsWith("8")||data.toLowerCase().startsWith("9")) {

                                    MerchantCategoryDetailsListPojo mcd = new MerchantCategoryDetailsListPojo();
                                    mcd.setMerchantName(mOriginalValues.get(i).getMerchantName());
                                    mcd.setPath(mOriginalValues.get(i).getPath());
                                    mcd.setCategory(mOriginalValues.get(i).getCategory());
                                    FilteredArrList.add(mcd);
                                }
                            }











                        }






                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
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

