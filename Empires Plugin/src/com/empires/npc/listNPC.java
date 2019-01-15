package com.empires.npc;

import org.apache.commons.lang.StringUtils;
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
			String listOfNPCS = "";
			if(args[0].contentEquals("all")) {
				System.out.println("Listing all NPC's");
				listOfNPCS = "Current NPC's:";
			    for (PlayerNPC npc : main.npcContainer.getAllNPCS()) {
			    	listOfNPCS += npc.getPrintInfo();
			    }
			} else if((args.length == 1)) {
				System.out.println("args.length = 1");
				PlayerNPC npc;
				if(StringUtils.isNumeric(args[0]))
				{
					int id = Integer.parseInt(args[0]);
					npc = main.npcContainer.getNPC(id);
				} else {
					String name = args[0];
					System.out.println("Name is: " + name);
					npc = main.npcContainer.getNPC(name);
				}
				if(npc != null) {
				  listOfNPCS += npc.getPrintInfo();
				}
			} else if(args.length > 1) {
				String npcName = "";
				for( String str : args)
					npcName+= str + " ";
				npcName = npcName.substring(0, npcName.length() - 1);
				System.out.println("The name is: " + ChatColor.DARK_GREEN + npcName);
				PlayerNPC npc = main.npcContainer.getNPC(npcName);
				if(npc != null) {
					  listOfNPCS += npc.getPrintInfo();
				}
			}
			if(sender instanceof Player) {
				Player p = ((Player) sender);
				p.sendMessage(listOfNPCS);
			} else {
				System.out.println(listOfNPCS);
			}
			return true;
		}
    	return false;
	}
}
