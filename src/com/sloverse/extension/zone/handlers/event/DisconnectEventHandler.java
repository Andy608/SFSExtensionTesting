package com.sloverse.extension.zone.handlers.event;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.player.Player;
import com.sloverse.extension.zone.simulation.room.SloverseRoom;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class DisconnectEventHandler extends BaseServerEventHandler 
{
	@Override
	public void handleServerEvent(ISFSEvent event) throws SFSException
	{
		User user = (User) event.getParameter(SFSEventParam.USER);
		
		Player removedPlayer = SloverseZoneExtension.zoneExtension.getWorld().removePlayerFromWorld(user);
		SloverseRoom room = removedPlayer.getLastRoom();
		
		if (room == null)
		{
			//it is not a main map room and is a user generated room like a house or store
			SloverseZoneExtension.zoneExtension.trace("USER DISCONNECT: Non-main map rooms are currently not supported at this time.");
		}
		else
		{
			room.removePlayer(removedPlayer);
		}
	}
}
