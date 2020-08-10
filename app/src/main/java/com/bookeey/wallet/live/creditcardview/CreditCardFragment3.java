//package com.bookeey.wallet.live.creditcardview;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bookeey.wallet.live.R;
//import com.bookeey.wallet.live.application.CoreApplication;
//import com.bookeey.wallet.live.mainmenu.MainActivity;
//import com.google.gson.Gson;
//
//import coreframework.utils.FontTypeChange;
//import coreframework.utils.PriceFormatter;
//import ycash.wallet.json.pojo.login.CustomerLoginRequestReponse;
//
//public class CreditCardFragment3 extends Fragment {
//
//
//    FontTypeChange fontTypeChange = null;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        View rootView = inflater.inflate(R.layout.credit_card_fragment1, container, false);
////        Button buttonInFragment1 = rootView.findViewById(R.id.button_1);
////        buttonInFragment1.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Toast.makeText(getContext(), "button in fragment 1", Toast.LENGTH_SHORT).show();
////            }
////        });
//
//
//        ImageView img_credit_card_bookeey = rootView.findViewById(R.id.img_credit_card_bookeey);
//        ImageView img_credit_warba = rootView.findViewById(R.id.img_credit_warba);
//        ImageView img_credit_card_type = rootView.findViewById(R.id.img_credit_card_type);
//
//
//        TextView tv_member_name = rootView.findViewById(R.id.tv_member_name);
//
//        TextView tv_member_mobile = rootView.findViewById(R.id.tv_member_mobile);
//
//        TextView balanceId = rootView.findViewById(R.id.balanceId);
//
//
//        fontTypeChange=new FontTypeChange(getActivity());
//        tv_member_name.setTypeface(fontTypeChange.get_fontface(2));
//        tv_member_mobile.setTypeface(fontTypeChange.get_fontface(2));
//        balanceId.setTypeface(fontTypeChange.get_fontface(3));
//
//        //Test
//        tv_member_name.setText("A.R. Rahaman");
//        tv_member_mobile.setText("+9656565885");
//
//
//
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
//
//
//
//
//        img_credit_card_bookeey.setImageResource(R.drawable.bookeey_newlogo2);
//
//        img_credit_warba.setImageResource(R.drawable.warba_black_bg);
//
//        img_credit_card_type.setImageResource(R.drawable.ic_amex);
//        img_credit_card_type.setVisibility(View.INVISIBLE);
//
//
//        LinearLayout pay_layout = rootView.findViewById(R.id.pay_layout);
//        pay_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                Toast.makeText(getActivity(),"PAY",Toast.LENGTH_LONG).show();
//
//                ((MainActivity)getActivity()).showAmountEntryPayDialogue();
//
//
//            }
//        });
//
//
//        return rootView;
//    }
//}
