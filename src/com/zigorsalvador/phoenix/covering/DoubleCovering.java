package com.zigorsalvador.phoenix.covering;

import com.zigorsalvador.phoenix.messages.Constraint;
import com.zigorsalvador.phoenix.messages.Relation;

public class DoubleCovering
{
	protected static Boolean covers(Constraint constraint1, Constraint constraint2)
	{
		Relation relation1 = constraint1.getRelation();
		Relation relation2 = constraint2.getRelation();
		
		switch (relation1)
		{
			case EQU:
											
				switch (relation2)
				{
					case EQU: return constraint1.getDoubleValue().equals(constraint2.getDoubleValue());
					case LEQ: return false;
					case GEQ: return false;
					case NEQ: return false;
					case ANY: return false;
				}
			
			case LEQ: 
				
				switch (relation2)
				{
					case EQU: return constraint1.getDoubleValue() >= constraint2.getDoubleValue();
					case LEQ: return constraint1.getDoubleValue() >= constraint2.getDoubleValue();
					case GEQ: return false;
					case NEQ: return false;
					case ANY: return false;
				}
				
			case GEQ:
			
				switch (relation2)
				{
					case EQU: return constraint1.getDoubleValue() <= constraint2.getDoubleValue();
					case LEQ: return false;
					case GEQ: return constraint1.getDoubleValue() <= constraint2.getDoubleValue();
					case NEQ: return false;
					case ANY: return false;
				}
				
			case NEQ:
				
				switch (relation2)
				{
					case EQU: return ! constraint1.getDoubleValue().equals(constraint2.getDoubleValue());
					case LEQ: return constraint1.getDoubleValue() > constraint2.getDoubleValue();
					case GEQ: return constraint1.getDoubleValue() < constraint2.getDoubleValue();
					case NEQ: return constraint1.getDoubleValue().equals(constraint2.getDoubleValue());
					case ANY: return false;
				}
				
			case ANY:

				switch (relation2)
				{
					case EQU: return true;
					case LEQ: return true;
					case GEQ: return true;
					case NEQ: return true;
					case ANY: return true;
				}
		}
		
		return null;
	}
}