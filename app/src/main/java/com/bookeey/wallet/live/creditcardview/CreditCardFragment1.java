package com.bookeey.wallet.live.creditcardview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;
import com.bookeey.wallet.live.mainmenu.MainActivity;
import com.google.gson.Gson;

import coreframework.utils.FontTypeChange;
import coreframework.utils.PriceFormatter;
import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;

public class CreditCardFragment1 extends Fragment {


    FontTypeChange fontTypeChange = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.e("CreditCardFragment1","STARTED");


        View rootView = inflater.inflate(R.layout.credit_card_fragment1, container, false);

        Log.e("CreditCardFragment1","STARTED");

//        Button buttonInFragment1 = rootView.findViewById(R.id.button_1);
//        buttonInFragment1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "button in fragment 1", Toast.LENGTH_SHORT).show();
//            }
//        });



        TextView tv_member_name = rootView.findViewById(R.id.tv_member_name);

        TextView tv_member_mobile = rootView.findViewById(R.id.tv_member_mobile);

        TextView balanceId = rootView.findViewById(R.id.tv_wallet_balance);


        fontTypeChange=new FontTypeChange(getActivity());

//        tv_member_name.setTypeface(fontTypeChange.get_fontface(2));
//        tv_member_mobile.setTypeface(fontTypeChange.get_fontface(2));

        tv_member_name.setTypeface(fontTypeChange.get_fontface(3));
        tv_member_mobile.setTypeface(fontTypeChange.get_fontface(3));


        balanceId.setTypeface(fontTypeChange.get_fontface(3));

        //Test
//        tv_member_name.setText("A.R. Rahaman");
//        tv_member_mobile.setText("+9656565885");



        CustomerLoginRequestReponse customerLoginRequestReponse = ((CoreApplication) getActivity().getApplication()).getCustomerLoginRequestReponse();
        if (new Gson().toJson(customerLoginRequestReponse) != null) {

            tv_member_name.setText(customerLoginRequestReponse.getCustFirstName() + " " + customerLoginRequestReponse.getCustLastName());
            tv_member_mobile.setText(customerLoginRequestReponse.getMobileNumber());
            if (customerLoginRequestReponse.getWalletBalance() != null) {
                balanceId.setText("KWD " + PriceFormatter.format(customerLoginRequestReponse.getWalletBalance(), 3, 3));
            }
        }




        LinearLayout load_money_layout = rootView.findViewById(R.id.load_money_layout);
        load_money_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(),"PAY",Toast.LENGTH_LONG).show();

                ((MainActivity)getActivity()).loadMoneyCheck();


            }
        });


        LinearLayout pay_layout = rootView.findViewById(R.id.pay_layout);
        pay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(),"PAY",Toast.LENGTH_LONG).show();

                ((MainActivity)getActivity()). showAmountEntryPayDialogue();;


            }
        });



        return rootView;
    }
}
