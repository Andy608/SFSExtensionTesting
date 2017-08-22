package com.sloverse.extension.zone.simulation.room;

public class RoomCoordinate 
{
	public static final float MAX_BOUND = 1.0f;
	public static final float MIN_BOUND = -1.0f;
	
	public float x;
	public float y;
	
	public RoomCoordinate(float x, float y) 
	{	
		this.x = x;
		this.y = y;
	}
	
	public RoomCoordinate(RoomCoordinate other)
	{
		x = other.x;
		y = other.y;
	}
	
	public double distanceTo(RoomCoordinate another) 
	{
		double dx = Math.pow(this.x - another.x, 2);
		double dy = Math.pow(this.y - another.y, 2);
		return Math.sqrt(dx + dy);
	}
}
