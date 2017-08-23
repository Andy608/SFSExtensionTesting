package com.sloverse.extension.zone.simulation.room.bounds;

import java.util.ArrayList;

import com.sloverse.extension.zone.util.math.Vec2;

public class RoomBoundsList 
{
	public static RoomBounds townCenterBounds = new RoomBounds(new ArrayList<Vec2>() 
	{	
		{
			add(new Vec2(0, 0));
			add(new Vec2(0, 1));
			add(new Vec2(1, 1));
			add(new Vec2(1, 0));
		}
	});
}
