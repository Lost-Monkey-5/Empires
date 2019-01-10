package com.empires.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class SpawnListener implements Listener {
	
	@EventHandler
	public void onSpawn(PlayerJoinEvent e) {
		
		//Create player
		Player p = (Player) e.getPlayer();
		//Get players Location
		Location playerLocation = p.getLocation();
		//spawn Entity of type Zombie on the player
		Entity ent = Bukkit.getWorld("world").spawnEntity(playerLocation, EntityType.ZOMBIE);
		//Make the Entity glow so the player will have no trouble seeing the Entity
		ent.setGlowing(true);
		
		//Cast Entity to Zombie
		Zombie maxTheZombie = (Zombie) ent;
		//Name Zombie Max
		maxTheZombie.setCustomName("Max the Zombie");
		maxTheZombie.setCustomNameVisible(true);
		//Give Max a stick to beat the player with
		maxTheZombie.getEquipment().setItemInMainHand(new ItemStack(Material.STICK));
		//Tell Max to beat player with Stick
		maxTheZombie.setTarget(p);
		
		//Tell Console about Max and his actions
		System.out.println("Spawned " + ent.getCustomName() + " to greet " 
				+ p.getDisplayName() + " with a stick!");
		//Tell The Player about Max and his intent
		p.sendMessage(ChatColor.GRAY + "Spawned " + ChatColor.DARK_GREEN 
				+ ent.getCustomName() + ChatColor.GRAY + " to greet " + ChatColor.BLUE
				+ p.getDisplayName() + ChatColor.GRAY + " with a stick!");
	}
}
