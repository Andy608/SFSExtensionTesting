package com.sloverse.extension.zone.handlers.request;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.player.Player;
import com.sloverse.extension.zone.simulation.room.SloverseRoom;
import com.sloverse.extension.zone.simulation.room.bounds.RoomBounds;
import com.sloverse.extension.zone.util.math.Vec2;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class UpdateTargetPositionRequestHandler extends BaseClientRequestHandler
{
	@Override
	public void handleClientRequest(User user, ISFSObject data)
	{
		Player player = SloverseZoneExtension.zoneExtension.getWorld().getPlayer(user);
		
		if (player == null)
		{
			SloverseZoneExtension.zoneExtension.trace("Player is null. Unable to set a target position.");
			return;
		}
		
		//Validate target position to make sure it's inside the room bounds.
		SloverseRoom room = player.getLastRoom();
		
		if (room == null)
		{
			SloverseZoneExtension.zoneExtension.trace("Room is null for player: " + user.getName() + ". Unable to set a target position.");
			return;
		}
		else
		{
			Vec2 requestedTargetPosition = new Vec2(data.getFloat("x"), data.getFloat("y"));
			RoomBounds bounds = room.getRoomBounds();
			
			Vec2 newTargetPosition = bounds.clampTargetToRoomBounds(requestedTargetPosition);
			
			player.setLerp(true);
			player.setTargetPosition(newTargetPosition);
			SloverseZoneExtension.zoneExtension.trace("Set new target position for player, " + user.getName() + " at: X = " + newTargetPosition.x + ", Y = " + newTargetPosition.y + " LERP: " + player.lerpToTarget());
		}
	}
}
