package com.empires.core;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.empires.npc.NPCMain;
//import com.empires.packets.PacketMain;


public class Main extends JavaPlugin implements Listener{
	public NPCMain npc = NPCMain.getInstance();
//	public PacketMain packet = new PacketMain();
	
	@Override
	public void onEnable() {
		//Configuration
		this.getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		//Events
		Bukkit.getPluginManager().registerEvents(new JoinListener(), this);
//		Bukkit.getPluginManager().registerEvents(packet, this);
		
		//Commands
		npc.setNPCCommandExecutors(this);
	}
	
	@Override
	public void onDisable() {
		System.out.println("Plugin Disabled!");
	}
}