package strimy.bukkit.plugins.revertteleport;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class TeleportAction 
{

	private HashMap<Player, TeleportLocations> playersToLocations = new HashMap<Player, TeleportLocations>();
	
	public void addPlayer(Player player, TeleportLocations locations)
	{
		playersToLocations.put(player, locations);
	}
	
	public void clearPlayerList()
	{
		playersToLocations = new HashMap<Player, TeleportLocations>();
	}
	
	public HashMap<Player, TeleportLocations> getPlayersLocations()
	{
		return playersToLocations;
	}
}

