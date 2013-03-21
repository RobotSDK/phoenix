package com.zigorsalvador.phoenix.transport;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.zigorsalvador.phoenix.constants.Settings;
import com.zigorsalvador.phoenix.interfaces.IReceiver;
import com.zigorsalvador.phoenix.jackson.CustomMapper;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.utilities.MessageLogger;

public class ClientPacketReceiver
{	
	private NioSocketAcceptor acceptor;
	
	//////////

	public ClientPacketReceiver(IReceiver receiver, CustomMapper json, MessageLogger logger, Address local)
	{		
		acceptor = new NioSocketAcceptor();

        acceptor.setDefaultLocalAddress(new InetSocketAddress(local.getHost(), local.getPort()));
		acceptor.setHandler(new ReceiverHandler(receiver, json, logger));
        acceptor.setCloseOnDeactivation(true);
		acceptor.setReuseAddress(true);
				
		TextLineCodecFactory factory = new TextLineCodecFactory();
		factory.setDecoderMaxLineLength(4096);
		
		acceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(factory));
		
		if (Settings.MINA_LOGGING) acceptor.getFilterChain().addLast("logging", new LoggingFilter(getClass()));       

        try
        { 	      						
        	acceptor.bind();
        }
        catch (IOException exception)
        {
        	System.out.println(exception);
        	System.exit(1);
		}
	}
	
	//////////
	
	public void terminate()
	{	
		acceptor.unbind(); 
	}
}