package com.zigorsalvador.phoenix.discovery;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.zigorsalvador.phoenix.constants.Discovery;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.utilities.AddressParser;


public class UnicastSpeaker
{	
	private static DatagramSocket socket;
	private static DatagramPacket packet;
	private static InetAddress address;
	
	//////////
		
	public static Address discover()
	{
		try
		{
			address = InetAddress.getByName(Discovery.UNICAST_ADDRESS);
			socket = new DatagramSocket(Discovery.DEFAULT_SOURCE_PORT);
			socket.setSoTimeout(Discovery.UNICAST_TIMEOUT);
			socket.setReuseAddress(true);
			
			ExecutorService service = Executors.newSingleThreadExecutor();			
			FutureTask<String> future = new FutureTask<String>(new UnicastFuture(socket, packet));
			service.execute(future);
	        
			String message = Discovery.REQUEST;
			byte[] outputBuffer = new byte[256];
	        outputBuffer = message.getBytes();
	            
	        packet = new DatagramPacket(outputBuffer, outputBuffer.length, address, Discovery.UNICAST_DESTINATION_PORT);

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