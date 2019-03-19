package com.empires.npc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandList implements CommandExecutor {
	private NPCMain npcMain;

	public CommandList(NPCMain npcMain) {
		this.npcMain = npcMain;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String listOfNPCS = "Current NPC's:\n";
		for (PlayerNPC npc : npcMain.getContainer().getAllNPCS()) {
			listOfNPCS += "NPC:\n";
			listOfNPCS += npc.toString();
		}
		if (sender instanceof Player) {
			Player p = ((Player) sender);
			p.sendMessage(listOfNPCS);
		} else {
			System.out.println(listOfNPCS);
		}
		return true;
	}
}
