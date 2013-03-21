package com.zigorsalvador.phoenix.structures;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.utilities.DigestFinder;

public class TableNode
{
	private Filter filter;
	private String digest;

	private List<Address> subscribers;
	
	//////////
	
	public TableNode(Filter filter, Address subscriber) 
	{
		this.filter = filter;
	 	
	 	subscribers = new CopyOnWriteArrayList<Address>();
		
		subscribers.add(subscriber);
		
	   	digest = DigestFinder.digest(filter);
	}
	
	//////////
	
	public Filter getFilter()
    {
    	return filter;
    }
	
	//////////
	
	public String getDigest()
	{
		return digest;
	}
       
    //////////
    
    public List<Address> getSubscribers()
    {
    	return subscribers;
    }
}