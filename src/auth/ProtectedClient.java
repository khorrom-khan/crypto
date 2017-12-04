package auth;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;

public class ProtectedClient {
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException
	{
		DataOutputStream out = new DataOutputStream(outStream);//data output stream to put data into buffer
		
		//Time Stamp declaration
		
		long ts1 = (new Date()).getTime();
		long ts2 = (new Date()).getTime();
		
		//Two random_namber declaration
		double randNum1 = Math.random();
		double randNum2 = Math.random();
		
		// digesttwo times for double strength and store into byteArrays
		byte[] digest1 = Protection.makeDigest(user, password, ts1, randNum1);
		byte[] digest2 = Protection.makeDigest(digest1, ts2, randNum2);
		
		// write into data output stream
		out.writeUTF(user); 
		out.writeLong(ts1);
		out.writeLong(ts2);
		out.writeDouble(randNum1);
		out.writeDouble(randNum2);
		out.writeInt(digest1.length); 
		out.write(digest2);
				
		out.flush();
	}
	
	public static void main(String[] args) throws Exception
	{
		String host = "127.0.0.1";//The IP address of localhost
		int port = 7999;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);
		
		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());
		
		s.close();
	}

}