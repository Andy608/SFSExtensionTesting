package com.sloverse.extension.zone.simulation.player;

import com.sloverse.extension.zone.simulation.room.RoomCoordinate;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class PlayerPositionData 
{	
	private RoomCoordinate roomPosition;
	
	public PlayerPositionData()
	{
		//positioned center below screen (viewport is (-1, -1) to (1, 1) )
		this(0, -2);
	}
	
	public PlayerPositionData(float x, float y)
	{
		roomPosition = new RoomCoordinate(x, y);
	}
	
	public RoomCoordinate getRoomPosition()
	{
		return roomPosition;
	}
	
	public static PlayerPositionData fromSFSObject(ISFSObject data)
	{
		ISFSObject positionData = data.getSFSObject(Player.BUNDLE_POSITION_NAME);
		
		float x = positionData.getFloat("x");
		float y = positionData.getFloat("y");
		
		return new PlayerPositionData(x, y);
	}
	
	public void toSFSObject(ISFSObject data)
	{
		ISFSObject positionData = new SFSObject();
		positionData.putFloat("x", roomPosition.x);
		positionData.putFloat("y", roomPosition.y);
		
		data.putSFSObject(Player.BUNDLE_POSITION_NAME, positionData);
	}
	
	public void override(PlayerPositionData another)
	{
		this.roomPosition = new RoomCoordinate(another.roomPosition);
	}
}
