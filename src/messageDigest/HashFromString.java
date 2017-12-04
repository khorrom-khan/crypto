package messageDigest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

public class HashFromString {
public static String hashGenerator(String inputString, String algorithom) throws NoSuchAlgorithmException  {
		
		String hashValue = "";
		
		//digest the input string using provided algorithom
		MessageDigest md = MessageDigest.getInstance(algorithom);
		md.update(inputString.getBytes());
		byte[] b = md.digest();
		
		//generate hashvalue
		hashValue = DatatypeConverter.printHexBinary(b).toLowerCase();
		
		return hashValue;
		
	
	}
	
	public static void main(String[] args) {
		
		String input = "";
		Scanner s = new Scanner(System.in);
		System.out.println("Enter a String to generate MD5 and SHA hash: ");
		input = s.nextLine();
		
		//call hashGenerator for MD5 and SHA
		try {
			System.out.println("MD5 Hash: "+hashGenerator(input, "MD5"));
			System.out.println("SHA Hash: "+hashGenerator(input, "SHA"));
		} catch (NoSuchAlgorithmException e) {}
	}

}
