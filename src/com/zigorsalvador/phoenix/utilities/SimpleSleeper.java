package com.zigorsalvador.phoenix.utilities;

import java.util.concurrent.TimeUnit;

public class SimpleSleeper
{
	public static void sleepSeconds(Integer seconds)
	{		
		try
		{
			TimeUnit.SECONDS.sleep(seconds);
		}
		catch (Exception exception)
		{
			System.out.println(exception);
			System.exit(1);
		}
	}
	
	//////////
	
	public static void sleepMilliseconds(Integer milliseconds)
	{		
		try
		{
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		}
		catch (Exception exception)
		{
			System.out.println(exception);
			System.exit(1);
		}
	}
}