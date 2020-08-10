package newflow;

import ycash.wallet.json.pojo.generic.GenericResponse;

public class NewFlowOfferNew extends GenericResponse {

//    {"offerDetails":{"OfferText":"BOOKEEY TEST1","offerName":"BOOKEEY TEST1"
//    "ImageFile":"https://demo.bookeey.com/mno/merlogo/img07817-mer1800056.jpg",
//                "to Date":"Oct 31,19 3:24","from Date":"Jan 23,19 3:24"},
//        "g_status":1,"g_status_description":"SUCCESS","g_errorDescription":"",
//            "g_response_trans_type":"NEW_OFFER_AllACTIVEDETAILS_RESPONSE"}

    String toDate;
    String fromDate;
    String ImageFile;
    String OfferText;

    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    String offerName;
    String merchantLogo;

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getImageFile() {
        return ImageFile;
    }

    public void setImageFile(String imageFile) {
        ImageFile = imageFile;
    }

    public String getOfferText() {
        return OfferText;
    }

    public void setOfferText(String offerText) {
        OfferText = offerText;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }


}
