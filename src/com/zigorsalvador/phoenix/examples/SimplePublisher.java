package com.zigorsalvador.phoenix.examples;

import com.zigorsalvador.phoenix.components.Publisher;
import com.zigorsalvador.phoenix.constants.Messages;
import com.zigorsalvador.phoenix.discovery.DiscoveryManager;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Type;
import com.zigorsalvador.phoenix.messages.Value;
import com.zigorsalvador.phoenix.utilities.MessageLogger;
import com.zigorsalvador.phoenix.utilities.SimpleSleeper;

public class SimplePublisher 
{
	public SimplePublisher()
	{
		Address broker = DiscoveryManager.discover();
		
		if (broker != null)
		{
			Event event = new Event();
			Address client = new Address("publisher");
			Publisher publisher = new Publisher(client, broker);
			MessageLogger logger = new MessageLogger(client.getAlias());

			for (int counter = 0; counter < 100; counter++)
			{
				event.addValue(new Value(Type.INT, "counter", counter));
				SimpleSleeper.sleepMilliseconds(1000);
				publisher.publish(event);
				
				logger.println(Messages.SENT_PUBLICATION + event);
			}
			
			publisher.terminate();
		}
	}
	
	//////////
	
	public static void main(String[] args)
	{
		new SimplePublisher();
	}
}