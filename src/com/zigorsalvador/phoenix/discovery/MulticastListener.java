package com.zigorsalvador.phoenix.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

import com.zigorsalvador.phoenix.constants.Discovery;
import com.zigorsalvador.phoenix.constants.Messages;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.utilities.MessageLogger;

public class MulticastListener extends Thread
{
	private MulticastSocket socket;
	private DatagramPacket packet;
	private MessageLogger logger;
	private InetAddress group;
	private Address address;
	private Boolean active;
	
	//////////
	
	public MulticastListener(MessageLogger logger, Address address)
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
			group = InetAddress.getByName(Discovery.MULTICAST_ADDRESS);
			socket = new MulticastSocket(Discovery.MULTICAST_DESTINATION_PORT);
			socket.setLoopbackMode(true);
			socket.setReuseAddress(true);
			socket.setSoTimeout(1000);
			socket.joinGroup(group);
				
			logger.println(Messages.MULTICAST_LISTENER_START + socket.getLocalPort());
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
					logger.println(Messages.MULTICAST_REQUEST);
					
					String message = address.toString();				
					byte[] outputBuffer = new byte[256];
					outputBuffer = message.getBytes();
			            
					packet = new DatagramPacket(outputBuffer, outputBuffer.length, group, Discovery.DEFAULT_SOURCE_PORT);

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
			
		try
		{
			socket.leaveGroup(group);			
			socket.close();
		}
		catch (IOException exception) 
		{
			System.out.println(exception);
			System.exit(1);
		}
		
		logger.println(Messages.MULTICAST_LISTENER_STOP);
	}
	
	//////////
	
	public void terminate()
	{
		active = false;
	}	
}