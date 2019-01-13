package com.empires.npc;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.empires.core.Main;

public class HandleNPC implements CommandExecutor{
	private Set<PlayerNPC> npcSet = new HashSet<PlayerNPC>();
	private Main main;
	public HandleNPC(Main main) {
		this.main = main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if((sender instanceof Player) && (args.length > 0)) {
			//Cat name from args
			String npcName = "";
			for( String str : args)
				npcName+= str + " ";
			npcName = npcName.substring(0, npcName.length() - 1);
			//construct the new npc
			PlayerNPC npc = new PlayerNPC(((Player) sender), npcName);
			this.npcSet.add(npc);
			Bukkit.getPluginManager().registerEvents(npc, this.main);
			return true;
		}
    	return false;
	}

}
