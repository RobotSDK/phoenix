package com.zigorsalvador.phoenix.matching;

import com.zigorsalvador.phoenix.messages.Event;
import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.messages.Location;
import com.zigorsalvador.phoenix.utilities.DistanceFinder;


public class LocationMatching 
{
	protected static Boolean matches(Event event, Filter filter)
	{
		Location location1 = event.getLocation();
		Location location2 = filter.getLocation();
		
		if (location1 == null && location2 == null)
		{
			return true;
		}
		else if (location1 == null && location2 != null)
		{
			return false;
		}
		else if (location1 != null && location2 == null)
		{
			return false;
		}
		
		Double distance = DistanceFinder.distance(location1, location2);
		
		if (location1.getRadius() + location2.getRadius() >= distance)
		{
			return true;
		}
		
		return false;
	}
}