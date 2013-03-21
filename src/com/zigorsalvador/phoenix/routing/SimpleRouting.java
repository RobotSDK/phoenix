package com.zigorsalvador.phoenix.routing;

import java.util.Set;

import com.zigorsalvador.phoenix.interfaces.ITerminal;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Message;
import com.zigorsalvador.phoenix.messages.Method;
import com.zigorsalvador.phoenix.transport.BrokerPacketReceiver;
import com.zigorsalvador.phoenix.transport.BrokerPacketSender;

public class SimpleRouting extends AbstractRouting
{
	public SimpleRouting(Address address, ITerminal window, BrokerPacketSender sender, BrokerPacketReceiver receiver)
	{
		super(address, window, sender, receiver);
	}

	//////////
	
	public void processPublication(Message message) 
	{
		Address source = message.getSource();
		Event event = message.getEvent();
		
		// Construct matching forward set...
		
		Set<Address> interfaces = localState.getInterfaces();
		Set<Address> forward = routingTable.matchingSubscribers(event, interfaces);	
			
		forward.remove(source);
		
		// Forward publication message...
		
		for (Address target : forward)
		{																				
			if (localState.containsSubscriber(target))
			{
				sender.enqueue(new Message(Method.PUB, address, target, null, event), target);
			}
			
			sender.send(new Message(Method.PUB, address, target, null, event), target);  				
		}
	}
}