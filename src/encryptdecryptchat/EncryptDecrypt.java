package encryptdecryptchat;

/*
 * @author Armando Vucic
 * 
 * This is a class for encrypting and decrypting messages for chat
 * using AES algorithm and 128-bit encryption
 * 
 */
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class EncryptDecrypt {
	// variable for key type String
	private String secretKey;

	// constructor that initializes key
	public EncryptDecrypt(String secretKey) {
		this.secretKey = secretKey;
	}

	/*
	 * Method for encrypting the message for Chat
	 * 
	 * @params String
	 */
	public String encrypt(String message) {
		// setting cipher to ENCRYPT MODE
		Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
		// initializing encrypted message in byte[]
		byte[] encryptedMsg = null;
		/*
		 * In try/catch block doFinal method is encrypting the message in byte format and
		 * catching exceptions
		 */
		try {
			encryptedMsg = cipher.doFinal(message.getBytes());
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		// returns the encrypted message by using Base64 encode
		return Base64.encodeBase64String(encryptedMsg);
	}

	/*
	 * Method for encrypting the message for Chat
	 * 
	 * @params String
	 */
	public String decrypt(String encrypted) {
		// setting cipher on DECRYPT MODE
		Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
		// initializing decrypted message in byte[]
		byte[] decryptedMsg = null;
		/*
		 * In try/catch block doFinal method is decrypting the message with
		 * Base64 decode and catching exceptions *
		 */
		try {
			decryptedMsg = cipher.doFinal(Base64.decodeBase64(encrypted));
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		// returning the decrypted message in string format human readable
		return new String(decryptedMsg);
	}

	/*
	 * Cipher method is for determine which mode is needed for message for
	 * Encrypt or Decrypt
	 */
	private Cipher getCipher(int cipherMode) {
		// setting algorithm for encrypting and decrypting
		String algorithm = "AES";
		// initializing the SecretKeySpec and Cipher
		SecretKeySpec keySpec;
		Cipher cipher = null;

		try {
			// setting SecretKeySpec secretkey which is created by the client
			// and setting algorithm for that key
			keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), algorithm);
			// returns a Cipher object that implements the specified algorithm
			cipher = Cipher.getInstance(algorithm);
			// initializes cipher with specific mode (Encrypt/Decrypt) and
			// secretkey
			cipher.init(cipherMode, keySpec);
		} catch (UnsupportedEncodingException | InvalidKeyException
				| NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		// @return cipher
		return cipher;
	}
}
