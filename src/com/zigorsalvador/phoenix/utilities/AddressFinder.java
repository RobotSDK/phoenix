package com.zigorsalvador.phoenix.utilities;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Enumeration;

public class AddressFinder
{
	public static String host()
	{
		InetAddress loopback = null;
		
	    try
	    {
	    	Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	    	
	    	while (interfaces.hasMoreElements())
	    	{
	    		NetworkInterface iface = interfaces.nextElement();
	    		Enumeration<InetAddress> addresses = iface.getInetAddresses();
	    		
	    		while (addresses.hasMoreElements())
	    		{
	    			InetAddress address = addresses.nextElement();
    			
	    			if (address.isLoopbackAddress() == true && address.getAddress().length == 4)
	    			{
	    				loopback = address;
	    			}
	    			
	    			if (address.isLoopbackAddress() == false && address.getAddress().length == 4)
	    			{
    		        	return address.getHostAddress();
	    			}
	    		}
	    	}
	    }
	    catch (Exception exception) 
	    {
			System.out.println(exception);
			System.exit(1);
		}
	    
	    return loopback.getHostAddress();
	}
	
	//////////
	
	public static Integer port()
	{
		Integer output = null;
		
		try
		{
			ServerSocket socket;
			socket = new ServerSocket(0);
			socket.setReuseAddress(true);
			output = socket.getLocalPort();
			socket.close();
			socket = null;
		}
        catch (IOException exception)
        {
        	System.out.println(exception);
        	System.exit(1);
		}

		return output;
	}
}