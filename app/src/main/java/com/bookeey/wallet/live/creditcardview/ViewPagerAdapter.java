package com.bookeey.wallet.live.creditcardview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new CreditCardFragment1(); //ChildFragment1 at position 0
//            case 1:
//                return new CreditCardFragment2(); //ChildFragment2 at position 1
//            case 2:
//                return new CreditCardFragment3(); //ChildFragment3 at position 2
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 1; //three fragments
    }
}
