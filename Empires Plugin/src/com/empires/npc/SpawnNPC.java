package com.empires.npc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.empires.core.Main;

public class SpawnNPC implements CommandExecutor{
	private Main main;
	public SpawnNPC(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if((args.length > 0) && (args[0] != null) && (sender instanceof Player)) {
			//Cat name from args
			String npcName = "";
			for( String str : args)
				npcName+= str + " ";
			npcName = npcName.substring(0, npcName.length() - 1);
			//Construct the new npc
			PlayerNPC npc = new PlayerNPC(((Player) sender), npcName);
			//Save NPC 
			this.main.npcContainer.addNPC(npc);
			//Register Events with the NPC
			Bukkit.getPluginManager().registerEvents(npc, this.main);
			return true;
		}
    	return false;
	}
}
