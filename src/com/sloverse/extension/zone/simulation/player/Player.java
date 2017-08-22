package com.sloverse.extension.zone.simulation.player;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.room.RoomCoordinate;
import com.sloverse.extension.zone.simulation.room.SloverseRoom;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class Player 
{
	private User sfsUser;
	private Transform playerTransform;
	private RoomCoordinate targetPosition;
	private SloverseRoom lastRoom;
	
	public Player(User user)
	{
		sfsUser = user;
		playerTransform = new Transform();
		targetPosition = null;
	}
	
	public User getSFSUser()
	{
		return sfsUser;
	}
	
	public Transform getTransform()
	{
		return playerTransform;
	}
	
	public void toSFSObject(ISFSObject data)
	{
		ISFSObject playerData = new SFSObject();
		
		playerData.putInt("id", sfsUser.getId());
		playerTransform.toSFSObject(playerData);
		data.putSFSObject("player", playerData);
	}
	
	public SloverseRoom getLastRoom()
	{
		return lastRoom;
	}
	
	public boolean hasTargetPosition()
	{
		return targetPosition != null;
	}
	
	public RoomCoordinate getTargetPosition()
	{
		return targetPosition;
	}
	
	public void updateLastRoom()
	{
		SloverseRoom newLastRoom = SloverseZoneExtension.zoneExtension.getWorld().getSloverseRoom(sfsUser.getLastJoinedRoom());
		
		if (newLastRoom != null)
		{
			lastRoom = newLastRoom;
			setPosition((float) (lastRoom.getSpawn().getXCoordinate() + 1.0f - (Math.random() * 2.0f)), (float) (lastRoom.getSpawn().getYCoordinate() + 1.0f - (Math.random() * 2.0f)));
		}
		else
		{
			SloverseZoneExtension.zoneExtension.trace("LAST ROOM WAS NULL. CANNOT SET SPAWN POINT.");
		}
	}
	
	//Used for spawn points and teleportation.
	private void setPosition(float x, float y)
	{
		SloverseZoneExtension.zoneExtension.trace("SETTING POSITION TO: " + x + ", " + y);
		
		Transform newPlayerTransform = new Transform(x, y, playerTransform.getZ(),
				playerTransform.getRotationX(), playerTransform.getRotationY(), playerTransform.getRotationZ());
		
		playerTransform = newPlayerTransform;
		
		SloverseZoneExtension.zoneExtension.eventManager.updatePlayerPosition(this);
	}
	
	public void setTargetPosition(RoomCoordinate newTargetPosition)
	{
		targetPosition = newTargetPosition;
	}
}
