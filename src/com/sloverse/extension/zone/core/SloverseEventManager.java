package com.sloverse.extension.zone.core;

import java.util.List;

import com.sloverse.extension.zone.simulation.player.Player;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class SloverseEventManager 
{
	private static final String SPAWN_PLAYER = "spawnPlayer";
	private static final String UPDATE_PLAYER_POSITION = "updatePlayerActions";
	private static final String REMOVE_DUPLICATE_PLAYER = "removeDuplicatePlayer";
	
	//Send init new player message to all clients
	public void createPlayerInRoomEvent(Player player)
	{
		createPlayerInRoomEvent(player, null);
	}
	
	//Send init new player message to a specific target user
	public void createPlayerInRoomEvent(Player player, User target)
	{
		ISFSObject data = new SFSObject();
		player.toSFSObject(data);
		
		Room currentRoom = player.getLastRoom().getRoom();
		
		if (target == null)
		{
			List<User> userList = currentRoom.getPlayersList();
			SloverseZoneExtension.zoneExtension.send(SPAWN_PLAYER, data, userList);
		}
		else
		{
			SloverseZoneExtension.zoneExtension.send(SPAWN_PLAYER, data, target);
		}
	}
	
	public void updatePlayerPosition(Player playerChanged)
	{
		ISFSObject data = new SFSObject();
		playerChanged.toSFSObject(data);
		
		Room currentRoom = playerChanged.getLastRoom().getRoom();
		
		SloverseZoneExtension.zoneExtension.trace("UPDATE PLAYER POSITION METHOD: " + data.getSFSObject("player").getSFSObject("transform").getFloat("x") + ", " + data.getSFSObject("player").getSFSObject("transform").getFloat("y"));
		
		List<User> userList = currentRoom.getPlayersList();
		SloverseZoneExtension.zoneExtension.send(UPDATE_PLAYER_POSITION, data, userList);
	}
	
	public void removeDuplicatePlayer(Player playerToKick)
	{
		ISFSObject data = new SFSObject();
		data.putInt("id", playerToKick.getSFSUser().getId());
		
		Room currentRoom = playerToKick.getLastRoom().getRoom();
		
		SloverseZoneExtension.zoneExtension.trace("KICK PLAYER: " + playerToKick.getSFSUser().getName());
		
		List<User> userList = currentRoom.getPlayersList();
		SloverseZoneExtension.zoneExtension.send(REMOVE_DUPLICATE_PLAYER, data, userList);
	}
}
