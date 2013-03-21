package com.zigorsalvador.phoenix.launchers;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import com.zigorsalvador.phoenix.components.Broker;
import com.zigorsalvador.phoenix.discovery.DiscoveryManager;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.utilities.AddressHandler;

public class BrokerLauncher
{
	@Option(name="-broker")
    private Address broker;
	
	@Option(name="-master")
    private Address master;
	
	@Option(name="-console", handler=BooleanOptionHandler.class)
    private boolean console;

	//////////
	
	public BrokerLauncher(String[] args)
	{
		CmdLineParser.registerHandler(com.zigorsalvador.phoenix.messages.Address.class, AddressHandler.class);
		
		CmdLineParser parser = new CmdLineParser(this);
		
		try
		{
			parser.parseArgument(args);

			if (broker == null) broker = new Address("broker");
			if (master == null) master = DiscoveryManager.discover();
			
			new Broker(broker, master, console);
		}
		catch (Exception exception)
		{
			System.out.println(exception);
			System.exit(1);
		}
	}
	
	//////////
	
	public static void main(String[] args) 
	{
		new BrokerLauncher(args);
	}	
}