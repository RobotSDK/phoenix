package com.zigorsalvador.phoenix.matching;

import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Filter;

public class EventMatching
{
	public static Boolean matches(Event event, Filter filter)
	{
		return LocationMatching.matches(event, filter) && LogicalMatching.matches(event, filter);
	}
}