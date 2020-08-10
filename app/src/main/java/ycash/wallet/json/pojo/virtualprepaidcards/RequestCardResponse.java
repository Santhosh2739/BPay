package ycash.wallet.json.pojo.virtualprepaidcards;

import java.io.Serializable;
import java.math.BigDecimal;

import ycash.wallet.json.pojo.generic.GenericResponse;


/**
 * @author chandrasekhar
 */
public class RequestCardResponse extends GenericResponse implements Serializable {

	private BigDecimal totalRedemptionPoints;

	public BigDecimal getTotalRedemptionPoints() {
		return totalRedemptionPoints;
	}

	public void setTotalRedemptionPoints(BigDecimal totalRedemptionPoints) {
		this.totalRedemptionPoints = totalRedemptionPoints;
	}


	private static final long serialVersionUID = 1832859276676989814L;

	
	private Integer cardId;
	private String price;
	private String refId;
	private String prepaidhtml;

	private VocherL2Response vocherList;
	private String balAfter;

	public String getBalAfter() {
		return balAfter;
	}

	public void setBalAfter(String balAfter) {
		this.balAfter = balAfter;
	}

	public VocherL2Response getVocherList() {
		return vocherList;
	}

	public void setVocherList(VocherL2Response vocherList) {
		this.vocherList = vocherList;
	}

	public String getPrepaidhtml() {
		return prepaidhtml;
	}

	public void setPrepaidhtml(String prepaidhtml) {
		this.prepaidhtml = prepaidhtml;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Integer getCardId() {
		return cardId;
	}
	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}


}
