package com.bookeey.wallet.live.mainmenu;
/**
 * Created by 30099 on 4/13/2016.
 */
public interface Processor {
    public String[] getDisplayable();
    public Integer[] getTypes();
    public boolean isDoubleLine();
    public String[] getNextLineDisplayable();
}