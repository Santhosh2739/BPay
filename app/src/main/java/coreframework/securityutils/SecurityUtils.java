package coreframework.securityutils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import coreframework.utils.Hex;

public class SecurityUtils {

	public static byte[] generateMac(byte[] key, byte[] deviceId,byte[] cipherText,	int offset, int length, int mac_size_bits){
		byte[] result = null;
		try{
			byte[] data = new byte[length];
			System.arraycopy(cipherText, offset, data, 0, data.length);
			if(data.length%16!=0){
				//MAKE THE DATA A 16 MULTIPLE
				byte[] temp = new byte[(data.length/16)*16+16];
				System.arraycopy(data, 0, temp, 0, data.length);
				data = temp;
			}
			byte[] enciphered = encipherData(key,deviceId, data, 0, data.length);
			//Now Identify the source index for array copy!
			int source_index = (((data.length/16)-1)*16);
			byte[] buffer = new byte[mac_size_bits/8];
			System.arraycopy(enciphered, source_index, buffer, 0, buffer.length);
			result =  buffer;
		}catch(Exception e){
			
		}
		return result;
	}
	public static byte[] getHash(byte[] input) {
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(input);
			return md.digest();
		}catch(Exception e){
			return null;
		}
	}
	public static final byte[] computeIV(byte[] deviceId){
		byte[] digest = getHash(deviceId);
		byte[] result = new byte[16];
		System.arraycopy(digest, 0, result, 0, 16);
		for(int i=0;i<result.length;i++){
			result[i] ^= digest[16-i];
		}
		return result;
	}
	public static byte[] encipherData(byte[] key,byte[] deviceId, byte[] plainText, int offset,	int length) {
		SecretKeySpec skeySpec = null;
		try {
			skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126PADDING");
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,new IvParameterSpec(computeIV(deviceId)));
	        byte[] encrypted = cipher.doFinal(plainText, offset, length);
	        return encrypted;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			skeySpec = null;
		}
	}
	public static byte[] encryptAndGenerateMac(byte[] encKey, byte[] macKey,byte[] deviceId,
			byte[] plainText, int offset, int length, int mac_size_bits) {
		byte[] encryptedData = encipherData(encKey,deviceId ,plainText, offset, length);
		byte[] macedData = generateMac(macKey, deviceId, encryptedData, 0,
				encryptedData.length, mac_size_bits);
		byte[] result = new byte[encryptedData.length + macedData.length];
		System.arraycopy(encryptedData, 0, result, 0, encryptedData.length);
		System.arraycopy(macedData, 0, result, encryptedData.length,
				macedData.length);
		return result;
	}
	//HELPER CLASS FOR SERVER |DECIPHER AND GET JSON
	public static String verifyMacAndRetrieveJson(String userCipherText,String userEncKey,String userMacKey, String userDeviceId){
		
		byte[] cipherText 	= Hex.toByteArr(userCipherText.trim());
		byte[] enc_key 		= Hex.toByteArr(userEncKey);
		byte[] mac_key 		= Hex.toByteArr(userMacKey);
		byte[] device_id 	= Hex.toByteArr(userDeviceId);
		byte[] plainText 	=  verifyMacAndDecipherData(enc_key, mac_key, device_id, cipherText, 0, cipherText.length-4, 32, cipherText.length-4);
		
		return plainText!=null?new String(plainText):null;
	}
	public static String generteSecureJson(String plainJson,String userEncKey,String userMacKey, String userDeviceId){
		
		byte[] plainText 	= plainJson.getBytes();
		byte[] enc_key 		= Hex.toByteArr(userEncKey);
		byte[] mac_key 		= Hex.toByteArr(userMacKey);
		byte[] device_id 	= Hex.toByteArr(userDeviceId);
		byte[] cipherText	= encryptAndGenerateMac(enc_key, mac_key, device_id, plainText, 0, plainText.length, 32);
		
		return cipherText!=null?Hex.toHex(cipherText):null;
	}
	
	public static byte[] verifyMacAndDecipherData(byte[] encKey, byte[] macKey,byte[] deviceId,
			byte[] cipherText, int offset, int length, int mac_size_bits,
			int mac_offset) {
		byte[] mac_generated = generateMac(macKey, deviceId, cipherText, offset, length,
				mac_size_bits);
		byte[] provided_mac = new byte[mac_size_bits / 8];
		System.arraycopy(cipherText, mac_offset, provided_mac, 0,
				provided_mac.length);
		
		if (Arrays.equals(mac_generated, provided_mac)) {
			// Mac Validation Have Passed, now perform decipher operation!
			//Log.e(" Mac Validation  Passed, now perform decipher operation!","....");
			return decipherData(encKey,deviceId, cipherText, offset, length);
		} else {
			//Log.e("Mac Validation has failed for any reason", "....");
			// Mac Validation has failed for any reason
			return null;
		}
	}
	//PKCS5PADDING
	public static byte[] decipherData(byte[] key,byte[] deviceId, byte[] cipherText,
			int offset, int length) {
		SecretKeySpec skeySpec = null;
		try {
			skeySpec = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126PADDING");
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec,new IvParameterSpec(computeIV(deviceId)));
	        byte[] xresult = cipher.doFinal(cipherText, offset, length);
	        return xresult;
		} catch (Exception e) {
			return null;
		} finally {
			skeySpec = null;
		}
	}
	/**
	 * @param <>
	 * @functionality Method returns the AESKey.
	 */
	public static byte[] generateApplicationAESKey() {
		SecureRandom sr = new SecureRandom();
		byte[] AESKey = new byte[16];
		sr.nextBytes(AESKey);
		return AESKey;
	}
	/**
	 * @param length
	 *            <p>
	 * @functionality Method returns the AESKey of given length.
	 */
	public static byte[] generateApplicationAESKey(int length) {
		SecureRandom sr = new SecureRandom();
		byte[] AESKey = new byte[length];
		sr.nextBytes(AESKey);
		return AESKey;
	}
//	public static void main(String[] args) {
//		byte[] enc_key = {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07, 0x08,0x09,0x0A,0X0B,0X0C,0X0D,0X0E,0X0F};
//		byte[] mac_key = {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07, 0x08,0x09,0x0A,0X0B,0X0C,0X0D,0X0E,0X0F};
//		byte[] deviceid = {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07, 0x08,0x09,0x0A,0X0B,0X0C,0X0D,0X0E,0X0F};
////		byte[] plainText = "jasonGson".getBytes();
////		byte[] cipheredData = encryptAndGenerateMac(enc_key, mac_key,deviceid, plainText, 0, plainText.length, 32);
////		System.out.println(new BigInteger(1,cipheredData));
////		System.out.println(Hex.toHex(cipheredData));
////		System.out.println(new String(verifyMacAndDecipherData(enc_key, mac_key,deviceid, cipheredData, 0, cipheredData.length-4, 32, cipheredData.length-4)));
//		String cipherText = generteSecureJson("jasonGson", Hex.toHex(enc_key), Hex.toHex(mac_key), Hex.toHex(deviceid));
//		System.out.println(cipherText);
//		String plainJson = verifyMacAndRetrieveJson(cipherText, Hex.toHex(enc_key), Hex.toHex(mac_key), Hex.toHex(deviceid));
//		System.out.println(plainJson);
//	}
}
