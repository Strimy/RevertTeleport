package strimy.bukkit.plugins.revertteleport;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RevertTeleportExecutor implements CommandExecutor 
{
	RevertTeleportPlugin plugin;
	HashMap<CommandSender, TeleportAction> playerTpActions = new HashMap<CommandSender, TeleportAction>();
	
	public RevertTeleportExecutor(RevertTeleportPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandStr, String[] args) 
	{
		if(args.length == 0)
		{
			return false;
		}
		else if(args.length == 1)
		{
			if(args[0].equals("revert"))
			{
				if(playerTpActions.containsKey(sender))
				{
					TeleportAction lastSenderAction = playerTpActions.get(sender);
					sender.sendMessage(ChatColor.LIGHT_PURPLE+"Reverting last teleport...");
					
					// Teleport the player to his previous location
					//teleport(sender, lastSenderAction.getPlayer(), lastSenderAction.getFrom());
				}
			}
		}
		else if(args.length == 2)
		{	
			Player who = findPlayerByName(args[0]);
			if(who == null)
			{
				sender.sendMessage(ChatColor.GOLD+"Unable to find the user "+args[0]);
				return true;
			}
			Player toWho = findPlayerByName(args[1]);
			if(toWho == null)
			{
				sender.sendMessage(ChatColor.GOLD+"Unable to find the user "+args[1]);
				return true;
			}
			
			sender.sendMessage(ChatColor.GOLD+"Teleporting "+who.getDisplayName() + " to "+toWho.getDisplayName());
			teleport(sender, who, toWho.getLocation());		
		}
		return true;
	}
	
	private void teleport(CommandSender sender, Player who, Location to)
	{
		Location from = who.getLocation();
		who.teleport(to);
		if(playerTpActions.containsKey(sender))
		{
			playerTpActions.remove(sender);
		}
		TeleportAction action = new TeleportAction();
		//action.setFrom(from);
		//action.setPlayer(who);
		//action.setTo(to);
		playerTpActions.put(sender, action);
	}
	
	private Player findPlayerByName(String name)
	{
		Player[] players = plugin.getServer().getOnlinePlayers();
		for (Player player : players) 
		{
			if(player.getDisplayName().equals("name"))
			{
				return player;
			}
		}
		return null;
	}

}