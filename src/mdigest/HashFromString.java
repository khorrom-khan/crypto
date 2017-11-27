package mdigest;

import java.security.MessageDigest;
import java.util.Scanner;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class HashFromString {

	public static String hashGenerator(String inputString, String algorithom) throws NoSuchAlgorithmException  {
		
		String hashValue = "";
		MessageDigest md = MessageDigest.getInstance(algorithom);
		md.update(inputString.getBytes());
		byte[] b = md.digest();
		hashValue = DatatypeConverter.printHexBinary(b).toLowerCase();
		
		return hashValue;
	
	}
	
	public static void main(String[] args) {
		
		String input = "";
		Scanner s = new Scanner(System.in);
		System.out.println("Enter a String to generate MD5 and SHA hash: ");
		input = s.nextLine();
		try {
			System.out.println("MD5 Hash: "+hashGenerator(input, "MD5"));
			System.out.println("SHA Hash: "+hashGenerator(input, "SHA"));
		} catch (NoSuchAlgorithmException e) {}
	}

}
