package com.zigorsalvador.phoenix.discovery;

import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.Callable;

public class MulticastFuture implements Callable<String>
{	
	private MulticastSocket socket;
	private DatagramPacket packet;
	
	//////////
	
	public MulticastFuture (MulticastSocket socket, DatagramPacket packet)
	{
		super();
		
		this.socket = socket;
		this.packet = packet;
	}

	//////////

	@Override
	
	public String call() throws Exception 
	{
		try 
		{
			byte[] inputBuffer = new byte[256];
  			packet = new DatagramPacket(inputBuffer, inputBuffer.length);
  			socket.receive(packet);
  			
  			return new String(packet.getData()).trim();
		}
		catch (Exception exception)
		{
			return null;
		}
	}
}