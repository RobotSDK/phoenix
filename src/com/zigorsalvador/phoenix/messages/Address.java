package com.zigorsalvador.phoenix.messages;

import com.zigorsalvador.phoenix.utilities.AddressFinder;

public class Address implements Comparable<Address>
{
	private String alias;	
	private String host;
	private Integer port;
 
	//////////
	
	public Address () {} // NOTE: Empty constructor...
	
	//////////
	
	public Address(String alias)
	{
		this.alias = alias;		
		this.host = AddressFinder.host();
		this.port = AddressFinder.port();
	}
	
	//////////
	
	public Address(String alias, Integer port)
	{
		this.alias = alias;		
		this.host = AddressFinder.host();
		this.port = port;
	}
	
	//////////
	
	public Address(String alias, String host, Integer port) 
	{
		try 
		{
			this.alias = alias;		
			this.host = host;
			this.port = port;
		}
		catch (Exception exception)
		{
        	System.out.println(exception);
        	System.exit(1);
		}
	}
	
	//////////

	public String getAlias()
	{
		return alias;
	}
	
	//////////
	
	public String getHost()
	{
		return host;
	}
	
	//////////
	
	public Integer getPort()
	{
		return port;
	}
	
	//////////	
	 
	public void setAlias(String alias) 
	{
		this.alias = alias;
	}
	
	//////////	

	public void setHost(String host) 
	{
		this.host = host;
	}

	//////////	
	
	public void setPort(Integer port) 
	{
		this.port = port;
	}

	//////////
		
	@Override
	
	public int compareTo(Address address) // NOTE: Comparable...
	{
		return toString().compareTo(address.toString());
	}
	
	//////////
	
	public boolean equals(Object object) // NOTE: Equals...
	{
		if (object == this)
		{
			return true;
		}
		
		if (object instanceof Address)
		{
			Address address = (Address) object;
			
			return ((alias.equals(address.alias) && host.equals(address.host) && port.equals(address.port)));
		}
		
		return false;
	}
	
	//////////
	
	public int hashCode() // NOTE: Hash...
	{	
		int result = 17;	
		
		result = 31 * result + (alias == null ? 0 : alias.hashCode());
		result = 31 * result + (host == null ? 0 : host.hashCode());
		result = 31 * result + (port == null ? 0 : port.hashCode());
	
		return result;
	}
	
	//////////
	
	public String toString() // NOTE: String...
	{
		String output = new String();
		
		output = output + alias;
		output = output + ":";
		output = output + host;
		output = output + ":";
		output = output + port;
		
		return output;		
	}
}