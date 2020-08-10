package newflow;

import java.util.List;

import ycash.wallet.json.pojo.generic.GenericResponse;

public class OffersNewFlow extends GenericResponse {

    public List<NewFlowOffer> getNewFlowOffers() {
        return merchantOffers;
    }

    public void setNewFlowOffers(List<NewFlowOffer> newFlowOffers) {
        this.merchantOffers = newFlowOffers;
    }

    List<NewFlowOffer> merchantOffers;
}
