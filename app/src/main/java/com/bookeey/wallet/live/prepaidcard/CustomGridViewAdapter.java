package com.bookeey.wallet.live.prepaidcard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bookeey.wallet.live.R;
import com.bookeey.wallet.live.application.CoreApplication;

import java.util.List;

import ycash.wallet.json.pojo.virtualprepaidcards.CardDetails;

/**
 * Created by 30099 on 6/16/2016.
 */
public class CustomGridViewAdapter extends ArrayAdapter<CardDetails> {
    Context context;
    DenominationViewHolder holder = null;
    CardDetails cardDetails;
    List<CardDetails> cardDetailsList;

    public CustomGridViewAdapter(Context context, List<CardDetails> cardDetailsList) {
        super(context, R.layout.denomination_text_view, cardDetailsList);
        this.context = context;
        this.cardDetailsList = cardDetailsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        cardDetails = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.denomination_text_view, parent, false);
            holder = new DenominationViewHolder();
            holder.denomination_price_txt = (TextView) convertView.findViewById(R.id.denomination_price_txt);
            convertView.setTag(holder);
        } else {
            holder = (DenominationViewHolder) convertView.getTag();
        }
        ((CoreApplication) context.getApplicationContext()).setCardId(cardDetails.getCardId());
        ((CoreApplication) context.getApplicationContext()).setCardPrice(cardDetails.getPrice().replace("GBP", "\u00A3"));
        holder.denomination_price_txt.setText(cardDetails.getPrice().replace("GBP", "\u00A3"));
        ((CoreApplication) context.getApplicationContext()).setCardPrice(cardDetails.getPrice());
        return convertView;
    }

    static class DenominationViewHolder {
        TextView denomination_price_txt;
    }

}