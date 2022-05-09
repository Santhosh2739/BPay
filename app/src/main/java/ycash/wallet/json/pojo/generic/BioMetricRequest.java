package ycash.wallet.json.pojo.generic;


/**
 * 
 * @author mohit
 *
 */
public class BioMetricRequest extends GenericRequest {
	
	private boolean biometric;

	public boolean isBiometric() {
		return biometric;
	}

	public void setBiometric(boolean biometric) {
		this.biometric = biometric;
	}
	public BioMetricRequest() {
		super.setG_transType(TransType.REGISTER_ACTIVATION.name());
	}

}
