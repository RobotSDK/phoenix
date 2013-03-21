package com.zigorsalvador.phoenix.matching;

import com.zigorsalvador.phoenix.messages.Constraint;
import com.zigorsalvador.phoenix.messages.Relation;
import com.zigorsalvador.phoenix.messages.Type;
import com.zigorsalvador.phoenix.messages.Value;

public class ValueMatching 
{
	protected static Boolean matches(Type type, Constraint constraint, Value value)
	{										
		Relation relation = constraint.getRelation();
		
		switch (type)
		{
			case LON:
			
				switch (relation)
				{
					case EQU: return constraint.getLongValue().equals(value.getLongValue());
					case LEQ: return constraint.getLongValue() >= value.getLongValue();
					case GEQ: return constraint.getLongValue() <= value.getLongValue();
					case NEQ: return ! constraint.getLongValue().equals(value.getLongValue());
					case ANY: return true;
				}
		
			case DOU:
			
				switch (relation)
				{
					case EQU: return constraint.getDoubleValue().equals(value.getDoubleValue());
					case LEQ: return constraint.getDoubleValue() >= value.getDoubleValue();
					case GEQ: return constraint.getDoubleValue() <= value.getDoubleValue();
					case NEQ: return ! constraint.getDoubleValue().equals(value.getDoubleValue());
					case ANY: return true;
				}
				
			case STR:
	
				switch (relation)
				{
					case EQU: return constraint.getStringValue().equals(value.getStringValue());
					case LEQ: return false;
					case GEQ: return false;
					case NEQ: return ! constraint.getStringValue().equals(value.getStringValue());
					case ANY: return true;
				}
				
			case BOO:
				
				switch (relation)
				{
					case EQU: return constraint.getBooleanValue().equals(value.getBooleanValue());
					case LEQ: return false;
					case GEQ: return false;
					case NEQ: return ! constraint.getBooleanValue().equals(value.getBooleanValue());
					case ANY: return true;
				}
				
			case INT:
				
				switch (relation)
				{
					case EQU: return constraint.getIntegerValue().equals(value.getIntegerValue());
					case LEQ: return constraint.getIntegerValue() >= value.getIntegerValue();
					case GEQ: return constraint.getIntegerValue() <= value.getIntegerValue();
					case NEQ: return ! constraint.getIntegerValue().equals(value.getIntegerValue());
					case ANY: return true;
				}
		}
	
		return null;
	}
}