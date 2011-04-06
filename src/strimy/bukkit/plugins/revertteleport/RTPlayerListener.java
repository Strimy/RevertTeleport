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
					if(playerTpActions.containsKey(sender))
					{
						TeleportAction action =  playerTpActions.get(sender);
						HashMap<Player, TeleportLocations> playerSet = action.getPlayersLocations();
						Set<Player> players = playerSet.keySet();
						
						action.clearPlayerList();
						who = new Player[players.size()];
						who = players.toArray(who);
						
						for (Player player : players) 
						{
							TeleportLocations currentLocations = playerSet.get(player);
							TeleportLocations newLocations = new TeleportLocations();
							newLocations.setFrom(currentLocations.getTo());
							newLocations.setTo(currentLocations.getFrom());
							
							action.addPlayer(player, newLocations);
							Player onlinePlayer = findPlayerByName(player.getDisplayName());
							
							if(onlinePlayer != null)
							{
								onlinePlayer.teleport(newLocations.getTo());
								sender.sendMessage(ChatColor.LIGHT_PURPLE+"Player " + player.getDisplayName()+ " teleported");
							}
							
						}
						event.setCancelled(true);
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
				else
				{
					who = new Player[1];
					who[0] = findPlayerByName(args[0]);
					toWho = findPlayerByName(args[1]);
				}
			}
			
			if(who != null && toWho != null)
			{
				TeleportAction action = new TeleportAction();
				Location to = toWho.getLocation();
				for (Player player : who) 
				{
					TeleportLocations newLocations = new TeleportLocations();
					newLocations.setFrom(player.getLocation());
					newLocations.setTo(to);
					action.addPlayer(player, newLocations);
				}
				if(playerTpActions.containsKey(sender))
					playerTpActions.remove(sender);
				
				playerTpActions.put(event.getPlayer(), action);
				sender.sendMessage("Registered teleport");
			}
			else
			{
				sender.sendMessage("Cannot find an user");
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
