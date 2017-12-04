package publicKey;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Scanner;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


import javax.crypto.Cipher;


public class RSAPublicAnand {
	
	
	public static void main(String[] args) throws Exception {
      
		String host = "127.0.0.1";
		int port = 7999;
		Socket s = new Socket(host, port);
		
		ObjectOutputStream outStream = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream inStream = new ObjectInputStream(s.getInputStream());
		
		// Generate Anands's key pairs (private key and public key)
		KeyPairGenerator generateKeyPair = KeyPairGenerator.getInstance("RSA");
		generateKeyPair.initialize(1024 ,new SecureRandom());
	    KeyPair keyPair = generateKeyPair.genKeyPair();
	    RSAPublicKey anandPublicKey = (RSAPublicKey) keyPair.getPublic();
	    RSAPrivateKey anandPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
	    
	    // Send Anand's public key to Khorrom 
	    outStream.writeObject(anandPublicKey);
	    
	    // Get Khorrom's public key 
	    RSAPublicKey khorromPublicKey = (RSAPublicKey) inStream.readObject();
	    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    
        System.out.println("Which kind of service do you want? Enter 1 for Confidentiality, 2 for Confidentiality + Integrity: ");
		Scanner scanner = new Scanner(System.in);
		
	    int option= scanner.nextInt();
	    
	    Boolean flag=true;
	    while(flag) {
	    	if (option==1) {
				  //Confidentiality: encipher by receiver's(Khorrom) public key

				  System.out.println("Confidentiality: Message will be enciphered by receiver's public key ");
				  Scanner scanMessage = new Scanner(System.in);
				  System.out.println("Please enter the message you want to encipher: ");
				  String message= scanMessage.nextLine();
				  cipher.init(Cipher.ENCRYPT_MODE, khorromPublicKey);
 			      byte[] cipherText = cipher.doFinal(message.getBytes());
				  
 			      outStream.writeInt(1);
				  outStream.writeObject(cipherText);
				  outStream.flush();
				  outStream.close();
				  flag=false;
				
	    	} else if(option==2){ 
				  //Confidentiality and Integrity: encipher by sender's(Anand) private key 

				  System.out.println("Confidentiality and Integrity: Message will be enciphered by sender's public key ");
				  Scanner scanMessage = new Scanner(System.in);
				  System.out.println("Please enter the message you want to encipher: ");
				  String message= scanMessage.nextLine();
				  cipher.init(Cipher.ENCRYPT_MODE, anandPrivateKey);
 			      byte[] cipherText = cipher.doFinal(message.getBytes());
 			      
				  outStream.writeInt(2);
				  outStream.writeObject(cipherText);
				  outStream.flush();
				  outStream.close();
				  flag=false;
				  
			}else {
				
				System.out.println("Invalid input");
				System.out.println("Which kind of service do you want? Enter 1 for Confidentiality, 2 for Confidentiality + Integrity: ");
				option= scanner.nextInt();				
			
			}
		}
		   
		s.close();
		scanner.close();
	}

	    
	    
}

	