package com.zigorsalvador.phoenix.covering;

import com.zigorsalvador.phoenix.messages.Filter;
import com.zigorsalvador.phoenix.messages.Location;
import com.zigorsalvador.phoenix.utilities.DistanceFinder;


public class LocationCovering 
{
	protected static Boolean covers(Filter filter1, Filter filter2)
	{
		Location location1 = filter1.getLocation();
		Location location2 = filter2.getLocation();
		
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
		
		if (location1.getRadius() >= location2.getRadius() + distance)
		{
			return true;
		}
		
		return false;
	}
}