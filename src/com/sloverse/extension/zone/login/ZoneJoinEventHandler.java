package com.sloverse.extension.zone.login;

import java.util.Arrays;
import java.util.List;

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
		
		Room lobby = this.getParentExtension().getParentZone().getRoomByName("Lobby");
		
		if (lobby == null)
		{
			throw new SFSException("Lobby Room was not found.");
		}
		else
		{
			trace("Joining the Lobby...");
			this.getApi().joinRoom(loggedInUser, lobby);
		}
	}
}
