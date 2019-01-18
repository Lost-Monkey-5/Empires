package com.empires.npc;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

public class CommandVelocity implements CommandExecutor {
	private NPCMain npcMain;

	public CommandVelocity(NPCMain npcMain) {
		this.npcMain = npcMain;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		PlayerNPC npc;
		boolean Arg0isNumber = StringUtils.isNumeric(args[0]);
		// Check if Arg0 is an id number
		if (Arg0isNumber) {
			int id = Integer.parseInt(args[0]);
			System.out.println("ID: " + id);
			npc = npcMain.getContainer().getNPC(id);
			if(npc != null) {
				//Get the index of the last element in args
				int end = args.length - 1;
				
				// Get boolean for the last three args
				boolean Arg1isNumber = StringUtils.isNumeric(args[end-2]);
				boolean Arg2isNumber = StringUtils.isNumeric(args[end-1]);
				boolean Arg3isNumber = StringUtils.isNumeric(args[end]);
				
				// Check that the remaining three arguments are numeric for vector
				if (Arg1isNumber && Arg2isNumber && Arg3isNumber) {
					//Create a vector
					Vector vel = new Vector();
					vel.setX(Double.parseDouble(args[end-2]));
					vel.setY(Double.parseDouble(args[end-1]));
					vel.setZ(Double.parseDouble(args[end]));
                    //set the velocity of the player
					npc.setVelocity(vel);
					return true;
				}
			}
		}
		return false;
	}
}