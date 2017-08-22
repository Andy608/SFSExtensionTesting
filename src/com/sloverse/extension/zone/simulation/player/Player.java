package com.sloverse.extension.zone.simulation.player;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.room.RoomCoordinate;
import com.sloverse.extension.zone.simulation.room.SloverseRoom;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class Player 
{
	public static final String BUNDLE_NAME = "player";
	public static final String BUNDLE_POSITION_NAME = "playerPosition";
	
	private User sfsUser;
	private PlayerPositionData playerPosition;
	private RoomCoordinate targetPosition;
	private boolean teleportToTarget;
	private SloverseRoom lastRoom;
	
	public Player(User user)
	{
		sfsUser = user;
		playerPosition = new PlayerPositionData();
		targetPosition = null;
	}
	
	public User getSFSUser()
	{
		return sfsUser;
	}
	
	public PlayerPositionData getPositionInRoom()
	{
		return playerPosition;
	}
	
	public void toSFSObject(ISFSObject data)
	{
		ISFSObject playerData = new SFSObject();
		
		playerData.putInt("id", sfsUser.getId());
		playerPosition.toSFSObject(playerData);
		data.putSFSObject(BUNDLE_NAME, playerData);
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
	
	public boolean isTargetPositionInstant()
	{
		return teleportToTarget;
	}
	
	public void updateLastRoom()
	{
		SloverseRoom newLastRoom = SloverseZoneExtension.zoneExtension.getWorld().getSloverseRoom(sfsUser.getLastJoinedRoom());
		
		if (newLastRoom != null)
		{
			lastRoom = newLastRoom;
			setTargetPosition(new RoomCoordinate((float) (lastRoom.getSpawn().x + 1.0f - (Math.random() * 2.0f)), (float) (lastRoom.getSpawn().y + 1.0f - (Math.random() * 2.0f))), true);
		}
		else
		{
			SloverseZoneExtension.zoneExtension.trace("LAST ROOM WAS NULL. CANNOT SET SPAWN POINT.");
		}
	}
	
	//Used for spawn points and teleportation.
	/*private void setPosition(float x, float y)
	{
		SloverseZoneExtension.zoneExtension.trace("SETTING POSITION TO: " + x + ", " + y);
		
		PlayerPositionData newPlayerPosition = new PlayerPositionData(x, y);
		
		playerPosition = newPlayerPosition;
		
		SloverseZoneExtension.zoneExtension.eventManager.updatePlayerPosition(this);
	}*/
	
	public void setTargetPosition(RoomCoordinate newTargetPosition, boolean isInstant)
	{
		targetPosition = newTargetPosition;
		teleportToTarget = isInstant;
	}
}
