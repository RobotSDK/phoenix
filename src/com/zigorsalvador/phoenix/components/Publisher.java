package com.zigorsalvador.phoenix.components;

import com.zigorsalvador.phoenix.constants.Messages;
import com.zigorsalvador.phoenix.interfaces.IPublisher;
import com.zigorsalvador.phoenix.interfaces.IReceiver;
import com.zigorsalvador.phoenix.interfaces.ISender;
import com.zigorsalvador.phoenix.jackson.CustomMapper;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Message;
import com.zigorsalvador.phoenix.messages.Method;
import com.zigorsalvador.phoenix.messages.Receipt;
import com.zigorsalvador.phoenix.transport.ClientPacketReceiver;
import com.zigorsalvador.phoenix.transport.ClientPacketSender;
import com.zigorsalvador.phoenix.utilities.MessageLogger;

public class Publisher implements IReceiver, ISender, IPublisher
{
	private ClientPacketReceiver receiver;
	private ClientPacketSender sender;
	private MessageLogger logger;
	private CustomMapper json;
	private Address address;
	private Address broker;
	private Long number;
	
	//////////
	
	public Publisher(Address address, Address broker)
	{
		this.address = address;
		this.broker = broker;
		
		json = new CustomMapper();
		logger = new MessageLogger(address.getAlias());
		
		sender = new ClientPacketSender(this, json, logger);
		receiver = new ClientPacketReceiver(this, json, logger, address);
		
		number = 1L;
		
		if (broker == null)
		{
			logger.println(Messages.BROKER_NOT_FOUND);
			System.exit(1);
		}
		
		logger.println(Messages.BROKER_ADDRESS + broker);
		logger.println(Messages.PUBLISHER_ADDRESS + address);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {public void run() {terminate(); logger.println(Messages.SHUTDOWN_HOOK_DETECTED);}});
	}
	
	//////////
	
	public Integer measure(Event event)
	{
		event.setReceipt(new Receipt(address, number));
		return json.encode(new Message(Method.PUB, address, broker, null, event)).length();
	}
	
	//////////

	@Override
	
	public void publish(Event event) 
	{
		event.setReceipt(new Receipt(address, number++));
		sender.send(new Message(Method.PUB, address, broker, null, event),  broker);
	}
	
	//////////
	
	@Override
	
	public void process(Message message) 
	{
		switch (message.getMethod())
		{		
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