package com.zigorsalvador.phoenix.discovery;

import com.zigorsalvador.phoenix.messages.Address;

public class DiscoveryManager
{
	private static Address multicastBroker;
	private static Address unicastBroker;	
	
	//////////
	
	public static Address discover()
	{	
		multicastBroker = MulticastSpeaker.discover();
			
		if (multicastBroker != null)
		{
			return multicastBroker;
		}
			
		unicastBroker = UnicastSpeaker.discover();
		
		if (unicastBroker != null)
		{
			return unicastBroker;
		}

		return null;
	}
}