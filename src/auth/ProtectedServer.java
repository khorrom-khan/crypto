package auth;

import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer {
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException
	{
		DataInputStream in = new DataInputStream(inStream);//to get data from buffer
		
		//read username from data input stream and getting password using lookPassword method
		String user = in.readUTF();
		String password = lookupPassword(user);
		
		//read time stamps from data input stream
		long ts1 = in.readLong();
		long ts2 = in.readLong();
		
		//read random numbers from data input stream
		double randNum1 = in.readDouble();
		double randNum2 = in.readDouble();
		
		//read digest from data input stream and put into byte array
		int length = in.readInt();
		byte[] clientDigest = new byte [length];
		in.readFully(clientDigest);
		
		
		//generate digest in server side
		byte[] initDigestServer = Protection.makeDigest(user, password, ts1, randNum1);
		byte[] serverDigest = Protection.makeDigest(initDigestServer, ts2, randNum2);
		
		boolean authentication = true;
		//compare client and server digest
		authentication = MessageDigest.isEqual(clientDigest, serverDigest); 
		
		//true/false? if true authentication will be passed and client will be able to log in
		return authentication;
		
	}
	
	protected String lookupPassword(String user)
	{
		return "abc123";
	}
	
	public static void main(String[] args) throws Exception
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();//to accept the socket of client
		
		ProtectedServer server = new ProtectedServer();
		
		//call authenticate to check if passed		
		if (server.authenticate(client.getInputStream()))
			System.out.println("Client logged in.");
		else
			System.out.println("Client failed to log in.");
		
		s.close();
	}
}