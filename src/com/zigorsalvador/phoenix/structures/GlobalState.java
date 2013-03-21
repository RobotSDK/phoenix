package com.zigorsalvador.phoenix.structures;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.zigorsalvador.phoenix.interfaces.ITerminal;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.utilities.DigestFinder;

public class GlobalState
{
	private ITerminal window;
	
	//////////
	
	private Map<Address, Address> interfaces;
	private Map<Address, Set<Filter>> subscriptions;
	
	//////////
	
	public GlobalState(ITerminal window)
	{
		this.window = window;
	
		interfaces = new ConcurrentHashMap<Address, Address>();
		subscriptions = new ConcurrentHashMap<Address, Set<Filter>>();
	}
	
	//////////
	
	public void addSubscription(Address subscriber, Filter subscription)
	{
		Set<Filter> active;
		
		if (subscriptions.containsKey(subscriber))
		{
			active = subscriptions.get(subscriber);
		}
		else
		{
			active = new HashSet<Filter>();
		}

		active.add(subscription);
		
		subscriptions.put(subscriber, active);
		
		updateGlobalSubscriptionsTable();
		window.setAmountOfClients(subscriptions.keySet().size());
	}
	
	//////////
	
	public void removeSubscription(Address subscriber, Filter subscription)
	{
		Set<Filter> active = subscriptions.get(subscriber);
		
		active.remove(subscription);
		
		if (active.isEmpty())
		{
			subscriptions.remove(subscriber);
		}
		else
		{
			subscriptions.put(subscriber, active);
		}
		
		updateGlobalSubscriptionsTable();
		window.setAmountOfClients(subscriptions.keySet().size());
	}
	
	//////////
	
	public Boolean hasSubscriptions(Address subscriber)
	{
		return subscriptions.containsKey(subscriber);
	}

	//////////
		
	public Set<Filter> getSubscriptions(Address subscriber)
	{
		return Collections.unmodifiableSet(subscriptions.get(subscriber));
	}
	
	//////////
	
	public void updateInterface(Address subscriber, Address address)
	{
		if (subscriptions.containsKey(subscriber))
		{
			interfaces.put(subscriber, address);
		}
		else
		{
			interfaces.remove(subscriber);
		}
		
		updateGlobalSubscriptionsTable();
	}

	//////////
		
	public Address getInterface(Address subscriber)
	{
		return interfaces.get(subscriber);
	}	
	
	//////////
	
	public void updateGlobalSubscriptionsTable()
	{
		Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
		
		for (Address address : new TreeSet<Address>(subscriptions.keySet()))
		{
			Set<Filter> filters = subscriptions.get(address);
			
			if (filters != null)
			{
				for (Filter filter : new TreeSet<Filter>(filters))
				{
					Vector<Object> row = new Vector<Object>();
							
					row.add(address);
					row.add(interfaces.get(address));
					row.add(DigestFinder.digest(filter));
					row.add(filter.toString());
							
					rows.add(row);
				}
			}
		}
		
		window.setGlobalSubscriptions(rows);
	}
}