package com.zigorsalvador.phoenix.launchers;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

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
import com.zigorsalvador.phoenix.utilities.AddressHandler;
import com.zigorsalvador.phoenix.utilities.MessageLogger;
import com.zigorsalvador.phoenix.utilities.SimpleSleeper;

public class SubscriberLauncher implements IListener
{
	private MessageLogger logger;
	
	//////////
	
	@Option(name="-client")
    private Address client;
	
	@Option(name="-broker")
    private Address broker;
	
	@Option(name="-text")
	private String text;

	@Option(name="-time")
	private Integer time;
	
	@Option(name="-verbose", handler=BooleanOptionHandler.class)
    private boolean verbose;
	
	//////////
	
	public SubscriberLauncher(String[] args)
	{
		CmdLineParser.registerHandler(com.zigorsalvador.phoenix.messages.Address.class, AddressHandler.class);
		
		CmdLineParser parser = new CmdLineParser(this);
		
		try
		{
			parser.parseArgument(args);

			if (client == null) client = new Address("subscriber");
			if (broker == null) broker = DiscoveryManager.discover();
			if (time == null) time = Integer.MAX_VALUE;
			if (text == null) text = "good";
		}
		catch (Exception exception)
		{
			System.out.println(exception);
			System.exit(1);
		}
		
		Subscriber subscriber = new Subscriber(client, broker, this);
		logger = new MessageLogger(client.getAlias());
		
		Filter subscription = new Filter();
		subscription.addPredicate(new Predicate(Type.STR, "text", new Constraint(Relation.EQU, text)));
		subscriber.subscribe(subscription);
		
		SimpleSleeper.sleepSeconds(time);
		
		subscriber.unsubscribe(subscription);
		
		subscriber.terminate();
		
		System.exit(0);
	}

	//////////
	
	public void notify(Event event, Boolean replay) 
	{	
		if (verbose)
		{
			if (replay)	logger.println(Messages.RECEIVED_PUBLICATION + event + " [R]");
			else logger.println(Messages.RECEIVED_PUBLICATION + event);
		}
	}
	
	//////////
	
	public static void main(String[] args)
	{
		new SubscriberLauncher(args);
	}
}