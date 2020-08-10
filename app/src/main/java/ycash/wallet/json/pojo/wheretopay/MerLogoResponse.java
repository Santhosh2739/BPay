package ycash.wallet.json.pojo.wheretopay;


import android.os.Parcel;
import android.os.Parcelable;

import ycash.wallet.json.pojo.generic.GenericResponse;

public class MerLogoResponse extends GenericResponse implements Parcelable{
	private byte[] merchantLogo;

	public byte[] getMerchantLogo() {
		return merchantLogo;
	}
	public void setMerchantLogo(byte[] merchantLogo) {
		this.merchantLogo = merchantLogo;
	}

	public MerLogoResponse(){}

	@Override
	public int describeContents() {
		return 0;
	}
	protected MerLogoResponse(Parcel in) {
		merchantLogo = new byte[in.readInt()];
		in.readByteArray(merchantLogo);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(merchantLogo.length);
		dest.writeByteArray(merchantLogo);
	}
	public static final Creator<MerLogoResponse> CREATOR = new Creator<MerLogoResponse>() {
		@Override
		public MerLogoResponse createFromParcel(Parcel in) {
			return new MerLogoResponse(in);
		}
		@Override
		public MerLogoResponse[] newArray(int size) {
			return new MerLogoResponse[size];
		}
	};
}
