package com.bookeey.wallet.live.recharge;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import java.util.HashMap;
/**
 * Created by 30099 on 5/3/2016.
 */
public class CustomAutoCompleteTextView extends AutoCompleteTextView {
    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected  CharSequence convertSelectionToString(Object selectedItem){
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("name");
    }
}