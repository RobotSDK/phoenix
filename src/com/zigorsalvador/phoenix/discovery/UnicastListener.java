package com.zigorsalvador.phoenix.discovery;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

import com.zigorsalvador.phoenix.constants.Discovery;
import com.zigorsalvador.phoenix.constants.Messages;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.utilities.MessageLogger;

public class UnicastListener extends Thread
{
	private DatagramSocket socket;
	private DatagramPacket packet;
	private MessageLogger logger;
	private Address address;
	private Boolean active;
	
	//////////
	
	public UnicastListener(MessageLogger logger, Address address)
	{
		this.logger = logger;
		this.address = address;
		this.active = true;

		this.setName(this.getClass().getSimpleName());
	}
	
	//////////
	
	public void run() 
	{	
		try
		{
			socket = new DatagramSocket(Discovery.UNICAST_DESTINATION_PORT);
			socket.setReuseAddress(true);
			socket.setSoTimeout(1000);
			
			logger.println(Messages.UNICAST_LISTENER_START + socket.getLocalPort());
		}
		catch (Exception exception)
		{
			System.out.println(exception);
			System.exit(1);
		}

		while (active)
		{
			try
			{
				byte[] inputBuffer = new byte[256];
				packet = new DatagramPacket(inputBuffer, inputBuffer.length);
				socket.receive(packet);
				
				String request = new String(packet.getData()).trim();
						
				if (request.equals(Discovery.REQUEST))
				{
					logger.println(Messages.UNICAST_REQUEST);
					
					String message = address.toString();
					byte[] outputBuffer = new byte[256];
					outputBuffer = message.getBytes();
		            
					packet = new DatagramPacket(outputBuffer, outputBuffer.length, packet.getAddress(), Discovery.DEFAULT_SOURCE_PORT);

					socket.send(packet);
				}
			}
			catch (SocketTimeoutException exception)
			{
			}
			catch (Exception exception)
			{
				System.out.println(exception);
				System.exit(1);
			}
		}
		
		socket.close();
		
		logger.println(Messages.UNICAST_LISTENER_STOP);
	}
	
	//////////
	
	public void terminate()
	{
		active = false;
	}	
}