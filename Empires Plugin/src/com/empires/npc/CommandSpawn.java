package com.empires.npc;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.empires.core.Main;

public class CommandSpawn implements CommandExecutor {
	private NPCMain npcMain;
	private Main main;

	public CommandSpawn(NPCMain npcMain, Main main) {
		this.npcMain = npcMain;
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args != null) {
			String npcName = "";
			if (sender instanceof ConsoleCommandSender) {
				// Get the name from the last set of arguments past in
				npcName = catName(Arrays.copyOfRange(args, 6, args.length - 1));
				// Get the location from the first set of arguments past in
				Location location = createLocation(args[0], Arrays.copyOfRange(args, 1, 5));
				// Check for invalid data
				if (location == null) {
					System.out.println("Location Data Invalad!");
					return false;
				} else if (npcName == null) {
					System.out.println("Name Data Invalad!");
					return false;
				} else {
					PlayerNPC npc = new PlayerNPC(location, npcName);
					saveNPC(npc);
				}
			} else if (sender instanceof Player) {
				// Get the player who issued the command
				Player p = ((Player) sender);
				if (args[0].equals("onPlayer")) {
					if (args.length == 2)
						npcName = args[1];
					else
						npcName = catName(Arrays.copyOfRange(args, 1, args.length - 1));
					System.out.println("Spawning " + npcName + " in " + p.getName());
					PlayerNPC npc = new PlayerNPC(p, npcName);
					System.out.println("Past NPC Constructor!");
					saveNPC(npc);
				} else {
					// Get the name from the last set of arguments past in
					npcName = catName(Arrays.copyOfRange(args, 6, args.length - 1));
					// Get the location from the first set of arguments past in
					Location location = createLocation(args[0], Arrays.copyOfRange(args, 1, 5));
					// Check for invalid data
					if (location == null) {
						p.sendMessage("Location Data Invalad!");
						return false;
					} else if (npcName == null) {
						p.sendMessage("Name Data Invalad!");
						return false;
					} else {
						PlayerNPC npc = new PlayerNPC(location, npcName, p);
						saveNPC(npc);
					}
				}
			} else {
				// Get the name from the last set of arguments past in
				npcName = catName(Arrays.copyOfRange(args, 6, args.length - 1));
				// Get the location from the first set of arguments past in
				Location location = createLocation(args[0], Arrays.copyOfRange(args, 1, 5));
				// Check for invalid data
				if ((location == null) || (npcName == null)) {
					return false;
				} else {
					PlayerNPC npc = new PlayerNPC(location, npcName);
					saveNPC(npc);
				}
			}
		}
		return false;
	}

	private String catName(String[] name) {
		String npcName = "";
		for (String str : name)
			npcName += str + " ";
		npcName = npcName.substring(0, npcName.length() - 1);
		return npcName;
	}

	private Location createLocation(String worldName, String[] corrdinates) {
		World world = (Bukkit.getServer()).getWorld(worldName);
		if (world == null)
			return null;
		for (String str : corrdinates) {
			if (!StringUtils.isNumeric(str))
				return null;
		}

		return new Location(world, Double.parseDouble(corrdinates[1]), Double.parseDouble(corrdinates[2]),
				Double.parseDouble(corrdinates[3]), Float.parseFloat(corrdinates[4]), Float.parseFloat(corrdinates[5]));
	}
	private void saveNPC(PlayerNPC npc) {
		if (npc != null) {
			System.out.println("Saving Character to container.");
			// Save NPC
			this.npcMain.getContainer().addNPC(npc);
			// Register Events with the NPC
			Bukkit.getPluginManager().registerEvents(npc, this.main);
		}
	}
}
