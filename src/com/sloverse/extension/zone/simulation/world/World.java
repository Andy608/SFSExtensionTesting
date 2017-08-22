package com.sloverse.extension.zone.simulation.world;

import java.util.ArrayList;
import java.util.List;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.player.Player;
import com.sloverse.extension.zone.simulation.room.RoomCoordinate;
import com.sloverse.extension.zone.simulation.room.SloverseRoom;
import com.sloverse.extension.zone.simulation.room.bounds.RoomBoundsList;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;

public class World 
{
	private List<Player> playersInWorld;
	
	//Includes user rooms list houses.
	private List<SloverseRoom> masterRoomsList;
	
	//Only includes main rooms like map rooms.
	private List<SloverseRoom> mainRoomsList;
	
	public SloverseRoom town;
	
	public World()
	{
		masterRoomsList = new ArrayList<SloverseRoom>();
		mainRoomsList = new ArrayList<SloverseRoom>();
		playersInWorld = new ArrayList<Player>();
		
		createMainRooms();
	}
	
	public void addPlayerToWorld(User user)
	{
		Player player = getPlayer(user);
		
		if (player == null)
		{
			player = new Player(user);
			playersInWorld.add(player);
			SloverseZoneExtension.zoneExtension.trace("Added user: " + user.getName() + " to the world!");
		}
	}
	
	public Player removePlayerFromWorld(User user)
	{
		Player player = getPlayer(user);
		
		if (player == null)
		{
			return null;
		}
		
		playersInWorld.remove(player);
		player.getLastRoom().removePlayer(player);
		SloverseZoneExtension.zoneExtension.trace("Removed user: " + user.getName() + " from the world.");
		return player;
	}
	
	public Player getPlayer(User user)
	{
		int userID = user.getId();
		
		for (Player player : playersInWorld)
		{
			if (player.getSFSUser().getId() == userID)
			{
				return player;
			}
		}
		
		return null;
	}
	
	private void createMainRooms()
	{
		Zone currentZone = SloverseZoneExtension.zoneExtension.getParentZone();
		
		town = new SloverseRoom("Town", 5, 0, true, 0, RoomBoundsList.townCenterBounds, new RoomCoordinate(2, 1));
		
		try 
		{
			town.initRoom(currentZone.getRoomByName(town.getRoomSettings().getName()) == null ? 
					SloverseZoneExtension.zoneExtension.getApi().createRoom(currentZone, town.getRoomSettings(), null, false, null, false, false) : 
				currentZone.getRoomByName(town.getRoomSettings().getName()));
		} 
		catch (SFSCreateRoomException e) 
		{
			SloverseZoneExtension.zoneExtension.trace("There was an error creating a room: " + e.getStackTrace());
		}
		
		addRoomToList(town, true);
	}
	
	public SloverseRoom getSloverseRoom(Room room)
	{
		//In the future make a list of rooms and then traverse the list
		
		if (room == null)
		{
			SloverseZoneExtension.zoneExtension.trace("FUCK WHY IS THE ROOM NULL. RETURNING DEFAULT ROOM");
			return town;
		}
		
		SloverseZoneExtension.zoneExtension.trace("HELLO THE LAST ROOM JOINED IS: " + room.getName());
		
		for (int i = 0; i < masterRoomsList.size(); ++i)
		{
			SloverseRoom sloverseRoom = masterRoomsList.get(i);
			if (room.getId() == sloverseRoom.getRoom().getId())
			{
				return sloverseRoom;
			}
		}
		
		return null;
	}
	
	private void addRoomToList(SloverseRoom room, boolean isMainRoom)
	{
		addRoomToMasterList(room);
		
		if (isMainRoom)
		{
			addRoomToMainList(room);			
		}
	}
	
	private void addRoomToMasterList(SloverseRoom room)
	{
		for (SloverseRoom r : masterRoomsList)
		{
			if (r.getRoom().getId() == room.getRoom().getId())
			{
				return;
			}
		}
		
		masterRoomsList.add(room);
	}
	
	private void addRoomToMainList(SloverseRoom room)
	{
		for (SloverseRoom r : mainRoomsList)
		{
			if (r.getRoom().getId() == room.getRoom().getId())
			{
				return;
			}
		}
		
		mainRoomsList.add(room);
	}
	
	public List<SloverseRoom> getMasterRoomsList()
	{
		return masterRoomsList;
	}
}
