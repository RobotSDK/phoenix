package com.zigorsalvador.phoenix.examples;

import com.zigorsalvador.phoenix.components.Subscriber;
import com.zigorsalvador.phoenix.constants.Messages;
import com.zigorsalvador.phoenix.discovery.DiscoveryManager;
import com.zigorsalvador.phoenix.interfaces.IListener;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Constraint;
import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.messages.Predicate;
import com.zigorsalvador.phoenix.messages.Relation;
import com.zigorsalvador.phoenix.messages.Type;
import com.zigorsalvador.phoenix.utilities.MessageLogger;
import com.zigorsalvador.phoenix.utilities.SimpleSleeper;

public class SimpleSubscriber implements IListener
{
	private MessageLogger logger;

	//////////

	public SimpleSubscriber()
	{
		Address broker = DiscoveryManager.discover();

		if (broker != null)
		{
			Address client = new Address("subscriber");			
			Subscriber subscriber = new Subscriber(client, broker, this);
			logger = new MessageLogger(client.getAlias());

			Filter subscription = new Filter();
			subscription.addPredicate(new Predicate(Type.INT, "counter", new Constraint(Relation.ANY)));
			subscriber.subscribe(subscription);

			SimpleSleeper.sleepSeconds(100);

			subscriber.unsubscribe(subscription);

			subscriber.terminate();
		}
	}
	
	//////////

	@Override
	
	public void notify(Event event, Boolean replay) 
	{
		logger.println(Messages.RECEIVED_PUBLICATION + event);
	}
	
	//////////
	
	public static void main(String[] args)
	{
		new SimpleSubscriber();
	}
}