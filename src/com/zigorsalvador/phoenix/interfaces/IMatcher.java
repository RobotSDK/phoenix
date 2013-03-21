package com.zigorsalvador.phoenix.interfaces;

import java.util.Set;

import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Filter;


public interface IMatcher
{
	public void insert(Filter filter, Address subscriber);
	public void remove(Filter filter, Address subscriber);
	public void swap(Set<Filter> subscriptions, Address oldAddress, Address newAddress);
	public Set<Address> matchingSubscribers(Event event, Set<Address> interfaces);
	public Set<String> matchingDigests(Event event);
	public Set<Address> matchedSubscribers (Set<String> digests, Set<Address> interfaces);
}