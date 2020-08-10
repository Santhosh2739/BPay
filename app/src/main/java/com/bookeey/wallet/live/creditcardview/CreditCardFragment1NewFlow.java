
package com.bookeey.wallet.live.creditcardview;

        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v4.content.ContextCompat;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RelativeLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bookeey.wallet.live.R;
        import com.bookeey.wallet.live.application.CoreApplication;
        import com.bookeey.wallet.live.mainmenu.MainActivity;
        import com.google.gson.Gson;

        import coreframework.database.CustomSharedPreferences;
        import coreframework.taskframework.ProgressDialogFrag;
        import coreframework.utils.FontTypeChange;
        import coreframework.utils.LocaleHelper;
        import coreframework.utils.PriceFormatter;
        import newflow.MainActivityNewFlow;
        import newflow_processing.DeviceIDSplashCheckProcessingNewFlow;
        import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
        import ycash.wallet.json.pojo.registration.CustomerMobileNumberRequest;

        import static com.facebook.FacebookSdk.getApplicationContext;

public class CreditCardFragment1NewFlow extends Fragment {


    FontTypeChange fontTypeChange = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.e("CreditCardFragment1","STARTED");


        View rootView = inflater.inflate(R.layout.credit_card_fragment1_newflow, container, false);

        Log.e("CreditCardFragment1","STARTED");

//        Button buttonInFragment1 = rootView.findViewById(R.id.button_1);
//        buttonInFragment1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "button in fragment 1", Toast.LENGTH_SHORT).show();
//            }
//        });



//        TextView tv_member_name = rootView.findViewById(R.id.tv_member_name);
//
//        TextView tv_member_mobile = rootView.findViewById(R.id.tv_member_mobile);
//
//        TextView balanceId = rootView.findViewById(R.id.tv_wallet_balance);


        fontTypeChange=new FontTypeChange(getActivity());

//        tv_member_name.setTypeface(fontTypeChange.get_fontface(2));
//        tv_member_mobile.setTypeface(fontTypeChange.get_fontface(2));

//        tv_member_name.setTypeface(fontTypeChange.get_fontface(3));
//        tv_member_mobile.setTypeface(fontTypeChange.get_fontface(3));
//
//
//        balanceId.setTypeface(fontTypeChange.get_fontface(3));

        //Test
//        tv_member_name.setText("A.R. Rahaman");
//        tv_member_mobile.setText("+9656565885");



//        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getActivity().getApplication()).getCustomerLoginRequestReponse();
//        if (new Gson().toJson(customerLoginRequestReponse) != null) {
//
//            tv_member_name.setText(customerLoginRequestReponse.getCustFirstName() + " " + customerLoginRequestReponse.getCustLastName());
//            tv_member_mobile.setText(customerLoginRequestReponse.getMobileNumber());
//            if (customerLoginRequestReponse.getWalletBalance() != null) {
//                balanceId.setText("KWD " + PriceFormatter.format(customerLoginRequestReponse.getWalletBalance(), 3, 3));
//            }
//        }
//



        LinearLayout load_money_layout = rootView.findViewById(R.id.load_money_layout);
        load_money_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(),"PAY",Toast.LENGTH_LONG).show();

//                ((MainActivityNewFlow)getActivity()).showNewFlowAlertDialogue();


                //Feb 13
                ((MainActivityNewFlow)getActivity()). showNewFlowAlertDialogueCreditCardView();


                //Commented on Feb 13 START to show new alert dialog

//                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
//                String deviceID = ((CoreApplication) getApplicationContext()).getThisDeviceUniqueAndroidId();
//                clr.setDeviceId(deviceID);
//
//                CoreApplication application = (CoreApplication) getApplicationContext();
//                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application,true,false,true));
//                ProgressDialogFrag progress = new ProgressDialogFrag();
//                Bundle bundle_req = new Bundle();
//                bundle_req.putString("uuid", uiProcessorReference);
//                progress.setCancelable(true);
//                progress.setArguments(bundle_req);
//                progress.show(getActivity().getSupportFragmentManager(), "progress_dialog");

                //Feb 13 END


            }
        });


        LinearLayout pay_layout = rootView.findViewById(R.id.pay_layout);
        pay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(),"PAY",Toast.LENGTH_LONG).show();

//                ((MainActivityNewFlow)getActivity()). showNewFlowAlertDialogue();


                //Feb 13
                ((MainActivityNewFlow)getActivity()). showNewFlowAlertDialogueCreditCardView();

                //Commented on Feb 13 START to show new alert dialog

