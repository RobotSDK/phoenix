package com.zigorsalvador.phoenix.matching;

import java.util.Map;
import java.util.Set;

import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.messages.Predicate;
import com.zigorsalvador.phoenix.messages.Value;


public class LogicalMatching
{
	protected static Boolean matches(Event event, Filter filter)
	{														
		Map<String, Value> values = event.getValues();
		Set<Predicate> predicates = filter.getPredicates();

		for (Predicate predicate : predicates)
		{
			Value value = values.get(predicate.getName());
			
			if (value == null)
			{										
				return false;	
			}
			else if (predicate.getType() != value.getType())
			{												
				return false;
			}
			else
			{														
				if (ValueMatching.matches(predicate.getType(), predicate.getConstraint(), value) == false)
				{
					return false;
				}
			}
		}
		
		return true;		
	}
}