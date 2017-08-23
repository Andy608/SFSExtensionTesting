package com.sloverse.extension.zone.simulation.room;

import java.util.ArrayList;
import java.util.List;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.player.Player;
import com.sloverse.extension.zone.simulation.room.bounds.RoomBounds;
import com.sloverse.extension.zone.util.math.Vec2;
import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.entities.Room;

public class SloverseRoom 
{
	private RoomBounds roomBounds;
	private Room room;
	
	private CreateRoomSettings roomSettings;
	
	private Vec2 spawnPoint;
	
	private List<Player> playersInRoom;
	
	public SloverseRoom(String roomName, int maxUsers, int maxSpectators, boolean isGame, int maxVariables, RoomBounds bounds) 
	{
		this(roomName, maxUsers, maxSpectators, isGame, maxVariables, bounds, new Vec2(0.5f, 0.5f));
	}
	
	public SloverseRoom(String roomName, int maxUsers, int maxSpectators, boolean isGame, int maxVariables, RoomBounds bounds, Vec2 spawn) 
	{
		roomSettings = new CreateRoomSettings();
		roomSettings.setName(roomName);
		roomSettings.setMaxUsers(maxUsers);
		roomSettings.setMaxSpectators(maxSpectators);
		roomSettings.setGame(isGame);
		roomSettings.setMaxVariablesAllowed(maxVariables);
		
		roomBounds = bounds;
		spawnPoint = spawn;
		
		 playersInRoom = new ArrayList<Player>();
	}

	public CreateRoomSettings getRoomSettings()
	{
		return roomSettings;
	}
	
	public RoomBounds getRoomBounds()
	{
		return roomBounds;
	}
	
	public Room getRoom()
	{
		return room;
	}
	
	public Vec2 getSpawn()
	{
		return spawnPoint;
	}
	
	public List<Player> getPlayers()
	{
		return playersInRoom;
	}
	
	public void initRoom(Room room)
	{
		this.room = room;
	}
	
	public boolean addPlayer(Player p)
	{
		Player player = getPlayer(p);
		
		if (player == null)
		{
			playersInRoom.add(p);
			SloverseZoneExtension.zoneExtension.eventManager.createPlayerInRoomEvent(p);
			return true;
		}
		else
		{
			SloverseZoneExtension.zoneExtension.trace("Player: " + player.getSFSUser().getName() + " is already in this room.");
			return false;
		}
	}
	
	public void removePlayer(Player p)
	{
		Player player = getPlayer(p);
		
		if (player == null)
		{
			//SloverseZoneExtension.zoneExtension.trace("Player: " + p.getSFSUser().getName() + " is not in this room.");
			return;
		}
		
		playersInRoom.remove(player);
		SloverseZoneExtension.zoneExtension.trace("Removed player: " + player.getSFSUser().getName() + " from room.");
		
		//Send update to all clients to remove the player.
	}
	
	public Player getPlayer(Player p)
	{
		int playerID = p.getSFSUser().getId();
		
		for (Player player : playersInRoom)
		{
			if (player.getSFSUser().getId() == playerID)
			{
				return player;
			}
		}
		
		return null;
	}
}