//                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
//                String deviceID = ((CoreApplication) getApplicationContext()).getThisDeviceUniqueAndroidId();
//                clr.setDeviceId(deviceID);
//
//                CoreApplication application = (CoreApplication) getApplicationContext();
//                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application,true,false,true));
//                ProgressDialogFrag progress = new ProgressDialogFrag();
//                Bundle bundle_req = new Bundle();
//                bundle_req.putString("uuid", uiProcessorReference);
//                progress.setCancelable(true);
//                progress.setArguments(bundle_req);
//                progress.show(getActivity().getSupportFragmentManager(), "progress_dialog");

                //Feb 13 END


            }
        });


        TextView goto_tour_text = (TextView) rootView.findViewById(R.id.goto_tour_text);
        goto_tour_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/PTb5zQuNGsE"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        });


        LinearLayout demo_layout = (LinearLayout)rootView. findViewById(R.id.demo_layout);

        demo_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/PTb5zQuNGsE"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);

            }
        });



        //language selection



        RelativeLayout language_layout = (RelativeLayout)rootView. findViewById(R.id.language_layout);
        final ImageView coutry_flag_img = (ImageView)rootView. findViewById(R.id.coutry_flag_img);
//        final TextView language_text = (TextView)rootView. findViewById(R.id.language_text );

        String selectedLanguage = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE);
        final String language_to_be_displayed = CustomSharedPreferences.getStringData(getApplicationContext(), CustomSharedPreferences.SP_KEY.LANGUAGE_TO_BE_DISPLAYED);

        if (selectedLanguage != null && !selectedLanguage.isEmpty()) {
            LocaleHelper.setLocale(getActivity(), selectedLanguage);
        }


        if (selectedLanguage.equals("en")) {
//            language_text.setText(getResources().getString(R.string.login_arabic));
//            CustomSharedPreferences.saveStringData(getApplicationContext(), "ar", CustomSharedPreferences.SP_KEY.LANGUAGE_TO_BE_DISPLAYED);
            coutry_flag_img.setImageResource(R.drawable.kuwait);
        } else {
//            language_text.setText(getResources().getString(R.string.login_english));
//            CustomSharedPreferences.saveStringData(getApplicationContext(), "en", CustomSharedPreferences.SP_KEY.LANGUAGE_TO_BE_DISPLAYED);
            coutry_flag_img.setImageResource(R.drawable.usa);
        }


        language_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (language_to_be_displayed.equals("en")) {
//                    language_text.setText(getResources().getString(R.string.login_arabic));
                    CustomSharedPreferences.saveStringData(getApplicationContext(), "ar", CustomSharedPreferences.SP_KEY.LANGUAGE_TO_BE_DISPLAYED);
//                    selectedLanguage = "en";
                    coutry_flag_img.setImageResource(R.drawable.kuwait);
                    CustomSharedPreferences.saveStringData(getApplicationContext(), "en", CustomSharedPreferences.SP_KEY.LANGUAGE);
                    LocaleHelper.setLocale(getActivity(), "en");

//                    Toast.makeText(getActivity(),"111",Toast.LENGTH_LONG).show();

                    ((MainActivityNewFlow)getActivity()).refresh(getActivity(), "en");

                } else {
//                    selectedLanguage = "ar";
//                    language_text.setText(getResources().getString(R.string.login_english));
                    CustomSharedPreferences.saveStringData(getApplicationContext(), "en", CustomSharedPreferences.SP_KEY.LANGUAGE_TO_BE_DISPLAYED);

                    coutry_flag_img.setImageResource(R.drawable.usa);
                    CustomSharedPreferences.saveStringData(getApplicationContext(), "ar", CustomSharedPreferences.SP_KEY.LANGUAGE);
                    LocaleHelper.setLocale(getActivity(), "ar");

//                    Toast.makeText(getActivity(),"222",Toast.LENGTH_LONG).show();

                    ((MainActivityNewFlow)getActivity()).refresh(getActivity(), "ar");
                }
            }
        });


        ImageView img_new_flow_login_door  =  (ImageView)rootView.findViewById(R.id.img_new_flow_login_door);
        img_new_flow_login_door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Jan 22 After call  START

                CustomerMobileNumberRequest clr = new CustomerMobileNumberRequest();
                String deviceID = ((CoreApplication) getApplicationContext()).getThisDeviceUniqueAndroidId();
                clr.setDeviceId(deviceID);

                CoreApplication application = (CoreApplication) getApplicationContext();
                String uiProcessorReference = application.addUserInterfaceProcessor(new DeviceIDSplashCheckProcessingNewFlow(clr, true, application,true,false,true));
                ProgressDialogFrag progress = new ProgressDialogFrag();
                Bundle bundle_req = new Bundle();
                bundle_req.putString("uuid", uiProcessorReference);
                progress.setCancelable(true);
                progress.setArguments(bundle_req);
                progress.show(getActivity().getSupportFragmentManager(), "progress_dialog");

                //Jan 22 After call  END

            }
        });


        return rootView;
    }
}

