package com.zigorsalvador.phoenix.jackson;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.zigorsalvador.phoenix.messages.Address;
import com.zigorsalvador.phoenix.messages.Message;

public class CustomMapper 
{
	private ObjectMapper mapper;
	
	//////////
	
	public CustomMapper()
	{
		mapper = new ObjectMapper();

		SimpleModule module = new SimpleModule("Custom");
		module.addDeserializer(Address.class, new AddressDeserializer());
		module.addSerializer(Address.class, new AddressSerializer());
		
		mapper.registerModule(module);
	}
	
	//////////
	
	public String encode(Message message)
	{
		try
		{
			return mapper.writeValueAsString(message);
		}
		catch (Exception exception)
		{
			System.out.println(exception);
			System.exit(1);
		}
		
		return null;
	}
	
	//////////
	
	public Message decode(Object object)
	{
		try
		{
			return mapper.readValue((String) object, Message.class);
		}
		
		catch (Exception exception)
		{
			System.out.println(exception);
			System.exit(1);			
		}
			
		return null;
	}
}