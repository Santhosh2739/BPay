package ycash.wallet.json.pojo.virtualprepaidcards;

import java.io.Serializable;
import java.sql.Blob;

import ycash.wallet.json.pojo.generic.GenericRequest;


/**
 * 
 * @author poongodi
 *
 */

public class DenominationRequest extends GenericRequest implements Serializable {

	private static final long serialVersionUID = -6351977117167548295L;

	private String cardName;
	private String countryName;
	private String price;

	private Integer cardId;
	private String refId;
	private String tpin;
	private String store;

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getTpin() {
		return tpin;
	}

	public void setTpin(String tpin) {
		this.tpin = tpin;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

}
