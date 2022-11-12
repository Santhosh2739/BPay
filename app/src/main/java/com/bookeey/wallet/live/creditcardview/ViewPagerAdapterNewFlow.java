package com.bookeey.wallet.live.creditcardview;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapterNewFlow extends FragmentPagerAdapter {

    public ViewPagerAdapterNewFlow(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new CreditCardFragment1NewFlow(); //ChildFragment1 at position 0
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
