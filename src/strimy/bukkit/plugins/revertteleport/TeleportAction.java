package strimy.bukkit.plugins.revertteleport;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportAction 
{

	private HashMap<Player, Location> playersToOriginalLocations = new HashMap<Player, Location>();
	
	public void addPlayer(Player player)
	{
		playersToLocations.put(player, player.getLocation());
	}
	
	public void clearPlayerList()
	{
		playersToLocations = new HashMap<Player, Location>();
	}
	
	public HashMap<Player, Location> getPlayersLocations()
	{
		return playersToLocations;
	}

}
