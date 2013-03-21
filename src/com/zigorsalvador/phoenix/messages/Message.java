package com.zigorsalvador.phoenix.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL) // NOTE: Jackson annotation...

public class Message
{
	private Method method;
	private Address source;
	private Address target;
	private Address address;
	
	//////////
	
	private Event event;
	private Filter filter;
	private Update update;
	
	//////////
	
	public Message () {} // NOTE: Empty constructor...
	
	//////////
	
	public Message (Method method, Address source, Address target)
	{
		this.method = method;
		this.source = source;
		this.target = target;
	}	
	
	//////////
	
	public Message (Method method, Address source, Address target, Address address, Event event)
	{
		this.method = method;
		this.source = source;
		this.target = target;
		this.address = address;
		this.event = event;
	}
	
	//////////
	
	public Message (Method method, Address source, Address target, Address address, Filter filter)
	{
		this.method = method;
		this.source = source;
		this.target = target;
		this.address = address;
		this.filter = filter;
	}
	
	//////////
	
	public Message (Method method, Address source, Address target, Address address, Update migration)
	{
		this.method = method;
		this.source = source;
		this.target = target;
		this.address = address;
		this.update = migration;
	}
	
	//////////
	
	public Method getMethod()
	{
		return method;
	}
	
	//////////
	
	public Address getSource()
	{
		return source;
	}
	
	//////////
	
	public Address getTarget()
	{
		return target;
	}
		
	//////////
	
	public Address getAddress()
	{
		return address;
	}
	
	//////////
	
	public Event getEvent()
	{
		return event;
	}	
	
	//////////
	
	public Filter getFilter()
	{
		return filter;
	}
		
	//////////
	
	public Update getUpdate()
	{
		return update;
	}	
	
	//////////
	
	public void setMethod(Method method)
	{
		this.method = method;
	}
	
	//////////
	
	public void setSource(Address source)
	{
		this.source = source;
	}
	
	//////////
	
	public void setTarget(Address target)
	{
		this.target = target;
	}
	
	//////////
	
	public void setAddress(Address address)
	{
		this.address = address;
	}

	//////////
	
	public void setEvent(Event event)
	{
		this.event = event;
	}
	
	//////////

	public void setFilter(Filter filter)
	{
		this.filter = filter;
	}
	
	//////////
	
	public void setUpdate(Update update)
	{
		this.update = update;
	}
	
	//////////
	
	public String toString() // NOTE: String...
	{
		String output = new String();
		
		output = output + method;
		output = output + " ";
		output = output + source;
		output = output + " ";
		output = output + target;
		
		if (address != null)
		{
			output = output + " ";
			output = output + address;
		}
		
		if (event != null)
		{	
			output = output + " ";
			output = output + event;
		}
		
		if (filter != null)
		{	
			output = output + " ";
			output = output + filter;
		}
		
		if (update != null)
		{	
			output = output + " ";
			output = output + update;
		}
		
		return output;
	}
}