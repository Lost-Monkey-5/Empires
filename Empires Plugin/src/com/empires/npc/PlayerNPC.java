package com.empires.npc;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import net.minecraft.server.v1_13_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_13_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_13_R2.PlayerConnection;
import net.minecraft.server.v1_13_R2.PlayerInteractManager;
import net.minecraft.server.v1_13_R2.WorldServer;

public class PlayerNPC implements Listener, CommandExecutor{
	private EntityPlayer entity;
	
	public void createPlayerNPC(Player p, String playerNPCName) {
		System.out.println("Contructing PlayerNPC");
		//Get the NMS Server Class
		MinecraftServer serverNMS = ((CraftServer) Bukkit.getServer()).getServer();
		//Get the NMS World Class
		WorldServer worldNMS = ((CraftWorld) p.getWorld()).getHandle();
		//Create a GameProfile
		GameProfile gameProfile = new GameProfile(p.getUniqueId(), playerNPCName);
		//Create Player Interact Manager
		PlayerInteractManager playerIM = new PlayerInteractManager(worldNMS);
		//set the new Entity
		this.entity = new EntityPlayer(serverNMS, worldNMS, gameProfile, playerIM);
		//Set the new Entity location
		this.entity.setLocation(
				p.getLocation().getX(), 
				p.getLocation().getY(),
				p.getLocation().getZ(), 
				p.getLocation().getYaw(),
				p.getLocation().getPitch());
		sendPacketsToOnlinePlayers();
	}
	public void sendPacketsToOnlinePlayers() {
		for(Player all : Bukkit.getOnlinePlayers()) {
			//Get Player Connection
			PlayerConnection connection = ((CraftPlayer) all.getPlayer()).getHandle().playerConnection;
			//Send Packets
			connection.sendPacket(new PacketPlayOutPlayerInfo(
					PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.entity));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.entity));
			connection.sendPacket(new PacketPlayOutPlayerInfo(
					PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entity));
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if((sender instanceof Player) && (args.length > 0)) {
			createPlayerNPC(((Player) sender), args[0]);
			return true;
		}
    	return false;
	}
	@EventHandler
	public void sendPackets(PlayerJoinEvent e) {
		if(this.entity.valid) {
			System.out.println("Sending PlayerNPC Packets to new player.");
			//Get Player Connection
			PlayerConnection connection = ((CraftPlayer) e.getPlayer()).getHandle().playerConnection;
			//Send Packets
			connection.sendPacket(new PacketPlayOutPlayerInfo(
					PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.entity));
			connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.entity));
			connection.sendPacket(new PacketPlayOutPlayerInfo(
					PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entity));
		}
	}
}