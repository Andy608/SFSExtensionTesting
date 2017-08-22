package com.sloverse.extension.zone.handlers.request;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.player.Player;
import com.sloverse.extension.zone.simulation.room.SloverseRoom;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class SpawnPlayerInRoomRequestHandler extends BaseClientRequestHandler
{
	@Override
	public void handleClientRequest(User user, ISFSObject data)
	{
		SloverseZoneExtension zoneExtension = SloverseZoneExtension.zoneExtension;
		
		Player player = zoneExtension.getWorld().getPlayer(user);
		
		if (player == null)
		{
			zoneExtension.trace("Player is null. Unable to Spawn them in room.");
			return;
		}
		
		SloverseRoom room = player.getLastRoom();
		
		if (room == null)
		{
			//it is not a main map room and is a user generated room like a house or store
			zoneExtension.trace("SPAWN NEW PLAYER IN ROOM: Non-Main map rooms are currently not supported at this time.");
			trace("Joining the town...");
			try {
				zoneExtension.getApi().joinRoom(user, zoneExtension.getWorld().town.getRoom());
			} 
			catch (SFSJoinRoomException e) 
			{
				zoneExtension.trace("Error trying to redirect user to the town: " + e.getStackTrace());
			}
		}
		else
		{
			boolean isNewPlayer = room.addPlayer(zoneExtension.getWorld().getPlayer(user));
			
			if (isNewPlayer)
			{
				updateOtherPlayersInRoom(room, user);
			}	
		}
	}
	
	private void updateOtherPlayersInRoom(SloverseRoom room, User newUser)
	{
		for (Player player : room.getPlayers())
		{
			if (player.getSFSUser().getId() != newUser.getId())
			{
				SloverseZoneExtension.zoneExtension.eventManager.createPlayerInRoomEvent(player, newUser);
			}
		}
	}
}
