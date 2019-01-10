package com.empires.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle.EnumTitleAction;

public class SpawnListener implements Listener {
	
	@EventHandler
	public void onSpawn(PlayerJoinEvent event) {
		
		displayJoinTitle(event);
		spawnMaxTheZombie(event);
	}
	private void displayJoinTitle(PlayerJoinEvent e) {
		//PacketPlayOutTitle(arg0, arg1, arg2, arg3, arg4)
		//arg0 EnumTitleAction
		//arg1 ChatSerializer
		//arg2 Number of ticks for fade-in phase of title
		//arg3 Number of ticks for stay phase of title
		//arg4 Number of ticks for fade-out phase of title
		
		//Create Title Packet
		PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, 
				ChatSerializer.a("{\"text\":\"" + " §o§4Lost's World" + "\"}"), 
				20, 100, 10);
		//Create Subtitle Packet
		PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, 
				ChatSerializer.a("{\"text\":\"" + " §8A midieval adventure awaits §0" 
				+ e.getPlayer().getName() + "§8!" + "\"}"), 
				20, 100, 10);
		//Send Title Packet
		((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(titlePacket);
		//Send Sub-Title Packet
		((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(subTitlePacket);
	}
	private void spawnMaxTheZombie(PlayerJoinEvent e) {
		//Get the Player for joined member
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
