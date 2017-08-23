package com.sloverse.extension.zone.simulation.room.bounds;

import java.util.ArrayList;
import java.util.List;

import com.sloverse.extension.zone.simulation.player.Player;
import com.sloverse.extension.zone.util.math.Vec2;

public class RoomBounds 
{
	private static float MAX_ROOM_BOUNDS = 1.0f;
	private static float MIN_ROOM_BOUNDS = -1.0f;
	
	private List<Vec2> roomBoundCoordinates;
	//private List<RoomBoundLine> roomBoundLines;
	
	public RoomBounds(ArrayList<Vec2> roomBounds)
	{
		roomBoundCoordinates = roomBounds;
	}
	
	public boolean isPlayerInRoomBounds(Player player) 
	{
		Vec2 playerPosition = player.getPositionInRoom().getRoomPosition();
		
		if (!isPlayerInAABoundingBox(playerPosition)) return false;
		
		//For now we are just testing the basic bounding box.
		//In the future, test if inside polygon.
		
		return true;
	}
	
	private boolean isPlayerInAABoundingBox(Vec2 playerPosition)
	{
		float minX = MAX_ROOM_BOUNDS, minY = MAX_ROOM_BOUNDS;
		float maxX = MIN_ROOM_BOUNDS, maxY = MIN_ROOM_BOUNDS;
		
		for (int i = 0; i < roomBoundCoordinates.size(); ++i)
		{
			float xCoord = roomBoundCoordinates.get(i).x;
			float yCoord = roomBoundCoordinates.get(i).y;
			
			if (xCoord < minX) minX = xCoord;
			if (yCoord < minY) minY = yCoord;
			if (xCoord > maxX) maxX = xCoord;
			if (yCoord > maxY) maxY = yCoord;
		}
		
		if (playerPosition.x < minX || playerPosition.x > maxX ||
			playerPosition.y < minY || playerPosition.y > maxY)
		{
			return false;
		}
		
		return true;
	}

	public Vec2 clampTargetToRoomBounds(Vec2 requestedTargetPosition) 
	{
		//IN THE FUTURE. 
		//LOOK AT 3D MATH TEXT BOOK PG 96 AND FIND CLOSEST INTERSECTION BETWEEN PLAYER POINT, ORIGINAL TARGET POINT AND BOUNDS.
		
		return requestedTargetPosition;
	}
}
