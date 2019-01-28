package com.empires.npc;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandMoveTo implements CommandExecutor {
	private NPCMain npcMain;

	public CommandMoveTo(NPCMain npcMain) {
		this.npcMain = npcMain;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		int id = Integer.parseInt(args[0]);
		float deltaX = Float.parseFloat(args[1]);
		float deltaY = Float.parseFloat(args[2]);
		float deltaZ = Float.parseFloat(args[3]);
		try {
			npcMain.getContainer().getNPC(id).move(deltaX, deltaY, deltaZ);
			return true;
		} catch (Exception ex) {
			System.out.println("Failed to move PlayerNPC on command!");
			ex.printStackTrace();
		}
		return false;
	}

}
