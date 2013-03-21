package com.zigorsalvador.phoenix.utilities;

import java.math.BigInteger;
import java.security.MessageDigest;

import com.zigorsalvador.phoenix.messages.Filter;

public class DigestFinder
{
	public static String digest(Filter filter)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
		    byte[] md5Digest = md.digest(filter.toString().getBytes("UTF-8"));
		    BigInteger md5Number = new BigInteger(1, md5Digest);
		    String md5String = md5Number.toString(16);
		        
		    return md5String;
		}
		catch (Exception exception)
		{
			System.out.println(exception);
			System.exit(1);
		}
		
		return null;
	}
}