package ycash.wallet.json.pojo.offers;

import java.util.ArrayList;
import java.util.List;

import ycash.wallet.json.pojo.generic.GenericResponse;


public class OfferResponse extends GenericResponse {

	List<Offers> offers;

	public List<Offers> getOffers() {
		return offers;
	}

	public void setOffers(List<Offers> offers) {
		this.offers = offers;
	}




}
