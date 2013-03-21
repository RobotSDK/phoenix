package com.zigorsalvador.phoenix.messages;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL) // NOTE: Jackson annotation...

public class Event
{
	private Receipt receipt;
	private Location location;
	private TreeMap<String, Value> values;
	private Set<String> digests;
	
	//////////
	
	public Event () // NOTE: Empty constructor...
	{
		values = new TreeMap<String, Value>();
		digests = new HashSet<String>();
	} 
	
	//////////
		
	public Receipt getReceipt()
	{
		return receipt;
	}

	//////////
	
	public Location getLocation()
	{
		return location;
	}
	
	//////////
		
	public Map<String, Value> getValues()
	{
		return values;
	}
	
	//////////
	
	public Set<String> getDigests()
	{
		return digests;
	}
	
	//////////
	
	public void setReceipt(Receipt receipt)
	{
		this.receipt = receipt;
	}
	
	//////////
	
	public void setLocation(Location location)
	{
		this.location = location;
	}
	
	//////////
	
	public void addValue(Value value)
	{
		values.put(value.getName(), value);
	}
	
	//////////
	
	public void setDigests(Set<String> digests)
	{
		this.digests = digests;
	}
		
	//////////
	
	public boolean equals(Object object) // NOTE: Equals...
	{
		if (object == this)
		{
			return true;
		}
		
		if (object instanceof Event)
		{
			Event event = (Event) object;
		
			return (receipt.equals(event.receipt) && location.equals(event.location) && values.entrySet().equals(event.values.entrySet()) && digests.equals(event.digests));
		}

		return false;
	}
	
	//////////
	
	public int hashCode() // NOTE: Hash...
	{
		int result = 17;	
		
		result = 31 * result + (receipt == null ? 0 : receipt.hashCode());
		result = 31 * result + (location == null ? 0 : location.hashCode());
		result = 31 * result + (values == null ? 0 : values.hashCode());
		result = 31 * result + (digests == null ? 0 : digests.hashCode());
	
		return result;
	}
	
	//////////
	
	public String toString() // NOTE: String...
	{
		String output = new String();
		
		output = output + "[";
		output = output + receipt;
		output = output + "] [";
		output = output + location;
		output = output + " ";
		
		for (Value value : values.values())
		{			
			output = output + value;
			output = output + " ";
		}
		
		output = output.substring(0, output.length() - 1);
		
		output = output + "] ";
		output = output + digests;
		
		return output;
	}
}