package com.empires.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	@Override
	public void onEnable() {
		System.out.println("Pugin Enabled!");
		
		this.getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	@Override
	public void onDisable() {
		System.out.println("Plugin Disabled!");
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		
		if(!p.hasPermission("core.allowmove")) {
			p.sendMessage("You are Petrified!");
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onThrow(PlayerEggThrowEvent e) {
		Player p = e.getPlayer();
		p.sendMessage(ChatColor.DARK_RED + "STOP THROWING MY BLOODY EGGS!");
	}
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		
		Player p = (Player) sender;
		
		if(cmd.getName().equals("config")) {
			String word = this.getConfig().getString("Word");
			int number = this.getConfig().getInt("Number");
			
			p.sendMessage(ChatColor.DARK_GRAY + "The word is " + ChatColor.GREEN + word 
					+ ChatColor.DARK_GRAY + ". The number is " + ChatColor.DARK_GREEN 
					+ number + ChatColor.GRAY + ".");
		}		
		return false;
	}
}