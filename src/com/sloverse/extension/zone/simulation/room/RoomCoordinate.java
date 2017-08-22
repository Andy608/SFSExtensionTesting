package com.sloverse.extension.zone.simulation.room;

public class RoomCoordinate 
{
	public static final float MAX_BOUND = 1.0f;
	public static final float MIN_BOUND = -1.0f;
	
	private float xCoordinate;
	private float yCoordinate;
	
	public RoomCoordinate(float x, float y) 
	{	
		xCoordinate = x;
		yCoordinate = y;
	}
	
	public RoomCoordinate(RoomCoordinate other)
	{
		xCoordinate = other.xCoordinate;
		yCoordinate = other.yCoordinate;
	}
	
	public float getXCoordinate()
	{
		return xCoordinate;
	}
	
	public float getYCoordinate()
	{
		return yCoordinate;
	}
	
	public void setXCoordinate(float x)
	{
		this.xCoordinate = x;
	}
	
	public void setYCoordinate(float y)
	{
		this.yCoordinate = y;
	}
}
