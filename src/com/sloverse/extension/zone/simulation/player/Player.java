package com.sloverse.extension.zone.simulation.player;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.room.SloverseRoom;
import com.sloverse.extension.zone.util.math.Vec2;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class Player 
{
	public static final String BUNDLE_NAME = "player";
	public static final String BUNDLE_POSITION_NAME = "playerPosition";
	
	private User sfsUser;
	private PlayerPositionData playerPosition;
	private Vec2 targetPosition;
	private EnumPlayerDirection lookDirection;
	private boolean lerpToTarget;
	private SloverseRoom lastRoom;
	
	public Player(User user)
	{
		sfsUser = user;
		playerPosition = new PlayerPositionData();
		targetPosition = null;
		lookDirection = EnumPlayerDirection.SOUTH;
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
		
		boolean hasTarget = targetPosition != null;
		playerData.putBool("hasTarget", hasTarget);
		
		if (hasTarget)
		{			
			playerData.putFloat("targetX", targetPosition.x);
			playerData.putFloat("targetY", targetPosition.y);
		}
		
		playerData.putInt("dir", lookDirection.ordinal());
		
		playerPosition.toSFSObject(playerData);
		playerData.putBool("lerp", lerpToTarget);
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
	
	public Vec2 getTargetPosition()
	{
		return targetPosition;
	}
	
	public void setLerp(boolean lerp)
	{
		SloverseZoneExtension.zoneExtension.trace("SETTING LERP TO: " + lerp);
		lerpToTarget = lerp;
	}
	
	public boolean lerpToTarget()
	{
		return lerpToTarget;
	}
	
	public void updateLastRoom()
	{
		SloverseRoom newLastRoom = SloverseZoneExtension.zoneExtension.getWorld().getSloverseRoom(sfsUser.getLastJoinedRoom());
		
		if (newLastRoom != null)
		{
			lastRoom = newLastRoom;
			setLerp(false);
			setTargetPosition(new Vec2((float) (lastRoom.getSpawn().x + 0.1f - (Math.random() * 0.2f)), (float) (lastRoom.getSpawn().y + 0.1f - (Math.random() * 0.2f))));
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
	
	public void setTargetPosition(Vec2 newTargetPosition)
	{
		targetPosition = newTargetPosition;
	}
	
	public void setLookDirection(EnumPlayerDirection lookDir)
	{
		lookDirection = lookDir;
	}
	
	public EnumPlayerDirection getLookDirection()
	{
		return lookDirection;
	}
}
