package com.zigorsalvador.phoenix.structures;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.utilities.DigestFinder;

public class PosetNode implements Comparable<PosetNode>
{
    private Filter filter;
    private String digest;
	
    private Set<PosetNode> alphas;
    private Set<PosetNode> omegas;
   
    private List<Address> subscribers;

    //////////
    
    public PosetNode(Filter filter, Address subscriber) 
    {
     	this.filter = filter;
     	
     	alphas = new ConcurrentSkipListSet<PosetNode>();
     	omegas = new ConcurrentSkipListSet<PosetNode>();
       	
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
    
    public Set<PosetNode> getAlphas()
    {
    	return alphas;
    }
    
    //////////
    
    public Set<PosetNode> getOmegas()
    {
    	return omegas;
    }
    
    //////////
    
    public List<Address> getSubscribers()
    {
    	return subscribers;
    }
    
    //////////
    
    public Boolean isRoot()
    {
    	return alphas.isEmpty();
    }
    
    //////////
    
    public Boolean isLeaf()
    {
    	return omegas.isEmpty();
    }
    
	//////////
		
	@Override
	
	public int compareTo(PosetNode node) // NOTE: Comparable...
	{
		return filter.compareTo(node.getFilter());
	}
}