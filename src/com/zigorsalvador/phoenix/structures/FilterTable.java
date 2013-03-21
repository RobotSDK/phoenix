package com.zigorsalvador.phoenix.structures;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import com.zigorsalvador.phoenix.interfaces.IMatcher;
import com.zigorsalvador.phoenix.interfaces.ITerminal;
import com.zigorsalvador.phoenix.matching.EventMatching;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.utilities.DigestFinder;

public class FilterTable implements IMatcher
{
	private ITerminal window;
	
	//////////
	
	private Map<Filter, TableNode> filters;
	private Map<String, TableNode> digests;

	//////////
	
	public FilterTable(ITerminal window)
	{
		this.window = window;
		
		filters = new ConcurrentSkipListMap<Filter, TableNode>();
		digests = new ConcurrentSkipListMap<String, TableNode>();
	}
	
	//////////
	
	@Override
	
	public void insert(Filter filter, Address subscriber) 
	{
		TableNode node;
		
		if (filters.containsKey(filter))
		{
			node = filters.get(filter);
			node.getSubscribers().add(subscriber);
		}
		else
		{
			node = new TableNode(filter, subscriber);
		}
		
		filters.put(filter, node);		
		digests.put(DigestFinder.digest(filter), node);
		window.setAmountOfFilters(filters.size());
	}

	//////////
	
	@Override
	
	public void remove(Filter filter, Address subscriber) 
	{
		TableNode node;
		
		if (filters.containsKey(filter))
		{
			node = filters.get(filter);
			node.getSubscribers().remove(subscriber);

			if (node.getSubscribers().isEmpty())
			{
				filters.remove(filter);
				digests.remove(DigestFinder.digest(filter));			
				window.setAmountOfFilters(filters.size());
			}
			else
			{
				filters.put(filter, node);
				digests.put(DigestFinder.digest(filter), node);
			}
		}
	}
	
	//////////
	
	@Override
	
	public void swap(Set<Filter> subscriptions, Address oldAddress, Address newAddress)
	{
		for (TableNode node : filters.values())
		{
			if (subscriptions.contains(node.getFilter()))
			{
				node.getSubscribers().remove(oldAddress);
				node.getSubscribers().add(newAddress);
			}
		}
	}	

	//////////
	
	@Override
	
	public Set<Address> matchingSubscribers(Event event, Set<Address> interfaces) 
	{
		Set<Address> result = new HashSet<Address>();
		
		for (TableNode node : filters.values())
		{
			List<Address> subscribers = node.getSubscribers();
			
			if (result.containsAll(subscribers) == false) // NOTE: Optimization...
			{
				if (EventMatching.matches(event, node.getFilter()))
				{
					window.increasePositiveMatches(1); 
				
					result.addAll(subscribers);
					interfaces.removeAll(subscribers);
					
					if (interfaces.isEmpty()) // NOTE: Optimization...
					{
						return result;
					}
				}
				else
				{
					window.increaseNegativeMatches(1);
				}
			}
		}
		
		return result;
	}
		
	//////////
		
	@Override
	
	public Set<String> matchingDigests(Event event) 
	{
		Set<String> result = new HashSet<String>();
		
		for (TableNode node : filters.values())
		{
			if (EventMatching.matches(event, node.getFilter()))
			{
				result.add(node.getDigest());
				window.increasePositiveMatches(1); 
			}
			else
			{
				window.increaseNegativeMatches(1);
			}
		}
		
		return result;
	}
	
	//////////
	
	@Override
	
	public Set<Address> matchedSubscribers(Set<String> digests, Set<Address> interfaces)
	{
		Set<Address> result = new HashSet<Address>();
		
		for (String digest : digests)
		{
			TableNode node = this.digests.get(digest);
			
			if (node != null)
			{
				List<Address> subscribers = node.getSubscribers();
				
				result.addAll(subscribers);
				interfaces.removeAll(subscribers);
				if (interfaces.isEmpty()) return result;
			}
		}
		
		return result;
	}
}