package com.zigorsalvador.phoenix.routing;

import java.util.HashSet;
import java.util.Set;

import com.zigorsalvador.phoenix.constants.Settings;
import com.zigorsalvador.phoenix.interfaces.IMatcher;
import com.zigorsalvador.phoenix.interfaces.IRouter;
import com.zigorsalvador.phoenix.interfaces.ITerminal;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.messages.Message;
import com.zigorsalvador.phoenix.messages.Method;
import com.zigorsalvador.phoenix.messages.Receipt;
import com.zigorsalvador.phoenix.messages.Update;
import com.zigorsalvador.phoenix.structures.FilterPoset;
import com.zigorsalvador.phoenix.structures.FilterTable;
import com.zigorsalvador.phoenix.structures.GlobalState;
import com.zigorsalvador.phoenix.structures.LocalState;
import com.zigorsalvador.phoenix.transport.BrokerPacketReceiver;
import com.zigorsalvador.phoenix.transport.BrokerPacketSender;

public abstract class AbstractRouting implements IRouter
{	
	protected Address address;
	protected BrokerPacketSender sender;
	protected BrokerPacketReceiver receiver;
	
	//////////
	
	protected IMatcher routingTable;	
	protected LocalState localState;
	protected GlobalState globalState;
		
	//////////
	
	public AbstractRouting(Address address, ITerminal window, BrokerPacketSender sender, BrokerPacketReceiver receiver)
	{
		this.address = address;
		this.sender = sender;
		this.receiver = receiver;
		
		localState = new LocalState(window);
		globalState = new GlobalState(window);
	
		routingTable = (Settings.FILTER_POSET) ? new FilterPoset(window) : new FilterTable(window);
	}
	
	//////////
	
	@Override
	
	public void connect(Address broker) 
	{	
		localState.addBroker(broker);
		sender.send(new Message(Method.CNN, address, broker), broker);
	}
	
	//////////

	@Override
	
	public void disconnect(Address broker) 
	{
		localState.removeBroker(broker);
		sender.send(new Message(Method.BYE, address, broker), broker);
	}
	
	//////////

	@Override
	
	public void processConnection(Message message)
	{
		Address broker = message.getSource();
		
		// Update local state...
		
		localState.addBroker(broker);
	}

	//////////
	
	@Override
	
	public void processDisconnection(Message message)
	{
		Address broker = message.getSource();
		
		// Update local state...
		
		localState.removeBroker(broker);
	}
	
	//////////

	@Override

	public void processSubscription(Message message) 
	{
		Address source = message.getSource();
		Address client = message.getAddress();
		Filter filter = message.getFilter();
		
		// Update local state...
		
		if (source.equals(client) && !globalState.hasSubscriptions(client))
		{
			localState.addSubscriber(source);
		}
		
		// Update global state...
		
		globalState.addSubscription(client, filter);
		globalState.updateInterface(client, source);
		
		// Update routing table...
		
		routingTable.insert(filter, source);
		
		// Forward subscription message...
		
		HashSet<Address> forward = new HashSet<Address>();
		
		forward.addAll(localState.getBrokers());
		forward.remove(source);
		
		for (Address target : forward)
		{
			sender.send(new Message(Method.SUB, address, target, client, filter), target);
		}
	}
		
	//////////

	@Override
	
	public void processUnsubscription(Message message)
	{						
		Address source = message.getSource();
		Address client = message.getAddress();
		Filter filter = message.getFilter();	
		
		// Update global state...
		
		globalState.removeSubscription(client, filter);
		globalState.updateInterface(client, source);
		
		// Update local state...
		
		if (source.equals(client) && !globalState.hasSubscriptions(client))
		{
			localState.removeSubscriber(source);
			sender.reset(client);
		}
			
		// Update routing table...
																													
		routingTable.remove(filter, source);
		
		// Forward unsubscription message...
			
		HashSet<Address> forward = new HashSet<Address>();
			
		forward.addAll(localState.getBrokers());
		forward.remove(source);
		
		for (Address target : forward)
		{
			sender.send(new Message(Method.UNS, address, target, client, filter), target);
		}	
	}
	
	//////////
	
	@Override
	
	public abstract void processPublication(Message message);
	
	//////////
		
	@Override
		
	public void processMigration(Message message) 
	{		
		Address source = message.getSource();
		Address client = message.getAddress();
		Update update = message.getUpdate();
		
		// Update routing table...
		
		Address forward = globalState.getInterface(client);
		Set<Filter> subscriptionsToCorrect = globalState.getSubscriptions(client);
		routingTable.swap(subscriptionsToCorrect, forward, source);

		// Update global state...
				
		globalState.updateInterface(client, source);

		// Retransmit events or propagate message...
		
		if (localState.containsSubscriber(client))
		{
			if (update.getReplay() == true)
			{			
				Receipt receipt = update.getReceipt();
				sender.replay(source, client, receipt);
			}
			else
			{
				sender.reset(client);
			}			
		}
		else
		{
			sender.send(new Message(Method.MIG, address, forward, client, update), forward);
		}
		
		// Update local state...
		
		if (localState.containsBroker(source) == false) 
		{
			localState.addSubscriber(client);
		}
		else if (localState.containsSubscriber(client))
		{
			localState.removeSubscriber(client);
		}
	}
		
	//////////
	
	@Override
	
	public void processReplay(Message message)
	{	
		Address client = message.getAddress();
		Event event = message.getEvent();
		
		Address forward = globalState.getInterface(client);		
		
		if (localState.containsSubscriber(forward))
		{
			sender.enqueue(new Message(Method.REP, address, forward, client, event), forward);
		}
		
		sender.send(new Message(Method.REP, address, forward, client, event), forward);  				
	}
		
	//////////
	
	@Override
	
	public void processResume(Message message)
	{
		Address source = message.getSource();
		Update update = message.getUpdate();
		
		if (update.getReplay() == true)
		{
			Receipt receipt = update.getReceipt();
			sender.resume(source, receipt);
		}
		else
		{
			sender.connect(source, true);
		}
	}
	
	//////////

	@Override
	
	public void processFailure(Address subscriber) 
	{
		// Simulate unsubscription messages...
		
		Set<Filter> subscriptions = globalState.getSubscriptions(subscriber); 
		
		for (Filter filter : subscriptions)
		{
			processUnsubscription(new Message(Method.UNS, subscriber, address, subscriber, filter));
		}			
		
		// Update subscriber registry...
		
		localState.removeSubscriber(subscriber);
	}
	
	//////////

	@Override
	
	public void terminate()
	{
		// Simulate subscriber unsubscriptions...
		
		// TODO: Pending...
		
		// Send disconnection messages...
		
		for (Address broker : localState.getBrokers())
		{
			sender.send(new Message(Method.BYE, address, broker), broker);
		}
		
		// Terminate communication endpoints...
		
		sender.terminate();
		receiver.terminate();
	}
}