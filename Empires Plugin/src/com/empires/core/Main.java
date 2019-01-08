package com.empires.core;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	@Override
	public void onEnable() {
		System.out.println("Pugin Enabled!");
	}
	@Override
	public void onDisable() {
		System.out.println("Plugin Disabled!");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if(cmd.getName().equals("hello")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage(ChatColor.GRAY + "Hello, " + ChatColor.GREEN + player.getName() + ChatColor.GRAY + ". Your Health has been restored!");
				player.setHealth(20.0);
			}else {
				System.out.println("You cannot use this unless you are a player!");
			}
		}
		return false;
	}
}