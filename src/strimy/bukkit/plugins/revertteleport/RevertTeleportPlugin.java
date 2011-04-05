package strimy.bukkit.plugins.revertteleport;
import java.util.logging.Logger;

import org.bukkit.plugin.java.*;
import org.bukkit.event.*;
import org.bukkit.event.Event.Priority;


public class RevertTeleportPlugin extends JavaPlugin 
{
	Logger log;
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnable() 
	{
		log = getServer().getLogger();
		
		RTPlayerListener playerListener = new RTPlayerListener(this);
		//getServer().getPluginManager().registerEvent(Event.Type.PLAYER_TELEPORT, playerListener, Priority.Normal, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Normal, this);
		
		log.info("["+getDescription().getName()+"] Plugin loaded");
		getCommand("rtp").setExecutor(new RevertTeleportExecutor(this));
		
	}

}
