package com.empires.npc;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandLook implements CommandExecutor {
	private NPCMain npcMain;

	public CommandLook(NPCMain npcMain) {
		this.npcMain = npcMain;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (StringUtils.isNumeric(args[0]) && StringUtils.isNumeric(args[1]) && StringUtils.isNumeric(args[2]) && StringUtils.isNumeric(args[2])) {
			int id = Integer.parseInt(args[0]);
			float pitchHead = Float.parseFloat(args[1]);
			float yawHead = Float.parseFloat(args[2]);
			float yawBody = Float.parseFloat(args[3]);
			try {
				npcMain.getContainer().getNPC(id).orientation(pitchHead, yawHead, yawBody);
			} catch (Exception ex) {
				System.out.println("Failed to rotate head on command!");
				ex.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
