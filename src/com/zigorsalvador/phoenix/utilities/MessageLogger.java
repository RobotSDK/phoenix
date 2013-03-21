package com.zigorsalvador.phoenix.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageLogger 
{
	private String header;
	private SimpleDateFormat format;
	
	/////////
	
	public MessageLogger (String header)
	{
		this.header = header;		

		format = new SimpleDateFormat("HH:mm:ss:SSS");
	}
	
	//////////
	
	public void println(String string)
	{
		Date date = new Date();
		
		System.out.println(format.format(date) + " > " + header + " > " + string);
	}
}