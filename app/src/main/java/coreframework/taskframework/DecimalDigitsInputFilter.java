package coreframework.taskframework;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter {

	Pattern mPattern;
	float max_amount = 0f;

	public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero, float max_amount) {
		if(digitsAfterZero!=0){
			mPattern= Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
		}else{
//			mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}");
			mPattern= Pattern.compile("[0-9]{0," + digitsBeforeZero + "}");
		}
		this.max_amount = max_amount; 
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		Matcher matcher=mPattern.matcher(dest);
		if(!matcher.matches())
			return "";
		try{
			float in_amount = Float.parseFloat(dest.toString() + source.toString());
			if(in_amount>max_amount){
				return "";
			}
		}catch(Exception e){
			return "";
		}
		return null;
	}

}
