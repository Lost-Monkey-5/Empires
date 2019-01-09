package com.empires.core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NumberCommand implements CommandExecutor {
    //number <number> <test> <test2>
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		
		if(args[0].equalsIgnoreCase("1")) {
			player.sendMessage("You are number fucking 1!");
		}else {
			player.sendMessage(ChatColor.GRAY + "Your stupid number is " 
		      + ChatColor.RED + args[0] + ChatColor.DARK_GRAY + ".");
		}
		return false;
	}

}
