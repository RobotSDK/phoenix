package com.zigorsalvador.phoenix.messages;

public class Location
{
	private Double latitude;
	private Double longitude;
	private Double radius;
	
	//////////
	
	public Location () {} // NOTE: Empty constructor...
	
	//////////	
	
	public Location(Double latitude, Double longitude, Double radius)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.radius = radius;
	}
	
	//////////
	
	public Double getLatitude()
	{
		return latitude;
	}
	
	//////////
	
	public Double getLongitude()
	{
		return longitude;
	}
	
	//////////
	
	public Double getRadius()
	{
		return radius;
	}
	
	//////////
	
	public void setLatitude(Double latitude) 
	{
		this.latitude = latitude;
	}
	
	//////////

	public void setLongitude(Double longitude) 
	{
		this.longitude = longitude;
	}
	
	//////////

	public void setRadius(Double radius) 
	{
		this.radius = radius;
	}
	
	//////////
	
	public boolean equals(Object object) // NOTE: Equals...
	{
		if (object == this)
		{
			return true;
		}
		
		if (object instanceof Location)
		{
			Location location = (Location) object;
		
			return (latitude.equals(location.latitude) && longitude.equals(location.longitude) && radius.equals(location.radius));
		}

		return false;
	}
	
	//////////
	
	public int hashCode() // NOTE: Hash...
	{
		int result = 17;	
		
		result = 31 * result + (latitude == null ? 0 : latitude.hashCode());
		result = 31 * result + (longitude == null ? 0 : longitude.hashCode());
		result = 31 * result + (radius == null ? 0 : radius.hashCode());
	
		return result;
	}
	
	//////////
	
	public String toString()  // NOTE: String...
	{
		String output = new String();
		
		output = output + latitude;
		output = output + " ";
		output = output + longitude;
		output = output + " ";
		output = output + radius;
		
		return output;
	}
}