package ycash.wallet.json.pojo.translimit;

import ycash.wallet.json.pojo.generic.GenericResponse;

public class TransactionLimitResponse extends GenericResponse {

	private String tranType;
	private String subTranType;
	private Double minValuePerTransaction;
	private Double maxValuePerTransaction;
	private Double tpinLimit;
	
//	public TransactionLimitResponse(String tranType, String subTranType, Integer minValuePerTransaction,
//			Integer maxValuePerTransaction) {
//		super();
//		this.tranType = tranType;
//		this.subTranType = subTranType;
//		this.minValuePerTransaction = minValuePerTransaction;
//		this.maxValuePerTransaction = maxValuePerTransaction;
//	}

	public TransactionLimitResponse() {
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getSubTranType() {
		return subTranType;
	}

	public void setSubTranType(String subTranType) {
		this.subTranType = subTranType;
	}

	public Double getMinValuePerTransaction() {
		return minValuePerTransaction;
	}

	public void setMinValuePerTransaction(Double minValuePerTransaction) {
		this.minValuePerTransaction = minValuePerTransaction;
	}

	public Double getMaxValuePerTransaction() {
		return maxValuePerTransaction;
	}

	public void setMaxValuePerTransaction(Double maxValuePerTransaction) {
		this.maxValuePerTransaction = maxValuePerTransaction;
	}

	public Double getTpinLimit() {
		return tpinLimit;
	}

	public void setTpinLimit(Double tpinLimit) {
		this.tpinLimit = tpinLimit;
	}
	
}
