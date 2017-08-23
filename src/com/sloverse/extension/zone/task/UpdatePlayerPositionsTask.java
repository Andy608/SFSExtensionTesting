package com.sloverse.extension.zone.task;

import java.util.ArrayList;
import java.util.List;

import com.sloverse.extension.zone.core.SloverseTaskManager;
import com.sloverse.extension.zone.core.SloverseZoneExtension;
import com.sloverse.extension.zone.simulation.player.Player;
import com.sloverse.extension.zone.simulation.room.SloverseRoom;
import com.sloverse.extension.zone.simulation.world.World;
import com.sloverse.extension.zone.util.math.Vec2;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class UpdatePlayerPositionsTask implements Runnable {

	//private int cycles = 0;
	
	private static final String UPDATE_PLAYERS_POSITION_BUNDLE = "updatePlayersPositionBundle";
	
	private static List<Player> playersToUpdate = new ArrayList<Player>();
	private static Vec2 targetDirection = new Vec2();
	
	private static final float SPEED = 5.0f;
	private static Vec2 moveVelocity = new Vec2();
	
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
					Vec2 currentPosition = currentPlayer.getPositionInRoom().getRoomPosition();
					Vec2 targetPosition = currentPlayer.getTargetPosition();
					
					if (!currentPlayer.lerpToTarget())
					{
						currentPlayer.getPositionInRoom().setRoomPosition(targetPosition);
					}
					else
					{
						targetDirection = Vec2.subtract(targetPosition, currentPosition);
						
						if (targetDirection.getMagnitude() != 0)
						{
							targetDirection.normalize();
							
							//move player towards target by TARGETSPEED * deltatime
							moveVelocity = Vec2.scale(targetDirection, SPEED * SloverseTaskManager.DELTA);
							
							//extension.trace("TARGET DIRECTION: " + targetDirection.x + ", " + targetDirection.y + " | LERP: " + currentPlayer.lerpToTarget());
							
							currentPlayer.getPositionInRoom().getRoomPosition().add(moveVelocity);
						}
						else
						{
							extension.trace("Player is already at the target position!");
							currentPlayer.setTargetPosition(null);
							continue;
						}
					}
					
					
					//add player to update list
					playersToUpdate.add(currentPlayer);
					//extension.trace("ADDING PLAYER TO UPDATE LIST");
					
					if (Vec2.distanceBetween(currentPlayer.getPositionInRoom().getRoomPosition(), targetPosition) < 1.0f)
					{
						currentPlayer.getPositionInRoom().setRoomPosition(targetPosition);
						currentPlayer.setTargetPosition(null);
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
