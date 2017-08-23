package com.sloverse.extension.zone.core;

import com.sloverse.extension.zone.handlers.event.DisconnectEventHandler;
import com.sloverse.extension.zone.handlers.event.JoinRoomEventHandler;
import com.sloverse.extension.zone.handlers.event.LeaveRoomEventHandler;
import com.sloverse.extension.zone.handlers.event.LoginEventHandler;
import com.sloverse.extension.zone.handlers.event.ServerReadyEventHandler;
import com.sloverse.extension.zone.handlers.event.ZoneJoinEventHandler;
import com.sloverse.extension.zone.handlers.request.SpawnPlayerInRoomRequestHandler;
import com.sloverse.extension.zone.handlers.request.UpdatePlayerDirectionRequestHandler;
import com.sloverse.extension.zone.handlers.request.UpdateTargetPositionRequestHandler;
import com.sloverse.extension.zone.simulation.world.World;
import com.sloverse.extension.zone.util.SloverseRequestType;
import com.sloverse.extension.zone.util.SloverseZoneInfo;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class SloverseZoneExtension extends SFSExtension
{
	public static SloverseZoneExtension zoneExtension;
	
	public SloverseEventManager eventManager;
	public SloverseTaskManager taskManager;
	
	private World world;
	
	@Override
	public void init() 
	{
		zoneExtension = this;
		trace("Starting " + this.getName() + ", Version: " + SloverseZoneInfo.EXT_VERSION);
		
		eventManager = new SloverseEventManager();
		taskManager = new SloverseTaskManager();
		world = new World();
		
		this.addRequestHandler(SloverseRequestType.SPAWN_PLAYER_IN_ROOM, SpawnPlayerInRoomRequestHandler.class);
		this.addRequestHandler(SloverseRequestType.UPDATE_TARGET_POSITION, UpdateTargetPositionRequestHandler.class);
		this.addRequestHandler(SloverseRequestType.UPDATE_PLAYER_DIRECTION, UpdatePlayerDirectionRequestHandler.class);
		
		this.addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		this.addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class);
		this.addEventHandler(SFSEventType.USER_JOIN_ROOM, JoinRoomEventHandler.class);
		this.addEventHandler(SFSEventType.USER_DISCONNECT, DisconnectEventHandler.class);
		this.addEventHandler(SFSEventType.USER_LEAVE_ROOM, LeaveRoomEventHandler.class);
		this.addEventHandler(SFSEventType.USER_LOGOUT, DisconnectEventHandler.class);
		this.addEventHandler(SFSEventType.SERVER_READY, ServerReadyEventHandler.class);
		
		trace(this.getName() + " initialized!");
	}
	
	public World getWorld()
	{
		return world;
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		taskManager.stopAllTasks();
	}
}
