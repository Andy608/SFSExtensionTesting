package com.sloverse.extension.zone.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.sloverse.extension.zone.task.UpdatePlayerPositionsTask;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.util.TaskScheduler;

public class SloverseTaskManager 
{
	public enum EnumTaskID 
	{
		UPDATE_PLAYER_POSITION
	};
	
	private TaskScheduler taskScheduler;
	
	private boolean areTasksInitialized;
	
	private List<ScheduledFuture<?>> sloverseZoneScheduledTasks;
	
	private ScheduledFuture<?> updatePlayerPositionsTask;
	
	private Runnable updatePlayerPositionsRunnable;
	
	public SloverseTaskManager()
	{
		taskScheduler = SmartFoxServer.getInstance().getTaskScheduler();
		areTasksInitialized = false;
		
		sloverseZoneScheduledTasks = new ArrayList<ScheduledFuture<?>>();
	}
	
	public void initTasks()
	{
		if (areTasksInitialized)
		{
			return;
		}
		else
		{
			updatePlayerPositionsRunnable = new UpdatePlayerPositionsTask();
			
			//Fire every tenth of a second
			addTaskToSchedule(updatePlayerPositionsRunnable, 0, 100, TimeUnit.MILLISECONDS);
		}
	}
	
	private void addTaskToSchedule(Runnable task, int initialDelay, int period, TimeUnit timeUnit)
	{
		sloverseZoneScheduledTasks.add(updatePlayerPositionsTask = taskScheduler.scheduleAtFixedRate(task, initialDelay, period, timeUnit));
	}
	
	public void stopAllTasks()
	{
		SloverseZoneExtension.zoneExtension.trace("STOPPING ALL TASKS.");
		
		for (ScheduledFuture<?> task : sloverseZoneScheduledTasks)
		{
			//As of right now it interrupts even if in the middle of a task.
			task.cancel(true);
		}
	}
	
	public void stopTask(EnumTaskID taskID)
	{
		switch(taskID)
		{
		case UPDATE_PLAYER_POSITION:
			updatePlayerPositionsTask.cancel(true);
			break;
		default:
			break;
		}
	}
}
