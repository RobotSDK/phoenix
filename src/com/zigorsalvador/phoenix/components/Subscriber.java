package com.zigorsalvador.phoenix.components;

import java.util.HashSet;

import com.zigorsalvador.phoenix.constants.Messages;
import com.zigorsalvador.phoenix.interfaces.IListener;
import com.zigorsalvador.phoenix.interfaces.IReceiver;
import com.zigorsalvador.phoenix.interfaces.ISender;
import com.zigorsalvador.phoenix.interfaces.ISubscriber;
import com.zigorsalvador.phoenix.jackson.CustomMapper;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.messages.Message;
import com.zigorsalvador.phoenix.messages.Method;
import com.zigorsalvador.phoenix.messages.Receipt;
import com.zigorsalvador.phoenix.messages.Update;
import com.zigorsalvador.phoenix.transport.ClientPacketReceiver;
import com.zigorsalvador.phoenix.transport.ClientPacketSender;
import com.zigorsalvador.phoenix.utilities.MessageLogger;

public class Subscriber implements IReceiver, ISender, ISubscriber
{
	private HashSet<Filter> subscriptions;
	private ClientPacketReceiver receiver;
	private ClientPacketSender sender;
	private MessageLogger logger;
	private IListener listener;
	private CustomMapper json;
	private Receipt receipt;
	private Address address;
	private Address broker;
	
	//////////
	
	public Subscriber(Address address, Address broker, IListener listener)
	{
		this.address = address;
		this.broker = broker;
		this.listener = listener;
		
		json = new CustomMapper();
		logger = new MessageLogger(address.getAlias());
		
		sender = new ClientPacketSender(this, json, logger);
		receiver = new ClientPacketReceiver(this, json, logger, address);
		
		subscriptions = new HashSet<Filter>();
		
		if (broker == null)
		{
			logger.println(Messages.BROKER_NOT_FOUND);
			System.exit(1);
		}
		
		logger.println(Messages.BROKER_ADDRESS + broker);
		logger.println(Messages.SUBSCRIBER_ADDRESS + address);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {public void run() {unsubscribe(); terminate(); logger.println(Messages.SHUTDOWN_HOOK_DETECTED);}});
	}

	//////////

	private void processPublication(Message message)
	{
		Event event = message.getEvent();
		receipt = event.getReceipt();
		listener.notify(event, false);
	}
	
	//////////
	
	private void processReplay(Message message)
	{
		Event event = message.getEvent();
		receipt = event.getReceipt();
		listener.notify(event, true);
	}
	
	//////////

	@Override
	
	public void subscribe(Filter filter) 
	{	
		if (subscriptions.contains(filter) == false)
		{
			sender.send(new Message(Method.SUB, address, broker, address, filter), broker);
			logger.println(Messages.SENT_SUBSCRIPTION + filter);
			subscriptions.add(filter);
		}	
	}

	//////////
	
	@Override
	
	public void unsubscribe(Filter filter) 
	{
		if (subscriptions.contains(filter) == true)
		{
			sender.send(new Message(Method.UNS, address, broker, address, filter), broker);
			logger.println(Messages.SENT_UNSUBSCRIPTION + filter);
			subscriptions.remove(filter);
		}
	}
	
	//////////
	
	@Override
	
	public void unsubscribe()
	{
		for (Filter filter : subscriptions)
		{
			sender.send(new Message(Method.UNS, address, broker, address, filter), broker);
			logger.println(Messages.SENT_UNSUBSCRIPTION + filter);
		}
		
		subscriptions.clear();
	}
	
	//////////
	
	@Override
	
	public void reconfigure(Address broker)
	{
		this.broker = broker;		
		
		sender = new ClientPacketSender(this, json, logger);
		receiver = new ClientPacketReceiver(this, json, logger, address);

		logger.println(Messages.BROKER_ADDRESS + broker);
		logger.println(Messages.SUBSCRIBER_ADDRESS + address);
	}
	
	//////////
	
	@Override
	
	public void resume(Boolean replay)
	{
		Update update = new Update(receipt, replay);
		sender.send(new Message(Method.RES, address, broker, null, update), broker);
		logger.println(Messages.SENT_RESUMPTION + update);
	}

	//////////
	
	@Override
	
	public void migrate(Boolean replay)
	{
		Update update = new Update(receipt, replay);
		sender.send(new Message(Method.MIG, address, broker, address, update), broker);
		logger.println(Messages.SENT_MIGRATION + update);
	}
	
	//////////
	
	@Override
	
	public void process(Message message) 
	{
		switch (message.getMethod())
		{
			case PUB: processPublication(message); break;
			case REP: processReplay(message); break;
			
			default: logger.println(Messages.UNEXPECTED_MESSAGE + message); System.exit(1);
		}
	}
	
	//////////
	
	@Override

	public void failure(Address address) 
	{
		logger.println(Messages.CONNECTION_ABORTED + address);
		System.exit(1);
	}
	
	//////////
	
	@Override
	
	public void terminate() 
	{
		sender.terminate();
		receiver.terminate();
	}
}