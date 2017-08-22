package com.sloverse.extension.zone.simulation.player;

import com.sloverse.extension.zone.simulation.room.RoomCoordinate;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class Transform 
{
	private RoomCoordinate roomPosition;
	private float z;
	
	private float rx;
	private float ry;
	private float rz;
	
	private long timeStamp = 0;
	
	public Transform()
	{
		this(0, 0, 0, 0, 0, 0);
	}
	
	public Transform(float x, float y, float z, float rx, float ry, float rz)
	{
		roomPosition = new RoomCoordinate(x, y);
		this.z = z;
		
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}
	
	public float getRotationX()
	{
		return rx;
	}
	
	public float getRotationY()
	{
		return ry;
	}
	
	public float getRotationZ()
	{
		return rz;
	}
	
	public float getX()
	{
		return roomPosition.getXCoordinate();
	}
	
	public float getY()
	{
		return roomPosition.getYCoordinate();
	}
	
	public float getZ()
	{
		return z;
	}
	
	public RoomCoordinate getRoomPosition()
	{
		return roomPosition;
	}
	
	public void setTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	public long getTimeStamp() 
	{
		return timeStamp;
	}
	
	public static Transform fromSFSObject(ISFSObject data)
	{
		ISFSObject transformData = data.getSFSObject("transform");
		
		float x = transformData.getFloat("x");
		float y = transformData.getFloat("y");
		float z = transformData.getFloat("z");
		
		float rx = transformData.getFloat("rx");
		float ry = transformData.getFloat("ry");
		float rz = transformData.getFloat("rz");
		
		long t = transformData.getLong("t");
		
		Transform transform = new Transform(x, y, z, rx, ry, rz);
		transform.setTimeStamp(t);
		
		return transform;
	}
	
	public void toSFSObject(ISFSObject data) 
	{
		ISFSObject transformData = new SFSObject();
		transformData.putFloat("x", roomPosition.getXCoordinate());
		transformData.putFloat("y", roomPosition.getYCoordinate());
		transformData.putFloat("z", z);

		transformData.putFloat("rx", rx);
		transformData.putFloat("ry", ry);
		transformData.putFloat("rz", rz);

		transformData.putLong("t", this.timeStamp);

		data.putSFSObject("transform", transformData);
	}
	
	public void override(Transform another)
	{
		this.roomPosition = new RoomCoordinate(another.roomPosition);
		this.z = another.z;
		
		this.rx = another.rx;
		this.ry = another.ry;
		this.rz = another.rz;
		
		this.setTimeStamp(another.getTimeStamp());
	}
	
	public double distanceTo(Transform transform) 
	{
		double dx = Math.pow(this.getX() - transform.getX(), 2);
		double dy = Math.pow(this.getY() - transform.getY(), 2);
		double dz = Math.pow(this.getZ() - transform.getZ(), 2);
		return Math.sqrt(dx + dy + dz);
	}
}
