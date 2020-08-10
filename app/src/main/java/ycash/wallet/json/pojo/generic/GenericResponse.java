package ycash.wallet.json.pojo.generic;

import com.bookeey.wallet.live.forceface.TransTypeInterface;

/**
 * 
 * @author mohit
 *
 */
public class GenericResponse implements TransTypeInterface{
	
	private int g_status;
	private String g_status_description;
	private String g_errorDescription;
	private String g_response_trans_type;
    private String knetUrl;
	private boolean speakstatus;

	public int getNotificationCount() {
		return notificationCount;
	}

	public void setNotificationCount(int notificationCount) {
		this.notificationCount = notificationCount;
	}

	private int notificationCount;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	private String reason;

	public boolean isSpeakstatus() {
		return speakstatus;
	}

	public void setSpeakstatus(boolean speakstatus) {
		this.speakstatus = speakstatus;
	}

	public int getG_status() {
		return g_status;
	}
	public void setG_status(int g_status) {
		this.g_status = g_status;
	}
	public String getG_status_description() {
		return g_status_description;
	}
	public void setG_status_description(String g_status_description) {
		this.g_status_description = g_status_description;
	}
	public String getG_errorDescription() {
		return g_errorDescription;
	}
	public void setG_errorDescription(String g_errorDescription) {
		this.g_errorDescription = g_errorDescription;
	}
	public String getG_response_trans_type() {
		return g_response_trans_type;
	}
	public void setG_response_trans_type(String g_response_trans_type) {
		this.g_response_trans_type = g_response_trans_type;
	}

    public String getKnetUrl() {
        return knetUrl;
    }

    public void setKnetUrl(String knetUrl) {
        this.knetUrl = knetUrl;
    }
}
