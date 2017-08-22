package com.sloverse.extension.zone.handlers.event;

import java.util.Arrays;
import java.util.List;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.util.SloverseSessionProperties;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class ZoneJoinEventHandler extends BaseServerEventHandler
{
	@Override
	public void handleServerEvent(ISFSEvent zoneJoinEvent) throws SFSException 
	{
		User loggedInUser = (User)zoneJoinEvent.getParameter(SFSEventParam.USER);
		
		UserVariable playerID = new SFSUserVariable("playerID", loggedInUser.getSession().getProperty(SloverseSessionProperties.PLAYER_ID));
		playerID.setHidden(true);
		
		List<UserVariable> userVars = Arrays.asList(playerID);
		this.getApi().setUserVariables(loggedInUser, userVars);
		
		SloverseZoneExtension.zoneExtension.getWorld().addPlayerToWorld(loggedInUser);
		
		Room town = SloverseZoneExtension.zoneExtension.getWorld().town.getRoom();
		
		if (town == null)
		{
			throw new SFSException("Town Room was not found.");
		}
		else
		{
			trace("Joining the town...");
			this.getApi().joinRoom(loggedInUser, town);
		}
	}
}
