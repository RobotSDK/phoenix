package com.zigorsalvador.phoenix.messages;

public class Update
{
	private Receipt receipt;
	private Boolean replay;
	
	//////////
	
	public Update() {} // NOTE: Empty constructor...
	
	//////////
	
	public Update(Receipt receipt, Boolean replay)
	{
		this.receipt = receipt;
		this.replay = replay;
	}
	
	//////////
	
	public Receipt getReceipt()
	{
		return receipt;
	}
	
	//////////
	
	public Boolean getReplay()
	{
		return replay;
	}
	
	//////////
	
	public void setReceipt(Receipt receipt)
	{
		this.receipt = receipt;
	}	

	//////////
	
	public void setReplay(Boolean replay)
	{
		this.replay = replay;
	}
	
	//////////
	
	public String toString() // NOTE: String...
	{
		String output = new String();
		
		output = output + receipt;
		output = output + " ";
		output = output + replay;
		
		return output;
	}
}