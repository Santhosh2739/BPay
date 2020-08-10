package ycash.wallet.json.pojo.txnhistory;

import java.util.ArrayList;

import com.bookeey.wallet.live.forceface.TransTypeInterface;
import ycash.wallet.json.pojo.generic.GenericResponse;


public class TransactionHistoryResponse extends GenericResponse {

	private long from;
	private long to;
	private long totalNoOfTransactions;
	ArrayList<TransTypeInterface> records = new ArrayList<TransTypeInterface>();
	
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
	public long getTotalNoOfTransactions() {
		return totalNoOfTransactions;
	}
	public void setTotalNoOfTransactions(long totalNoOfTransactions) {
		this.totalNoOfTransactions = totalNoOfTransactions;
	}
	public ArrayList<TransTypeInterface> getRecords() {
		return records;
	}
	public void setRecords(ArrayList<TransTypeInterface> records) {
		this.records = records;
	}
	
}
