package com.volcanicplaza.minetrends;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Encryption {

	//Get the server identification key / public key.
	public static String getServerKey() {
		String key = Minetrends.key;
		String privateKey = getPrivateKey();
		if (privateKey != null) {
			key = key.replace(privateKey, "");
		}
		return key;

	}

	//Get the private key / key to authenticate with.
	public static String getPrivateKey() {
		if (Minetrends.key.length() == 48) {
			return Minetrends.key.substring(0, 16);
		} else {
			return null;
		}

	}

	public static String encryptString(String value) {
		if (Minetrends.privateKey == null) {
			//No private key. Just fallback and send non encrypted data.
			return value;
		} else {
			String ENCRYPTION_KEY = Minetrends.privateKey;
			String result = null;

			try {
				byte[] input = value.getBytes("utf-8");

				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] thedigest = md.digest(ENCRYPTION_KEY.getBytes("UTF-8"));
				SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.ENCRYPT_MODE, skc);

				byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
				int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
				ctLength += cipher.doFinal(cipherText, ctLength);

				result = Base64.encodeBase64String(cipherText);
			} catch (Exception ex) {
				System.err.println(ex);
			}

			return result;
		}
	}

}
