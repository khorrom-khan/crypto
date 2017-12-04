package signature;
import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalAlice {
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d)
	{
		
		// compute y equals g to the power d mod p
		BigInteger y = g.modPow(d, p);
		return y;
	}
	
	private static BigInteger computeK(BigInteger p)
	{
		SecureRandom rnd = new SecureRandom();
		int numBits = 1024;
		
		//initialize k as a random biginteger of length=numBits 
		BigInteger k = new BigInteger(numBits, rnd);
		
		
		//generate p-1
		BigInteger pMinusOne = p.subtract(BigInteger.ONE); 
	
		//check "BigInteger k is relatively prime to BigInteger p-1
		while(!k.gcd(pMinusOne).equals(BigInteger.ONE))
		{
			k = new BigInteger(numBits, rnd);
		}	//loop will check until k is relatively prime to (p-1)
		
		return k;
	}
	
	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k)
	{
		// compute a equals to g to the power k mod p
		BigInteger a = g.modPow(k, p);
		return a;
	}

	
	//compute b from the equation m = (da + kb) mod p – 1
	private static BigInteger computeB(	String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p)
	{	
		//convert string message to BigInteger
		BigInteger m = new BigInteger(message.getBytes()); 
		
		//generate p-1
		BigInteger pMinusOne = p.subtract(BigInteger.ONE); 
					
		// b = ((m-da)*h) mod (p-1) where H= k.modInverse(p-1)
		BigInteger h = k.modInverse(pMinusOne);
		BigInteger b = m.subtract(d.multiply(a)).multiply(h).mod(pMinusOne);
		
		return b;
	}

	public static void main(String[] args) throws Exception 
	{
		String message = "The quick brown fox jumps over the lazy dog.";
		
		String host = "127.0.0.1";//local host IP address
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int numBits = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

		// Create a BigInterger with numBits bit length that is highly likely to be prime.
		// (The '16' determines the probability that p is prime. Refer to BigInteger documentation.)
		p = new BigInteger(numBits, 16, mSecureRandom);
		
		// Create a randomly generated BigInteger of length numBits-1
		g = new BigInteger(numBits-1, mSecureRandom);
		d = new BigInteger(numBits-1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now compute the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);
		
		// send signature
		os.writeObject(a);
		os.writeObject(b);
		
		s.close();
	}
}