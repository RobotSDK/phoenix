package com.zigorsalvador.phoenix.covering;

import com.zigorsalvador.phoenix.messages.Filter;

public class FilterCovering
{
	public static Boolean covers(Filter filter1, Filter filter2)
	{
		return LocationCovering.covers(filter1, filter2) && LogicalCovering.covers(filter1, filter2);
	}
}