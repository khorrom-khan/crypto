package x509;

import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class X509Server {

	public static void main(String[] args) throws Exception 
	{   
		int port = 5555;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		ObjectInputStream inStream = new ObjectInputStream(s.getInputStream());
		
		String name="keyAlias";
        char[] password="changeit".toCharArray();
		      
		   
		//Load keystore 
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(new FileInputStream("X509Project.jks"), password);
        
        //Get server's private key
        PrivateKey serverPrivateKey = (PrivateKey)keyStore.getKey(name, password);
        if(serverPrivateKey==null) {
        	System.out.println("Null key found from the keystore");
        }
       
        //Decipher received message using server's private key 
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] in = (byte[]) inStream.readObject();
		cipher.init(Cipher.DECRYPT_MODE, serverPrivateKey);
		byte[] message = cipher.doFinal(in);
		
		//Print the message
		System.out.println("The deciphered message is: " + new String(message));
		server.close();
	}

}