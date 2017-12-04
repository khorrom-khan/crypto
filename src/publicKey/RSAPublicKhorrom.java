package publicKey;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;
public class RSAPublicKhorrom {

	public static void main(String[] args) throws Exception {

	    int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		

		ObjectInputStream inStream = new ObjectInputStream(client.getInputStream());
		ObjectOutputStream outStream = new ObjectOutputStream(client.getOutputStream());
		
		// Generate Khorrom's key pairs (private key and public key)
		KeyPairGenerator genKeyPair = KeyPairGenerator.getInstance("RSA");
	    genKeyPair.initialize(1024, new SecureRandom()); 
	    KeyPair keyPair = genKeyPair.genKeyPair();
	    RSAPublicKey khorromPublicKey = (RSAPublicKey) keyPair.getPublic();
	    RSAPrivateKey khorromPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
	    
	    // Send Khorrom's public key to Anand
	    outStream.writeObject(khorromPublicKey);
	    
	    // Get Anand's public key 
	    RSAPublicKey anandPublicKey = (RSAPublicKey) inStream.readObject();
	    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    
	    int option = inStream.readInt();
	    if(option==1) {
				  //Confidentiality: Decipher using Khorrom's private key 
				  System.out.println("Message was enciphered by receiver's(Khorrom) public  key and deciphered by receiver's(Khorrom) private key");
				  System.out.println("@@@@@@@@ Confidentiality Achived @@@@@@@");
				  
				  byte[] receivedCipher = (byte[]) inStream.readObject();
				  cipher.init(Cipher.DECRYPT_MODE, khorromPrivateKey);
 			      byte[] decipheredMessage = cipher.doFinal(receivedCipher);
				  System.out.println("The deciphered message is: " + new String(decipheredMessage));
		  
	    } else if(option==2) { 				  
				  // Confidentiality and Integrity: Decipher using Anand's public key 
				  System.out.println("Message was enciphered by sender's(Anand) private key and deciphered by sender's(Anand) public key");
				  System.out.println("@@@@@@@@ Confidentiality as well as Integrity achived @@@@@@@@");
				  byte[] receivedCipher = (byte[]) inStream.readObject();

				  cipher.init(Cipher.DECRYPT_MODE, anandPublicKey);
 			      byte[] decipheredMessage = cipher.doFinal(receivedCipher);
				  System.out.println("The deciphered message is: " + new String(decipheredMessage));
				  		
			}
		 
		}
		
	}