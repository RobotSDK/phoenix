package com.zigorsalvador.phoenix.launchers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import com.zigorsalvador.phoenix.components.Publisher;
import com.zigorsalvador.phoenix.constants.Messages;
import com.zigorsalvador.phoenix.discovery.DiscoveryManager;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Type;
import com.zigorsalvador.phoenix.messages.Value;
import com.zigorsalvador.phoenix.utilities.AddressHandler;
import com.zigorsalvador.phoenix.utilities.MessageLogger;
import com.zigorsalvador.phoenix.utilities.SimpleSleeper;

public class PublisherLauncher implements Runnable
{
	private MessageLogger logger;
	private Publisher publisher;
	
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
	
	public PublisherLauncher(String[] args)
	{
		CmdLineParser.registerHandler(com.zigorsalvador.phoenix.messages.Address.class, AddressHandler.class);
		
		CmdLineParser parser = new CmdLineParser(this);
		
		try
		{
			parser.parseArgument(args);

			if (client == null) client = new Address("publisher");
			if (broker == null) broker = DiscoveryManager.discover();
			if (time == null) time = Integer.MAX_VALUE;
			if (text == null) text = "good";
		}
		catch (Exception exception)
		{
			System.out.println("*" + exception);
			System.exit(1);
		}

		publisher = new Publisher(client, broker);		
		logger = new MessageLogger(client.getAlias());
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(this, 0, 1, TimeUnit.SECONDS);
		
		SimpleSleeper.sleepSeconds(time);
		
		service.shutdownNow();
		
		publisher.terminate();
		
		System.exit(0);
	}

	//////////
	
	@Override
	
	public void run() 
	{		
		Event event = new Event();
		event.addValue(new Value(Type.STR, "text", text));
		publisher.publish(event);

		if (verbose) logger.println(Messages.SENT_PUBLICATION + event);
	}

	//////////
	
	public static void main(String[] args) 
	{
		new PublisherLauncher(args);
	}
}