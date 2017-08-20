package com.sloverse.extension.zone.core;

import com.sloverse.extension.zone.login.LoginEventHandler;
import com.sloverse.extension.zone.login.ZoneJoinEventHandler;
import com.sloverse.extension.zone.util.SloverseZoneInfo;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class SloverseZoneExtension extends SFSExtension
{
	@Override
	public void init() 
	{
		trace("Starting " + this.getName() + ", Version: " + SloverseZoneInfo.EXT_VERSION);
		
		this.addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
		this.addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class);
	}
}
