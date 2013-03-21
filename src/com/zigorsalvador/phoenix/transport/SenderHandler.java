package com.zigorsalvador.phoenix.transport;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.zigorsalvador.phoenix.utilities.MessageLogger;

public class SenderHandler implements IoHandler
{
	private MessageLogger logger;
	
	//////////
	
	public SenderHandler(MessageLogger logger)
	{
		this.logger = logger;
	}
	
	//////////
	
	public void exceptionCaught(IoSession session, Throwable throwable) throws Exception
	{
		logger.println("Receiver exception = " + throwable.getClass().getSimpleName() + " : " + throwable.getMessage());
	}

	//////////
	
	public void messageReceived(IoSession session, Object object) throws Exception
	{
	}

	//////////
	
	public void messageSent(IoSession session, Object object) throws Exception 
	{
	}

	//////////
	
	public void sessionClosed(IoSession session) throws Exception 
	{
	}

	//////////
	
	public void sessionCreated(IoSession session) throws Exception 
	{
	}

	//////////
	
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception 
	{
	}

	//////////
	
	public void sessionOpened(IoSession session) throws Exception 
	{
	}	
}