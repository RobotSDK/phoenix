package com.zigorsalvador.phoenix.utilities;

import com.zigorsalvador.phoenix.messages.Location;

public class DistanceFinder 
{	
	public static Double distance(Location location1, Location location2)
	{
		Double latitude1 = location1.getLatitude();
		Double longitude1 = location1.getLongitude();
		Double latitude2 = location2.getLatitude();
		Double longitude2 = location2.getLongitude();
		
		return distance(latitude1, longitude1, latitude2, longitude2);
	}
	
	//////////
	
	public static Double distance(Double latitude1, Double longitude1, Double latitude2, Double longitude2) 
	{
		Double polarRadius = 6356752.3;
		Double equatorialRadius = 6378137.0;
		Double meanRadius = (polarRadius + equatorialRadius) / 2;

	    Double latitudeDelta = Math.toRadians(latitude2 - latitude1);
	    Double longitudeDelta = Math.toRadians(longitude2 - longitude1);
	    
	    Double a = Math.sin(latitudeDelta / 2) * Math.sin(latitudeDelta/2) + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * Math.sin(longitudeDelta / 2) * Math.sin(longitudeDelta / 2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    
	    Double distance = meanRadius * c;

	    return distance;
    }
}