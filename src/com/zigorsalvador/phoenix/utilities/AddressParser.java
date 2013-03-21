package com.zigorsalvador.phoenix.utilities;

import com.zigorsalvador.phoenix.messages.Address;

public class AddressParser 
{
	public static Address address(String address) 
	{
		try
		{
			String[] tokens = address.split(":");
			
			if (tokens.length == 3)
			{
				String alias = tokens[0];
				String host = tokens[1];
				Integer port = Integer.parseInt(tokens[2]);
			
				return new Address(alias, host, port);
			}
		}
		catch (Exception exception)
		{
        	System.out.println(exception);
        	System.exit(1);
		}	
		
		return null;
	}
}