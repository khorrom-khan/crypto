
package encryption;
import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;
import javax.swing.plaf.SliderUI;

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
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("keyFile.dat")));
		out.writeObject(key);	
		out.close();
	    
		//Encrypts given String object using key and sends the encrypted object over the socket to the server
		Socket s = new Socket(host, port);	
		
	    Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	    CipherOutputStream cipherOut = new CipherOutputStream(s.getOutputStream(), cipher);
		cipher.init(Cipher.ENCRYPT_MODE, key); //sets the cipher object to Encrypt mode with the specified key k 
	    byte input[] = message.getBytes();
	    cipherOut.write(input, 0, input.length);
	    cipherOut.close();
  
	    Thread.sleep(4000);
	    s.close();

	    
	}
}