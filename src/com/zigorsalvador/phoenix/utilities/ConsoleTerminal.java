package com.zigorsalvador.phoenix.utilities;

import java.util.Vector;

import com.zigorsalvador.phoenix.interfaces.ITerminal;

public class ConsoleTerminal implements ITerminal
{	
	public ConsoleTerminal () {}
	
	//////////
	
	@Override
	
	public void setAmountOfFilters(Integer value) {}

	//////////
	
	@Override
	
	public void setAmountOfClients(Integer value) {}

	//////////
	
	@Override
	
	public void increaseIncomingEvents(Integer delta) {}

	//////////
	
	@Override
	
	public void increaseOutgoingEvents(Integer delta) {}

	//////////
	
	@Override
	
	public void increaseIncomingSignals(Integer delta) {}

	//////////
	
	@Override
	
	public void increaseOutgoingSignals(Integer delta) {}

	//////////
	
	@Override
	
	public void increasePositiveMatches(Integer delta) {}

	//////////
	
	@Override

	public void increaseNegativeMatches(Integer delta) {}
	
	@Override

	public void setLocalBrokers(Vector<Vector<Object>> rows) {}
	
	//////////

	@Override
	
	public void setLocalSubscribers(Vector<Vector<Object>> rows) {}

	//////////
	
	@Override
	
	public void setGlobalSubscriptions(Vector<Vector<Object>> rows) {}
}