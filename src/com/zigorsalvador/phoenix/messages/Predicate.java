package com.zigorsalvador.phoenix.messages;

public class Predicate implements Comparable<Predicate>
{
	private Type type;
	private String name;
	private Constraint constraint;
		
	//////////
	
	public Predicate () {} // NOTE: Empty constructor...
	
	//////////
	
	public Predicate (Type type, String name, Constraint constraint)
	{
		this.type = type;
		this.name = name;
		this.constraint = constraint;
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
	
	public Constraint getConstraint()
	{
		return constraint;
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
	
	public void setConstraint(Constraint constraint) 
	{
		this.constraint = constraint;
	}
	
	//////////
	
	@Override
	
	public int compareTo(Predicate predicate) // NOTE: Comparable...
	{
		return toString().compareTo(predicate.toString());
	}
	
	//////////	
	
	public boolean equals(Object object) // NOTE: Equals...
	{
		if (object == this)
		{
			return true;
		}
		
		if (object instanceof Predicate)
		{
			Predicate predicate = (Predicate) object;
		
			return (type.equals(predicate.type) && name.equals(predicate.name) && constraint.equals(predicate.constraint));
		}
		
		return false;
	}
	
	//////////
	
	public int hashCode() // NOTE: Hash...
	{
		int result = 17;	
		
		result = 31 * result + (type == null ? 0 : type.hashCode());
		result = 31 * result + (name == null ? 0 : name.hashCode());
		result = 31 * result + (constraint == null ? 0 : constraint.hashCode());
	
		return result;
	}
	
	//////////
	
	public String toString() // NOTE: String...
	{
		String output = new String();
		
		output = output + name;
		output = output + " ";
		output = output + type;
		output = output + " ";
		output = output + constraint;
			
		return output;
	}
}