package com.sloverse.extension.zone.handlers.request;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.player.EnumPlayerDirection;
import com.sloverse.extension.zone.simulation.player.Player;
import com.sloverse.extension.zone.simulation.room.SloverseRoom;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class UpdatePlayerDirectionRequestHandler extends BaseClientRequestHandler
{
	@Override
	public void handleClientRequest(User user, ISFSObject data)
	{
		Player player = SloverseZoneExtension.zoneExtension.getWorld().getPlayer(user);
		
		if (player == null)
		{
			SloverseZoneExtension.zoneExtension.trace("Player is null. Unable to set a look direction.");
			return;
		}
		else
		{
			SloverseRoom room = player.getLastRoom();
			
			if (room == null)
			{
				SloverseZoneExtension.zoneExtension.trace("Room cannot be null...");
				return;
			}
			
			int lookDirection = data.getInt("dir");
			
			if (lookDirection < 0 || lookDirection >= EnumPlayerDirection.values().length)
			{
				SloverseZoneExtension.zoneExtension.trace("Invalid look direction...");
				return;
			}
			else
			{
				SloverseZoneExtension.zoneExtension.trace("UPDATING DIRECTION FOR USER: " + player.getSFSUser().getName());
				player.setLookDirection(EnumPlayerDirection.values()[lookDirection]);
				updateOtherPlayersInRoom(room, player);
			}
		}
	}
	
	private void updateOtherPlayersInRoom(SloverseRoom room, Player client)
	{
		int userID = client.getSFSUser().getId();
		
		for (Player player : room.getPlayers())
		{
			if (player.getSFSUser().getId() != userID)
			{
				SloverseZoneExtension.zoneExtension.eventManager.updatePlayerDirection(client, player.getSFSUser());
			}
		}
	}
}
