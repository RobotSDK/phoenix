package com.zigorsalvador.phoenix.transport;

import java.net.InetSocketAddress;
import java.util.HashMap;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.zigorsalvador.phoenix.constants.Messages;
import com.zigorsalvador.phoenix.constants.Settings;
import com.zigorsalvador.phoenix.interfaces.ISender;
import com.zigorsalvador.phoenix.jackson.CustomMapper;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Message;
import com.zigorsalvador.phoenix.utilities.MessageLogger;

public class ClientPacketSender 
{	
	private ISender sender;
	private CustomMapper json;
	private MessageLogger logger;
	private NioSocketConnector connector;
	private HashMap<Address, NioSession> sessions;
	
	//////////

	public ClientPacketSender(ISender sender, CustomMapper json, MessageLogger logger)
	{
		this.sender = sender;
		this.json = json;
		this.logger = logger;
		
		connector = new NioSocketConnector();
		
		connector.setConnectTimeoutMillis(1000);
		connector.setHandler(new SenderHandler(logger));
		connector.getSessionConfig().setWriteTimeout(10);
		connector.getSessionConfig().setReuseAddress(true);
		connector.getSessionConfig().setTcpNoDelay(Settings.TCP_NODELAY);
		
		sessions = new HashMap<Address, NioSession>();
		
		TextLineCodecFactory factory = new TextLineCodecFactory();
		factory.setEncoderMaxLineLength(4096);
		
		connector.getFilterChain().addLast("protocol", new ProtocolCodecFilter(factory));
				
		if (Settings.MINA_LOGGING) connector.getFilterChain().addLast("logging", new LoggingFilter(getClass())); 
	}

	//////////
		
	public NioSession connect(Address target)
	{
		NioSession session = sessions.get(target);
		
		if (session == null || session.isConnected() == false)
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
			}
			else
			{
				logger.println(Messages.CONNECTION_FAILURE + target);
				sender.failure(target);
			}
		}
	
		return session;
	}
	
	//////////
	
	public void send(Message message, Address target)
	{
		NioSession session = connect(target);
		
		if (session != null && session.isConnected())
		{
			session.write(json.encode(message));
		}
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