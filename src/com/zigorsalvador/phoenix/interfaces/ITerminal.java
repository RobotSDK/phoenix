package com.zigorsalvador.phoenix.interfaces;

import java.util.Vector;

public interface ITerminal
{
	public void setAmountOfFilters(Integer value);
	public void setAmountOfClients(Integer value);
	public void increaseIncomingEvents(Integer delta);
	public void increaseOutgoingEvents(Integer delta);
	public void increaseIncomingSignals(Integer delta);
	public void increaseOutgoingSignals(Integer delta);
	public void increasePositiveMatches(Integer delta);
	public void increaseNegativeMatches(Integer delta);
	
	public void setLocalBrokers(Vector<Vector<Object>> rows);
	public void setLocalSubscribers(Vector<Vector<Object>> rows);
	public void setGlobalSubscriptions(Vector<Vector<Object>> rows);
}
