package com.zigorsalvador.phoenix.covering;

import com.zigorsalvador.phoenix.messages.Constraint;
import com.zigorsalvador.phoenix.messages.Predicate;

public class PredicateCovering
{
	protected static Boolean covers(Predicate predicate1, Predicate predicate2)
	{
		if (predicate1.getName().equals(predicate2.getName()) && predicate1.getType().equals(predicate2.getType()))
		{
			Constraint constraint1 = predicate1.getConstraint();
			Constraint constraint2 = predicate2.getConstraint();
			
			switch (predicate1.getType())
			{
				case INT: return IntegerCovering.covers(constraint1, constraint2); 
				case LON: return LongCovering.covers(constraint1, constraint2); 
				case DOU: return DoubleCovering.covers(constraint1, constraint2); 
				case BOO: return BooleanCovering.covers(constraint1, constraint2); 
				case STR: return StringCovering.covers(constraint1, constraint2); 
			}
		}
		
		return false;
	}
}