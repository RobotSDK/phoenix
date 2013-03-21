package com.zigorsalvador.phoenix.discovery;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.zigorsalvador.phoenix.constants.Discovery;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.utilities.AddressParser;

public class MulticastSpeaker
{	
	private static MulticastSocket socket;
	private static DatagramPacket packet;
	private static InetAddress address;
	
	//////////
		
	public static Address discover()
	{
		try
		{
			address = InetAddress.getByName(Discovery.MULTICAST_ADDRESS);
			socket = new MulticastSocket(Discovery.DEFAULT_SOURCE_PORT);
			socket.setSoTimeout(Discovery.MULTICAST_TIMEOUT);
			socket.setReuseAddress(true);
			socket.joinGroup(address);
			
			ExecutorService service = Executors.newSingleThreadExecutor();			
			FutureTask<String> future = new FutureTask<String>(new MulticastFuture(socket, packet));
			service.execute(future);
	        
			String message = Discovery.REQUEST;
			byte[] outputBuffer = new byte[256];
	        outputBuffer = message.getBytes();
	            
	        packet = new DatagramPacket(outputBuffer, outputBuffer.length, address, Discovery.MULTICAST_DESTINATION_PORT);

	        socket.send(packet);

	        String response = future.get();
	        
	        socket.close();

	        if (response != null && !response.equals(Discovery.REQUEST))
	        {
	        	return AddressParser.address(response);
	        }
	  	}
		catch (Exception exception) 
		{
	    }
		
		return null;
	}
}