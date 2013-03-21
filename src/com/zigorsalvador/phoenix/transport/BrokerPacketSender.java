package com.zigorsalvador.phoenix.transport;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.zigorsalvador.phoenix.constants.Messages;
import com.zigorsalvador.phoenix.constants.Settings;
import com.zigorsalvador.phoenix.interfaces.ISender;
import com.zigorsalvador.phoenix.interfaces.ITerminal;
import com.zigorsalvador.phoenix.jackson.CustomMapper;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Message;
import com.zigorsalvador.phoenix.messages.Method;
import com.zigorsalvador.phoenix.messages.Receipt;
import com.zigorsalvador.phoenix.utilities.MessageLogger;

public class BrokerPacketSender 
{
	private Integer CAPACITY = 1000;
	
	//////////
	
	private ITerminal window;
	private CustomMapper json;
	private MessageLogger logger;
	private HashSet<Address> failures;
	private HashSet<Address> overflows;
	private NioSocketConnector connector;
	private HashMap<Address, NioSession> sessions;
	private HashMap<Address, LinkedBlockingQueue<Message>> queues;
		
	//////////

	public BrokerPacketSender(ISender sender, CustomMapper json, MessageLogger logger, ITerminal window)
	{
		this.json = json;
		this.logger = logger;
		this.window = window;
		
		connector = new NioSocketConnector();
		
		connector.setConnectTimeoutMillis(1000);
		connector.setHandler(new SenderHandler(logger));
		connector.getSessionConfig().setWriteTimeout(10);
		connector.getSessionConfig().setReuseAddress(true);
		connector.getSessionConfig().setTcpNoDelay(Settings.TCP_NODELAY);
		
		failures = new HashSet<Address>();
		overflows = new HashSet<Address>();
		sessions = new HashMap<Address, NioSession>();
		queues = new HashMap<Address, LinkedBlockingQueue<Message>>();
		
		TextLineCodecFactory factory = new TextLineCodecFactory();
		factory.setEncoderMaxLineLength(4096);
		
		connector.getFilterChain().addLast("protocol", new ProtocolCodecFilter(factory));
		connector.getFilterChain().addLast("executor", new ExecutorFilter());
				
		if (Settings.MINA_LOGGING) connector.getFilterChain().addLast("logging", new LoggingFilter(getClass())); 
	}

	//////////
	
	public NioSession connect(Address target, Boolean reconnect)
	{
		NioSession session = sessions.get(target);
		
		if (session == null || reconnect)
		{
			InetSocketAddress socket = new InetSocketAddress(target.getHost(), target.getPort());
			ConnectFuture future = connector.connect(socket);
			
			future.awaitUninterruptibly();
		
			if (future.isConnected() == true)
			{
				logger.println(Messages.CONNECTION_SUCCESS + target);
				session = (NioSession) future.getSession();
				session.setAttribute("address", target);
				sessions.put(target, session);
				failures.remove(target);
			}
			else
			{
				if (failures.contains(target) == false)
				{
					logger.println(Messages.CONNECTION_FAILURE + target);
					failures.add(target);
				}
			}
		}

		return session;
	}
	
	//////////
	
	public void send(Message message, Address target)
	{											
		NioSession session = connect(target, false);
		
		if (session != null && session.isConnected())
		{
			session.write(json.encode(message)); 
					
			if (message.getMethod().equals(Method.PUB) || message.getMethod().equals(Method.REP))
			{
				window.increaseOutgoingEvents(1);
			}
			else
			{
				window.increaseOutgoingSignals(1);
			}
		}
	}
	
	//////////			
				
	public synchronized void enqueue(Message message, Address subscriber) // NOTE: Synchronized...
	{
		LinkedBlockingQueue<Message> queue;
		
		if (queues.containsKey(subscriber))
		{
			queue = queues.get(subscriber);			
		}
		else
		{
			queue = new LinkedBlockingQueue<Message>(CAPACITY);
			logger.println(Messages.QUEUE_CREATION + subscriber);
		}
		
		if (queue.size() == CAPACITY)
		{
			if (overflows.contains(subscriber) == false)
			{
				logger.println(Messages.QUEUE_OVERFLOW + subscriber);
				overflows.add(subscriber);
			}
			
			queue.remove();
		}

		queue.add(message);
		queues.put(subscriber, queue);
	}
	
	//////////
	
	public synchronized void resume(Address subscriber, Receipt receipt) // NOTE: Synchronized...
	{
		Integer counter = 0;
		
		NioSession session = connect(subscriber, true);
		
		if (session != null && session.isConnected())
		{	
			LinkedBlockingQueue<Message> queue;
			
			if (queues.containsKey(subscriber))
			{
				queue = queues.get(subscriber);	

				Iterator<Message> iterator = queue.iterator();
				
				while (iterator.hasNext())
				{
					Message message = iterator.next();
					
					if (message.getEvent().getReceipt().equals(receipt))
					{
						break;
					}
				}
				
				while (iterator.hasNext())
				{
					Message message = iterator.next();
					message.setMethod(Method.REP);
					session.write(json.encode(message));
					window.increaseOutgoingEvents(1);
					counter++;
				}
				
				logger.println(Messages.REPLAYED_MESSAGES + counter);
			}
		}
	}
	
	//////////
	
	public synchronized void replay(Address broker, Address subscriber, Receipt receipt) // NOTE: Synchronized...
	{
		Integer counter = 0;
		
		NioSession session = connect(broker, false);
		
		if (session != null && session.isConnected())					
		{	
			LinkedBlockingQueue<Message> queue;
		
			if (queues.containsKey(subscriber))
			{
				queue = queues.get(subscriber);
			
				Iterator<Message> iterator = queue.iterator();
				
				while (iterator.hasNext())
				{
					Message message = iterator.next();
				
					if (message.getEvent().getReceipt().equals(receipt))
					{
						break;
					}
				}
				
				while (iterator.hasNext())
				{
					Message message = iterator.next();
					message.setTarget(broker);
					message.setMethod(Method.REP);									
					message.setAddress(subscriber);
					session.write(json.encode(message));
					window.increaseOutgoingEvents(1);
					counter++;
				}
								
				logger.println(Messages.REPLAYED_MESSAGES + counter);
				
				reset(subscriber);
			}
		}
	}
	
	//////////
	
	public void reset(Address subscriber) 
	{
		sessions.remove(subscriber);
		
		logger.println(Messages.QUEUE_DELETION + subscriber);
		queues.remove(subscriber);
		System.gc();
	}
	
	//////////
	
	public void terminate()
	{
		for (NioSession session : sessions.values())
		{
			session.close(true);
		}
		
		connector.dispose();
	}
}