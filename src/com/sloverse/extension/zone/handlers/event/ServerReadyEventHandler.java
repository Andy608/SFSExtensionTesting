package com.sloverse.extension.zone.handlers.event;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;

public class ServerReadyEventHandler extends BaseServerEventHandler
{	
	@Override
	public void handleServerEvent(ISFSEvent serverReadyEvent) throws SFSException 
	{
		SloverseZoneExtension.zoneExtension.trace("Initializing server timed tasks...");
		SloverseZoneExtension.zoneExtension.taskManager.initTasks();
	}
}
