package com.zigorsalvador.phoenix.discovery;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Callable;

public class UnicastFuture implements Callable<String>
{	
	private DatagramSocket socket;
	private DatagramPacket packet;
	
	//////////
	
	public UnicastFuture (DatagramSocket socket, DatagramPacket packet)
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