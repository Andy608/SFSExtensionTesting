package com.sloverse.extension.zone.task;

import java.util.List;

import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.player.Player;
import com.sloverse.extension.zone.simulation.room.SloverseRoom;
import com.sloverse.extension.zone.simulation.world.World;

public class UpdatePlayerPositionsTask implements Runnable {

	private int cycles = 0;
	
	@Override
	public void run() 
	{
		SloverseZoneExtension.zoneExtension.trace("Running UpdatePlayerPositionsTask. Cycle #" + cycles);
		
		World world = SloverseZoneExtension.zoneExtension.getWorld();
		
		List<SloverseRoom> masterRoomsList = world.getMasterRoomsList();
		
		for (SloverseRoom currentRoom : masterRoomsList)
		{
			List<Player> playersInRoom = currentRoom.getPlayers();
			
			for (Player currentPlayer : playersInRoom)
			{
				if (currentPlayer.hasTargetPosition())
				{
					//move player towards target by TARGETSPEED * deltatime
					//add player id, and new transform to bundle
				}
			}
			
			//if bundle has stuff in it, send it
		}
		
		++cycles;
	}
}
