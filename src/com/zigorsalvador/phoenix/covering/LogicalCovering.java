package com.zigorsalvador.phoenix.covering;

import java.util.Set;

import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.messages.Predicate;


public class LogicalCovering 
{
	protected static Boolean covers(Filter filter1, Filter filter2)
	{
		Set<Predicate> predicates1 = filter1.getPredicates();
		Set<Predicate> predicates2 = filter2.getPredicates();
		
		if (LocationCovering.covers(filter1, filter2))
		
		for (Predicate predicate1 : predicates1)
		{
			Boolean found = false;
			
			for (Predicate predicate2 : predicates2)
			{
				if (PredicateCovering.covers(predicate1, predicate2) == true)
				{
					found = true;
					break;
				}
			}
			
			if (found == false)
			{
				return false;
			}
		}
		
		return true;
	}
}