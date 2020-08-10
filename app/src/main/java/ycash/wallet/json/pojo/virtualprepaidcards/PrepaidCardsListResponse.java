package ycash.wallet.json.pojo.virtualprepaidcards;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ycash.wallet.json.pojo.generic.GenericResponse;


/**
 * @author chandrasekhar
 */
public class PrepaidCardsListResponse extends GenericResponse implements Serializable {
	
	private static final long serialVersionUID = 1832859276676989814L;

	List<CardDetails> priceList;
	Map<Integer,CardDetails> list;
	private String store;
	private List<String> denominations;
	private List<String> denominationsInKD;
	private String operatorName;
	private String operatorType;
	private List<String> stores;
	private  int cardId;

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public List<String> getStores() {
		return stores;
	}

	public void setStores(List<String> stores) {
		this.stores = stores;
	}

	public List<String> getDenominations() {
		return denominations;
	}

	public void setDenominations(List<String> denominations) {
		this.denominations = denominations;
	}

	public List<String> getDenominationsInKD() {
		return denominationsInKD;
	}

	public void setDenominationsInKD(List<String> denominationsInKD) {
		this.denominationsInKD = denominationsInKD;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/*public List<CardDetails> getList() {
		return priceList;
	}

	public void setList(List<CardDetails> list) {
		priceList = list;
	}


	public void setList(Map<Integer, CardDetails> list) {
		this.list = list;
	}*/

	public List<CardDetails> getPriceList() {
		return priceList;
	}

	public void setPriceList(List<CardDetails> priceList) {
		this.priceList = priceList;
	}

	public Map<Integer, CardDetails> getList() {
		return list;
	}

	public void setList(Map<Integer, CardDetails> list) {
		this.list = list;
	}
}
