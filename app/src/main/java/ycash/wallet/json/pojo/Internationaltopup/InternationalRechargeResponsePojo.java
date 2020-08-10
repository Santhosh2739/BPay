package ycash.wallet.json.pojo.Internationaltopup;


import java.util.List;

public class InternationalRechargeResponsePojo  {

	private List<CountryAndCode> countryNameAndCodeDetails;
    private List<DenominationPriceList> denominationList;

    public List<DenominationPriceList> getDenominationList() {
        return denominationList;
    }

    public void setDenominationList(List<DenominationPriceList> denominationList) {
        this.denominationList = denominationList;
    }

    public List<CountryAndCode> getCountryNameAndCodeDetails() {
        return countryNameAndCodeDetails;
    }

    public void setCountryNameAndCodeDetails(List<CountryAndCode> countryNameAndCodeDetails) {
        this.countryNameAndCodeDetails = countryNameAndCodeDetails;
    }
}
