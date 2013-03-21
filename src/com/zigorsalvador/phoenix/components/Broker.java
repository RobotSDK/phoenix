package com.zigorsalvador.phoenix.components;

import com.zigorsalvador.phoenix.constants.Messages;
import com.zigorsalvador.phoenix.constants.Settings;
import com.zigorsalvador.phoenix.discovery.MulticastListener;
import com.zigorsalvador.phoenix.discovery.UnicastListener;
import com.zigorsalvador.phoenix.graphical.WindowTerminal;
import com.zigorsalvador.phoenix.interfaces.IBroker;
import com.zigorsalvador.phoenix.interfaces.IReceiver;
import com.zigorsalvador.phoenix.interfaces.IRouter;
import com.zigorsalvador.phoenix.interfaces.ISender;
import com.zigorsalvador.phoenix.interfaces.ITerminal;
import com.zigorsalvador.phoenix.jackson.CustomMapper;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Message;
import com.zigorsalvador.phoenix.routing.GlobalRouting;
import com.zigorsalvador.phoenix.routing.SimpleRouting;
import com.zigorsalvador.phoenix.transport.BrokerPacketReceiver;
import com.zigorsalvador.phoenix.transport.BrokerPacketSender;
import com.zigorsalvador.phoenix.utilities.ConsoleTerminal;
import com.zigorsalvador.phoenix.utilities.MessageLogger;

public class Broker implements IReceiver, ISender, IBroker
{	
	private MulticastListener multicastListener;
	private UnicastListener unicastListener;
	private BrokerPacketReceiver receiver;
	private BrokerPacketSender sender;
	private MessageLogger logger;
	private ITerminal terminal;
	private CustomMapper json;
	private Address address;
	private IRouter router;
	
	//////////
		
	public Broker(Address address, Address broker, Boolean console)
	{	
		this.address = address;
				
		json = new CustomMapper();
		logger = new MessageLogger(address.getAlias());

		terminal = console ? new ConsoleTerminal() : new WindowTerminal(this);
		
		sender = new BrokerPacketSender(this, json, logger, terminal);
		receiver = new BrokerPacketReceiver(this, json, logger, address);
		
		router = (Settings.SINGLE_MATCHING) ? new GlobalRouting(address, terminal, sender, receiver) : new SimpleRouting(address, terminal, sender, receiver);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {public void run() {router.terminate(); logger.println(Messages.SHUTDOWN_HOOK_DETECTED);}});

		logger.println(Messages.BROKER_ADDRESS + address);
		
		String structure = (Settings.FILTER_POSET) ? "Poset" : "Table";
		String matching = (Settings.SINGLE_MATCHING) ? "Single" : "Normal";
		
		logger.println("(Filter " + structure + ") (" + matching + " Matching)");
		
		if (broker == null)
		{
			startMulticastListener();
			startUnicastListener();
		}
		else
		{
			connect(broker);
		}
	}
	
	//////////
	
	public void startMulticastListener()
	{
		multicastListener = new MulticastListener(logger, address);
		multicastListener.start();
	}
	
	//////////
	
	public Boolean isMulticastDiscoverable()
	{
		return (multicastListener != null);
	}
	
	//////////
	
	public void stopMulticastListener()
	{
		if (multicastListener != null)
		{
			multicastListener.terminate();
			multicastListener = null;
		}
	}
	
	//////////
	
	public void startUnicastListener()
	{
		unicastListener = new UnicastListener(logger, address);
		unicastListener.start();
	}
	
	//////////
	
	public Boolean isUnicastDiscoverable()
	{
		return (unicastListener != null);
	}
		
	//////////
	
	public void stopUnicastListener()
	{
		if (unicastListener != null)
		{
			unicastListener.terminate();
			unicastListener = null;
		}
	}
	
	//////////
	
	public Address getAddress()
	{
		return address;
	}
	
	//////////

	@Override
	
	public void connect(Address broker) 
	{	
		router.connect(broker);
	}

	//////////
	
	@Override
	
	public void disconnect(Address broker) 
	{	
		router.disconnect(broker);
	}
	
	//////////
	
	@Override
	
	public void process(Message message) 
	{
		switch (message.getMethod())
		{		
			case SUB: router.processSubscription(message); terminal.increaseIncomingSignals(1); break;
			case UNS: router.processUnsubscription(message); terminal.increaseIncomingSignals(1); break;
			case PUB: router.processPublication(message); terminal.increaseIncomingEvents(1); break;
			case REP: router.processReplay(message); terminal.increaseIncomingEvents(1); break;
			case MIG: router.processMigration(message); terminal.increaseIncomingSignals(1); break;
			case CNN: router.processConnection(message); terminal.increaseIncomingSignals(1); break;
			case BYE: router.processDisconnection(message); terminal.increaseIncomingSignals(1); break;	
			case RES: router.processResume(message); terminal.increaseIncomingSignals(1); break;	
						
			default: logger.println(Messages.UNEXPECTED_MESSAGE + message); System.exit(1);
		}
	}
	
	//////////
	
	@Override
	
	public void failure(Address address) 
	{
		logger.println(Messages.CONNECTION_ABORTED + address);
		router.processFailure(address);
	}
	
	//////////
	
	@Override
	
	public void terminate() 
	{
		if (multicastListener != null) multicastListener.terminate();
		if (unicastListener != null) unicastListener.terminate();
		
		router.terminate();
	}
}