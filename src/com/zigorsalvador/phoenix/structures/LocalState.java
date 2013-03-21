package com.zigorsalvador.phoenix.structures;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.mina.util.ConcurrentHashSet;

import com.zigorsalvador.phoenix.interfaces.ITerminal;
import com.zigorsalvador.phoenix.messages.Address;

public class LocalState 
{
	private ITerminal window;
	
	//////////
	
	private Set<Address> brokers;
	private Set<Address> subscribers;
	
	//////////
	
	public LocalState(ITerminal window)
	{
		this.window = window;
		
		brokers = new ConcurrentHashSet<Address>();
		subscribers = new ConcurrentHashSet<Address>();
	}
	
	//////////
	
	public void addBroker(Address broker)
	{
		if (brokers.add(broker)) updateLocalBrokerTable();
	}
	
	//////////
	
	public Boolean containsBroker(Address broker)
	{
		return brokers.contains(broker);
	}
	
	//////////
	
	public void removeBroker(Address broker)
	{
		if (brokers.remove(broker)) updateLocalBrokerTable();
	}
	
	//////////
	
	public Set<Address> getBrokers()
	{
		return Collections.unmodifiableSet(brokers);
	}
	
	//////////
	
	public void addSubscriber(Address subscriber)
	{
		if (subscribers.add(subscriber)) updateLocalSubscriberTable();
	}
	
	//////////
	
	public Boolean containsSubscriber(Address subscriber)
	{
		return subscribers.contains(subscriber);
	}
	
	//////////
	
	public void removeSubscriber(Address subscriber)
	{
		if (subscribers.remove(subscriber))	updateLocalSubscriberTable();
	}
	
	//////////
	
	public Set<Address> getSubscribers()
	{
		return Collections.unmodifiableSet(subscribers);
	}
	
	//////////
	
	public Set<Address> getInterfaces()
	{
		Set<Address> output = new HashSet<Address>();
		
		output.addAll(brokers);
		output.addAll(subscribers);
		
		return output;
	}
	
	//////////
	
	public void updateLocalBrokerTable()
	{
		Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
		
		for (Address address : new TreeSet<Address>(brokers))
		{
			Vector<Object> row = new Vector<Object>();
			row.add(address);
			rows.add(row);
		}
		
		window.setLocalBrokers(rows);
	}
	
	//////////
	
	public void updateLocalSubscriberTable()
	{
		Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
		
		for (Address address : new TreeSet<Address>(subscribers))
		{
			Vector<Object> row = new Vector<Object>();
			row.add(address);
			rows.add(row);
		}
		
		window.setLocalSubscribers(rows);
	}
}