package com.empires.npc;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.empires.core.Main;

public class listNPC implements CommandExecutor {
	private Main main;
	public listNPC(Main main){
		this.main = main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if((args.length > 0) && (args[0] != null)) {
			System.out.println("Listing all NPC's");
			if((args[0].contentEquals("all")) && (sender instanceof Player)) {
				Player p = ((Player) sender);
				String listOfNPCS = "Current NPC's:";
			    for (PlayerNPC npc : main.npcContainer.getAllNPCS()) {
			    	listOfNPCS += "\nNPC:\n" + ChatColor.GRAY + "  Name: " 
			    			+ ChatColor.DARK_GREEN + npc.getName()
			    			+ "\n" + ChatColor.GRAY + "  ID: "
			    			+ ChatColor.DARK_GREEN + npc.getID();
			    }
				p.sendMessage(listOfNPCS);
			}
			return true;
		}
    	return false;
	}
}
