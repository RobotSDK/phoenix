package com.zigorsalvador.phoenix.messages;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL) // NOTE: Jackson annotation...

public class Constraint
{	
	private Relation relation;
	
	//////////

	private Long longValue;
	private Double doubleValue;
	private String stringValue;
	private Boolean booleanValue;
	private Integer integerValue;
	
	//////////
	
	public Constraint() {} // NOTE: Empty constructor...
	
	//////////
	
	public Constraint(Relation relation)
	{
		this.relation = relation;
	}

	//////////

	public Constraint(Relation relation, Long longValue)
	{
		this.relation = relation;
		this.longValue = longValue;
	}

	//////////
	
	public Constraint(Relation relation, Double doubleValue)
	{
		this.relation = relation;
		this.doubleValue = doubleValue;
	}
	
	//////////

	public Constraint(Relation relation, String stringValue)
	{
		this.relation = relation;
		this.stringValue = stringValue;
	}

	//////////
	
	public Constraint(Relation relation, Boolean booleanValue)
	{
		this.relation = relation;
		this.booleanValue = booleanValue;
	}

	//////////
	
	public Constraint(Relation relation, Integer integerValue)
	{
		this.relation = relation;
		this.integerValue = integerValue;
	}

	//////////
	
	public Relation getRelation()
	{
		return relation;
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
	
	public void setRelation(Relation relation)
	{
		this.relation = relation;
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
		
		if (object instanceof Constraint)
		{
			Constraint constraint = (Constraint) object;
			
			if (longValue != null) return (relation.equals(constraint.relation) && longValue.equals(constraint.longValue));
			if (doubleValue != null) return (relation.equals(constraint.relation) && doubleValue.equals(constraint.doubleValue));
			if (stringValue != null) return (relation.equals(constraint.relation) && stringValue.equals(constraint.stringValue));
			if (booleanValue != null) return (relation.equals(constraint.relation) && booleanValue.equals(constraint.booleanValue));
			if (integerValue != null) return (relation.equals(constraint.relation) && integerValue.equals(constraint.integerValue));
			
			return (relation.equals(constraint.relation));
		}
		
		return false;
	}
	
	//////////
	
	public int hashCode() // NOTE: Hash...
	{
		int result = 17;	
		
		result = 31 * result + (relation == null ? 0 : relation.hashCode());
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
		
		output = output + relation;
		output = output + " ";
	
		if (longValue != null) output = output + longValue;
		if (doubleValue != null) output = output + doubleValue;
		if (stringValue != null) output = output + stringValue;
		if (booleanValue != null) output = output + booleanValue;
		if (integerValue != null) output = output + integerValue;
		
		if (relation == Relation.ANY) output = output + null;
		
		return output;
	}
}