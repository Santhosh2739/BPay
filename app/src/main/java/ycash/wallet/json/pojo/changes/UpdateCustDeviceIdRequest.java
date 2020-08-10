package ycash.wallet.json.pojo.changes;


import ycash.wallet.json.pojo.login.GenericRequest;

public class UpdateCustDeviceIdRequest extends GenericRequest {
	
		public String mobilenumber;
		public String civilid;
		public String walletPin;
		public String deviceid;
		public String tpin;
	    
		public String getMobilenumber() {
			return mobilenumber;
		}
		public void setMobilenumber(String mobilenumber) {
			this.mobilenumber = mobilenumber;
		}
		public String getCivilid() {
			return civilid;
		}
		public void setCivilid(String civilid) {
			this.civilid = civilid;
		}
		public String getWalletPin() {
			return walletPin;
		}
		public void setWalletPin(String walletPin) {
			this.walletPin = walletPin;
		}
		public String getDeviceid() {
			return deviceid;
		}
		public void setDeviceid(String deviceid) {
			this.deviceid = deviceid;
		}
	    

}
