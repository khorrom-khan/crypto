package x509;

import java.io.*;
import java.net.*;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Scanner;

import javax.crypto.*;

public class X509Client {

	public static void main(String[] args) throws Exception 
	{
		String host = "127.0.0.1";
		int port = 5555;
		Socket s = new Socket(host, port);
	    
		
		// Load the certificate
		ObjectOutputStream outStream = new ObjectOutputStream(s.getOutputStream());
        InputStream inStream = new FileInputStream("X509Project.cer");
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate)certFactory.generateCertificate(inStream);
        inStream.close();
        
     // Print the content of the certificate 
        System.out.println(certificate.toString());
        System.out.println("The detail of the certificate file is printed above.");
             
        // Check the validity of the certificate
        try {
			certificate.checkValidity();
			System.out.println("The certificate is valid from "+ certificate.getNotBefore()+ " to "+certificate.getNotAfter()) ;
			
		} catch (CertificateExpiredException e) {
			System.out.println("The certificate is expired.");
		} catch (CertificateNotYetValidException e) {
			System.out.println("The certificate is not yet valid.");
		} 
        
        
        
        // Get server's public key from the certificate
        RSAPublicKey serverPublicKey = (RSAPublicKey) certificate.getPublicKey();
        
        //Verify that the certificate was signed using the private key that corresponds to the specified public key
        certificate.verify(serverPublicKey);
        
        //Encipher a message using server's public key and send to server 
        System.out.println("Please enter your message: ");
        Scanner scanner = new Scanner(System.in);
        String message= scanner.nextLine();
        
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
        byte[] cipherText = cipher.doFinal(message.getBytes());
        
        outStream.writeObject(cipherText);
		outStream.flush();
		outStream.close();
	    s.close();
	    scanner.close();
	}



}