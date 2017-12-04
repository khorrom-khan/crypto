
package encryption;
import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

public class CipherClient
{
	public static void main(String[] args) throws Exception 
	{
		
		String message = "The quick brown fox jumps over the lazy dog.";
		System.out.println("The original message is: "+message);
		String host = "127.0.0.1";
		int port = 7999;
		
		//Generate a DES key
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		keyGen.init(new SecureRandom());
		Key key = keyGen.generateKey();
		
		//Store the key in a file
		ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(new File("keyFile.dat")));
		fileOut.writeObject(key);	
		fileOut.close();
	    
		//Encrypt given String using key and send the encrypted object over the socket to the server
		Socket s = new Socket(host, port);	
		ObjectOutputStream outStream = new ObjectOutputStream(s.getOutputStream());
	
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(message.getBytes());
        
        outStream.writeObject(cipherText);
		outStream.flush();
		outStream.close();
	    		
	    s.close();
	    
	}
}