package com.example.binloc;

import android.graphics.PointF;

public class Bin implements Comparable{
	private double lat, lon;
	private int types;
	public static final int LitterMask = 0B001, RecyclingMask = 0B010, GreenMask = 0B100, PersonMask = 0B1000;
	
	public Bin(double lat, double lon, int types)
	{
		this.lat = lat;
		this.lon = lon;
		this.types = types;
	}
	
	@Override
	public int compareTo(Object other) {
		Bin b2 = (Bin)other;
		return (int) Math.copySign(1, MainActivity.myLocation.getDist(this, 0)-MainActivity.myLocation.getDist(b2, 0));
	}
	
	
	
	/**
	 * calculates the approximate distance within an error of 
	 * points should be in the form x = latitude, y = longitude
	 * @param p1
	 * @param p2
	 * @param technique
	 * 		controls which technique is used to calculate distance.
	 * 		0 will use pythagorean, 1 haversine, 2 Spherical law of cosines 
	 * @return distance
	 */
	public double getDist(Bin p2, int technique)
	{
		int R = 6371000; // metres
		double lat1 = Math.toRadians(this.getLat()), lat2 = Math.toRadians(p2.getLat());
		double deltaLat = Math.toRadians(lat2-lat1);
		double lon1 = Math.toRadians(this.getLon()), lon2 = Math.toRadians(p2.getLon());
		double deltaLon = Math.toRadians(lon2-lon1);
		
		double d = 0;
		switch(technique){
			case 0: //pythagorean
			{
				double x = (lon2-lon1) * Math.cos((lat1+lat2)/2);
				double y = (lat2-lat1);
				d = Math.sqrt(x*x + y*y) * R;
				break;
			}
			case 1: //haversine
			{
				double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
				        Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
				double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
				d = R * c;
				break;
			}
			case 2: //Spherical Law of Cosines
			{
				d = Math.acos( Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2) * Math.cos(deltaLon) ) * R;
			}
		}
		return d;
	}
	
	public double getLat() { return lat; }
	public double getLon() { return lon; }
	
	public int getTypes() {return types; }
	public boolean isLitter() { return (types&LitterMask) != 0; }
	public boolean isRecycling() { return (types&RecyclingMask) != 0; }
	public boolean isGreenBin() { return (types&GreenMask) != 0; }
	public boolean isPerson() { return (types&PersonMask) != 0; }
}
