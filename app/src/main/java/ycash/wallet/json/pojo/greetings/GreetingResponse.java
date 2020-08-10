package ycash.wallet.json.pojo.greetings;

import java.io.Serializable;

import ycash.wallet.json.pojo.generic.GenericResponse;


/**
 * @author poongodi
 */
public class GreetingResponse extends GenericResponse implements Serializable {
	
	private static final long serialVersionUID = 1832859276676989814L;

	private String imagePath;
	private String text;
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
