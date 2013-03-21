package com.zigorsalvador.phoenix.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL) // NOTE: Jackson annotation...

public class Value  
{
	private Type type;
	private String name;
	
	//////////

	private Long longValue;
	private Double doubleValue;
	private String stringValue;
	private Boolean booleanValue;
	private Integer integerValue;
	
	//////////
	
	public Value () {} // NOTE: Empty constructor...
	
	//////////
	
	public Value(Type type, String name, Long longValue)
	{
		this.type = type;
		this.name = name;
		
		this.longValue = longValue;
	}
	
	//////////
	
	public Value(Type type, String name, Double doubleValue)
	{
		this.type = type;
		this.name = name;
		
		this.doubleValue = doubleValue;
	}
	
	//////////
	
	public Value(Type type, String name, String stringValue)
	{
		this.type = type;
		this.name = name;
		
		this.stringValue = stringValue;
	}
	
	//////////
	
	public Value(Type type, String name, Boolean booleanValue)
	{
		this.type = type;
		this.name = name;
		
		this.booleanValue = booleanValue;
	}
		
	//////////
	
	public Value(Type type, String name, Integer integerValue)
	{
		this.type = type;
		this.name = name;
		
		this.integerValue = integerValue;
	}
	
	//////////
	
	public Type getType()
	{
		return type;
	}
	
	//////////
	
	public String getName()
	{
		return name;
	}	
	
	//////////
	
	public Long getLongValue()
	{
		return longValue;
	}
	
	//////////
	
	public Double getDoubleValue()
	{
		return doubleValue;
	}
	
	//////////
	
	public String getStringValue()
	{
		return stringValue;
	}
	
	//////////
	
	public Boolean getBooleanValue()
	{
		return booleanValue;
	}
	
	//////////
	
	public Integer getIntegerValue()
	{
		return integerValue;
	}
	
	//////////
	
	public void setType(Type type) 
	{
		this.type = type;
	}

	//////////
	
	public void setName(String name) 
	{
		this.name = name;
	}

	//////////
	
	public void setLongValue(Long longValue) 
	{
		this.longValue = longValue;
	}

	//////////
	
	public void setDoubleValue(Double doubleValue) 
	{
		this.doubleValue = doubleValue;
	}

	//////////
	
	public void setStringValue(String stringValue) 
	{
		this.stringValue = stringValue;
	}

	//////////
	
	public void setBooleanValue(Boolean booleanValue) 
	{
		this.booleanValue = booleanValue;
	}

	//////////
	
	public void setIntegerValue(Integer integerValue) 
	{
		this.integerValue = integerValue;
	}
		
	//////////
	
	public boolean equals(Object object) // NOTE: Equals...
	{
		if (object == this)
		{
			return true;
		}
		
		if (object instanceof Value)
		{
			Value value = (Value) object;
			
			switch (type)
			{
				case LON: return (type.equals(value.type) && name.equals(value.name) && longValue.equals(value.longValue));
				case DOU: return (type.equals(value.type) && name.equals(value.name) && doubleValue.equals(value.doubleValue));	
				case STR: return (type.equals(value.type) && name.equals(value.name) && stringValue.equals(value.stringValue));
				case BOO: return (type.equals(value.type) && name.equals(value.name) && booleanValue.equals(value.booleanValue));
				case INT: return (type.equals(value.type) && name.equals(value.name) && integerValue.equals(value.integerValue));				
			}
		}
		
		return false;
	}
	
	//////////
	
	public int hashCode() // NOTE: Hash...
	{
		int result = 17;	
		
		result = 31 * result + (type == null ? 0 : type.hashCode());
		result = 31 * result + (name == null ? 0 : name.hashCode());
		result = 31 * result + (longValue == null ? 0 : longValue.hashCode());
		result = 31 * result + (doubleValue == null ? 0 : doubleValue.hashCode());
		result = 31 * result + (stringValue == null ? 0 : stringValue.hashCode());
		result = 31 * result + (booleanValue == null ? 0 : booleanValue.hashCode());
		result = 31 * result + (integerValue == null ? 0 : integerValue.hashCode());
	
		return result;
	}
	
	//////////
	
	public String toString() // NOTE: String...
	{
		String output = new String();
		
		output = output + type;
		output = output + " ";
		output = output + name;
		output = output + " ";
		output = output + Relation.EQU;
		output = output + " ";
		
		switch (type)
		{
			case LON: output = output + longValue; break;
			case DOU: output = output + doubleValue; break;
			case STR: output = output + stringValue; break;
			case BOO: output = output + booleanValue; break;
			case INT: output = output + integerValue; break;
		}
		
		return output;
	}
}