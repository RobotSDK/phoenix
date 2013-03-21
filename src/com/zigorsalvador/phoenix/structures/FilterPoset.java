package com.zigorsalvador.phoenix.structures;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import com.zigorsalvador.phoenix.interfaces.IMatcher;
import com.zigorsalvador.phoenix.interfaces.ITerminal;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.utilities.DigestFinder;

public class FilterPoset implements IMatcher
{
	private ITerminal window;

	//////////
	
	private Set<PosetNode> roots;
	private Map<Filter, PosetNode> filters;
	private Map<String, PosetNode> digests;
	
	//////////
	
	public FilterPoset(ITerminal window)
	{
		this.window = window;
		
		roots = new ConcurrentSkipListSet<PosetNode>();
		filters = new ConcurrentHashMap<Filter, PosetNode>();
		digests = new ConcurrentHashMap<String, PosetNode>();
	}
	  
	//////////
	
	@Override
	
	public void insert(Filter filter, Address subscriber)
	{
		if (filters.containsKey(filter))
		{
			PosetNode node = filters.get(filter);
			node.getSubscribers().add(subscriber);
		}
		else 
		{
			PosetNode node = new PosetNode(filter, subscriber);
			
			Set<PosetNode> covering = new HashSet<PosetNode>();
			Set<PosetNode> covered = new HashSet<PosetNode>();
			
			for (PosetNode root : roots)
			{
				covering.addAll(PosetTools.directCoveringNodes(root, filter));
				covered.addAll(PosetTools.directCoveredNodes(root, filter));
			}
			
			if (covering.isEmpty())
			{
				roots.add(node);
			}
			else
			{
				for (PosetNode alpha : covering)
				{
					alpha.getOmegas().add(node);
					node.getAlphas().add(alpha);
				}
			}
			
			for (PosetNode omega : covered)
			{
				if (omega.isRoot())
				{
					roots.remove(omega);
				}
							
				omega.getAlphas().add(node);
				node.getOmegas().add(omega);
			}
			
			for (PosetNode alpha : covering)
			{
				for (PosetNode omega : covered)
				{
					if (alpha.getOmegas().contains(omega) && omega.getAlphas().contains(alpha))
					{
						alpha.getOmegas().remove(omega);
						omega.getAlphas().remove(alpha);
					}
				}
			}
			
			filters.put(node.getFilter(), node);
			digests.put(DigestFinder.digest(filter), node);
			window.setAmountOfFilters(filters.size());
		}
	}
	
	//////////
		
	@Override
	
	public void remove(Filter filter, Address subscriber)
	{
		if (filters.containsKey(filter))
		{
			PosetNode node = filters.get(filter);			
			node.getSubscribers().remove(subscriber);
			
			if (node.getSubscribers().isEmpty())
			{
				if (node.isRoot()) 
				{
					roots.remove(node);
									
					for (PosetNode omega : node.getOmegas())
					{						
						omega.getAlphas().remove(node);	
						
						if (omega.isRoot())
						{
							roots.add(omega);
						}
					}					
				}
				else if (node.isLeaf()) 
				{
					for (PosetNode alpha : node.getAlphas())
					{
						alpha.getOmegas().remove(node);
					}
				}
				else 
				{
					for (PosetNode alpha : node.getAlphas())
					{
						alpha.getOmegas().remove(node);
					
						for (PosetNode omega : node.getOmegas())
						{
							omega.getAlphas().remove(node);
							
							if (PosetTools.subposetNodes(alpha).contains(omega) == false)
							{
								alpha.getOmegas().add(omega);
								omega.getAlphas().add(alpha);
							}
						}
					}
				}
				
				filters.remove(filter);
				digests.remove(DigestFinder.digest(filter));
				window.setAmountOfFilters(filters.size());
			}
		}
	}
		
	//////////
		
	@Override
	
	public void swap(Set<Filter> subscriptions, Address oldAddress, Address newAddress)
	{
		for (PosetNode node : filters.values())
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
		
		for (PosetNode root : roots)
		{
			Set<Address> subResult = PosetTools.matchingSubscribers(root, event, interfaces, window);
			
			result.addAll(subResult);
			interfaces.removeAll(subResult);
			
			if (interfaces.isEmpty()) // NOTE: Optimization...
			{
				return result;
			}
		}
		
		return result;
	}
		
	//////////
		
	@Override
	
	public Set<String> matchingDigests(Event event)
	{
		Set<String> result = new HashSet<String>();
		
		for (PosetNode root : roots)
		{
			result.addAll(PosetTools.matchingDigests(root, event, window));
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
			PosetNode node = this.digests.get(digest);
			
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