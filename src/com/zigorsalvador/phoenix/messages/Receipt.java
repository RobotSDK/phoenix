package com.zigorsalvador.phoenix.messages;

public class Receipt
{
	private Address publisher;
	private Long number;
	
	//////////
	
	public Receipt () {} // NOTE: Empty constructor...
	
	//////////
	
	public Receipt (Address publisher, Long number)
	{
		this.publisher = publisher;
		this.number = number;
	}
	
	//////////
	
	public Address getPublisher()
	{
		return publisher;
	}
	
	//////////
	
	public Long getNumber()
	{
		return number;
	}
	
	//////////
		
	public void setPublisher(Address publisher)
	{
		this.publisher = publisher;
	}
	
	//////////
	
	public void setNumber(Long number)
	{
		this.number = number;
	}
	
	//////////
	
	public boolean equals(Object object) // NOTE: Equals...
	{
		if (object == this)
		{
			return true;
		}
		
		if (object instanceof Receipt)
		{
			Receipt identifier = (Receipt) object;
		
			return (publisher.equals(identifier.publisher) && number.equals(identifier.number));
		}

		return false;
	}
	
	//////////
	
	public int hashCode() // NOTE: Hash...
	{
		int result = 17;	
		
		result = 31 * result + (publisher == null ? 0 : publisher.hashCode());
		result = 31 * result + (number == null ? 0 : number.hashCode());
	
		return result;
	}
	
	//////////
	
	public String toString() // NOTE: String...
	{
		String output = new String();
		
		output = output + publisher;
		output = output + " ";
		output = output + number;
		
		return output;
	}
}