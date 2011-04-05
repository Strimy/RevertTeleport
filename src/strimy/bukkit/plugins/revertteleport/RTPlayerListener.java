package strimy.bukkit.plugins.revertteleport;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerListener;

public class RTPlayerListener extends PlayerListener 
{
	RevertTeleportPlugin plugin;
	HashMap<CommandSender, TeleportAction> playerTpActions = new HashMap<CommandSender, TeleportAction>();
	
	public RTPlayerListener(RevertTeleportPlugin plugin) 
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) 
	{
		String command = event.getMessage();
		Player sender = event.getPlayer();
		
		if(command.startsWith("/tp "))
		{
			Player[] who = null;
			Player toWho = null;;
			String[] args = command.replaceFirst("/tp ", "").split(" ");
			if(args.length == 1)
			{
				if(args[0].equals("revert"))
				{
					if(playerTpActions.containsKey(event.getPlayer()))
					{
						TeleportAction action =  playerTpActions.get(sender);
						HashMap<Player, Location> playerSet = action.getPlayersLocations();
						action.clearPlayerList();
						
						who = playerSet.keySet().toArray(who);
						
						for (Player player : playerSet.keySet()) 
						{
							action.addPlayer(player);
							Location to = playerSet.get(player);
							player.teleport(to);
						}
						
						return;
					}
					else
					{
						sender.sendMessage(ChatColor.GOLD+"There isn't a registered teleport from you");
						event.setCancelled(true);
						return;
					}
				}
				else
				{
					who = new Player[1];
					who[0] = event.getPlayer();
					toWho = findPlayerByName(args[0]);
				}
			}
			if(args.length == 2)
			{
				if(args[0].equals("*"))
				{
					who = plugin.getServer().getOnlinePlayers();
					toWho = findPlayerByName(args[1]);
				}
			}
			
			if(who != null && toWho != null)
			{
				TeleportAction action = new TeleportAction();
				action.setTo(toWho.getLocation());
				for (Player player : who) 
				{
					action.addPlayer(player);
				}
				if(playerTpActions.containsKey(event.getPlayer()))
					playerTpActions.remove(event.getPlayer());
				
				playerTpActions.put(event.getPlayer(), action);
			}
		}
		
		super.onPlayerCommandPreprocess(event);
	}
	
	private Player findPlayerByName(String name)
	{
		Player[] players = plugin.getServer().getOnlinePlayers();
		for (Player player : players) 
		{
			if(player.getDisplayName().equals(name))
			{
				return player;
			}
		}
		return null;
	}

}
