package com.zigorsalvador.phoenix.messages;

import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL) // NOTE: Jackson annotation...

public class Filter implements Comparable<Filter>
{
	private Location location;
	private TreeSet<Predicate> predicates;
	
	//////////
	
	public Filter () // NOTE: Empty constructor...
	{
		predicates = new TreeSet<Predicate>();
	}
	
	//////////
	
	public Location getLocation()
	{
		return location;
	}	
	
	//////////
	
	public Set<Predicate> getPredicates()
	{
		return predicates;
	}
	
	//////////
	
	public void setLocation(Location location)
	{
		this.location = location;
	}
	
	//////////
	
	public void addPredicate(Predicate predicate)
	{
		predicates.add(predicate);
	}
	
	//////////
		
	@Override
	
	public int compareTo(Filter filter) // NOTE: Comparable...
	{
		return toString().compareTo(filter.toString());
	}
	
	//////////

	public boolean equals(Object object) // NOTE: Equals...
	{
		if (object == this)
		{
			return true;
		}
		
		if (object instanceof Filter)
		{
			Filter filter = (Filter) object;

			if (location == null && filter.location == null)
			{
				return predicates.equals(filter.predicates);
			}
			if (location != null && filter.location != null)
			{
				return (location.equals(filter.location) && predicates.equals(filter.predicates));
			}
		}

		return false;
	}
	
	//////////
	
	public int hashCode() // NOTE: Hash...
	{
		int result = 17;	
		
		result = 31 * result + (location == null ? 0 : location.hashCode());
		result = 31 * result + (predicates == null ? 0 : predicates.hashCode());
	
		return result;
	}
	
	//////////
	
	public String toString() // NOTE: String...
	{
		String output = new String();
		
		output = output + location;
		output = output + " ";
		
		for (Predicate predicate : predicates)
		{			
			output = output + predicate;
			output = output + " ";
		}
		
		output = output.substring(0, output.length() - 1);
		
		return output;
	}
}