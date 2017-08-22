package com.sloverse.extension.zone.simulation.room.bounds;

import java.util.ArrayList;

import com.sloverse.extension.zone.simulation.room.RoomCoordinate;

public class RoomBoundsList 
{
	public static RoomBounds townCenterBounds = new RoomBounds(new ArrayList<RoomCoordinate>() 
	{	
		{
			add(new RoomCoordinate(-1, -1));
			add(new RoomCoordinate(-1, 1));
			add(new RoomCoordinate(1, 1));
			add(new RoomCoordinate(1, -1));
		}
	});
}
