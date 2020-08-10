package ycash.wallet.json.pojo.wheretopay;

import com.bookeey.wallet.live.forceface.TransTypeInterface;

import java.util.ArrayList;

import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * Created by 30099 on 2/19/2016.
 */
public class MerchantListPojo extends GenericResponse {
    private long from;
    private long to;
    private long totalNumberOfmerchants;

    ArrayList<TransTypeInterface> records = new ArrayList<TransTypeInterface>();

    public ArrayList<TransTypeInterface> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<TransTypeInterface> records) {
        this.records = records;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public long getTotalNumberOfmerchants() {
        return totalNumberOfmerchants;
    }

    public void setTotalNumberOfmerchants(long totalNumberOfmerchants) {
        this.totalNumberOfmerchants = totalNumberOfmerchants;
    }
}
