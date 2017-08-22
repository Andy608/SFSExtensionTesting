package com.sloverse.extension.zone.task;

import java.util.ArrayList;
import java.util.List;

import com.sloverse.extension.zone.core.SloverseTaskManager;
import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.player.Player;
import com.sloverse.extension.zone.simulation.room.RoomCoordinate;
import com.sloverse.extension.zone.simulation.room.SloverseRoom;
import com.sloverse.extension.zone.simulation.world.World;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class UpdatePlayerPositionsTask implements Runnable {

	//private int cycles = 0;
	
	private static final String UPDATE_PLAYERS_POSITION_BUNDLE = "updatePlayersPositionBundle";
	
	private static List<Player> playersToUpdate = new ArrayList<Player>();
	
	private SloverseZoneExtension extension = SloverseZoneExtension.zoneExtension;
	
	@Override
	public void run() 
	{
		//SloverseZoneExtension.zoneExtension.trace("Running UpdatePlayerPositionsTask. Cycle #" + cycles);
		
		World world = extension.getWorld();
		
		List<SloverseRoom> masterRoomsList = world.getMasterRoomsList();
		
		for (SloverseRoom currentRoom : masterRoomsList)
		{
			List<Player> playersInRoom = currentRoom.getPlayers();
			
			for (Player currentPlayer : playersInRoom)
			{
				if (currentPlayer.hasTargetPosition())
				{
					RoomCoordinate currentPosition = currentPlayer.getPositionInRoom().getRoomPosition();
					RoomCoordinate targetPosition = currentPlayer.getTargetPosition();
					
					if (currentPlayer.isTargetPositionInstant())
					{
						currentPlayer.getPositionInRoom().getRoomPosition().x = targetPosition.x;
						currentPlayer.getPositionInRoom().getRoomPosition().y = targetPosition.y;
					}
					else
					{
						//Use vector eventually.
						RoomCoordinate slope = new RoomCoordinate(targetPosition.x - currentPosition.x, targetPosition.y - currentPosition.y);
						
						//move player towards target by TARGETSPEED * deltatime
						RoomCoordinate velocity = new RoomCoordinate(0, 0);
						velocity.x = slope.x * 1.0f * SloverseTaskManager.DELTA;
						velocity.y = slope.y * 1.0f * SloverseTaskManager.DELTA;
						
						extension.trace("SLOPE: " + slope.x + ", " + slope.y + " VELOCITY X: " + velocity.x + " | VELOCITY Y: " + velocity.y);
						
						currentPlayer.getPositionInRoom().getRoomPosition().x += velocity.x;
						currentPlayer.getPositionInRoom().getRoomPosition().y += velocity.y;
					}
					
					
					//add player to update list
					playersToUpdate.add(currentPlayer);
					extension.trace("ADDING PLAYER TO UPDATE LIST");
					
					if (currentPlayer.getPositionInRoom().getRoomPosition().distanceTo(targetPosition) < 0.1f)
					{
						currentPlayer.setTargetPosition(null, false);
						extension.trace("FINISHED MOVING");
					}
				}
			}
			
			//if playersToUpdate has stuff in it, add to bundle and send it
			
			if (!playersToUpdate.isEmpty())
			{
				createAndSendBundle(currentRoom);
				playersToUpdate.clear();
			}
		}
		
		//++cycles;
	}
	
	private void createAndSendBundle(SloverseRoom currentRoom)
	{
		ISFSObject playerPositionData = new SFSObject();
		
		playerPositionData.putInt("playerAmount", playersToUpdate.size());
		
		ISFSArray playerArray = new SFSArray();
		
		for (Player player : playersToUpdate)
		{
			ISFSObject playerWrapper = new SFSObject();
			player.toSFSObject(playerWrapper);
			playerArray.addSFSObject(playerWrapper);
		}
		
		playerPositionData.putSFSArray("playerDataArray", playerArray);
		extension.send(UPDATE_PLAYERS_POSITION_BUNDLE, playerPositionData, currentRoom.getRoom().getPlayersList());
	}
}
