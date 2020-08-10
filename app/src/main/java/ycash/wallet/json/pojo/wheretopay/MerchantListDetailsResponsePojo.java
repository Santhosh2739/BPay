package ycash.wallet.json.pojo.wheretopay;

import java.util.ArrayList;

import ycash.wallet.json.pojo.generic.GenericResponse;


public class MerchantListDetailsResponsePojo extends GenericResponse {
	
	
	private String merchantId;
	private String merchantName;
	private String merchantAddress;
	private byte[] merchantLogo;
    private String merchantRefNumber;
	private boolean isImageLoadingError = false;
    private Double latitude;
    private Double longitude;
    private String merchantLocation;
	public boolean isImageLoadingError() {
		return isImageLoadingError;
	}
	public void setIsImageLoadingError(boolean isImageLoadingError) {
		this.isImageLoadingError = isImageLoadingError;
	}
	public String getMerchantName() {
		return merchantName;
	}
    public String getMerchantRefNumber() {
        return merchantRefNumber;
    }
    public void setMerchantRefNumber(String merchantRefNumber) {
        this.merchantRefNumber = merchantRefNumber;
    }
    public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantAddress() {
		return merchantAddress;
	}
	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}
	public byte[] getMerchantLogo() {
		return merchantLogo;
	}
	public void setMerchantLogo(byte[] merchantLogo) {
		this.merchantLogo = merchantLogo;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
    public String getMerchantLocation() {
        return merchantLocation;
    }
    public void setMerchantLocation(String merchantLocation) {
        this.merchantLocation = merchantLocation;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
