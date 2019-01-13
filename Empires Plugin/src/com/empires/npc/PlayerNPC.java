package com.empires.npc;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import net.minecraft.server.v1_13_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_13_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_13_R2.PlayerConnection;
import net.minecraft.server.v1_13_R2.PlayerInteractManager;
import net.minecraft.server.v1_13_R2.WorldServer;

public class PlayerNPC implements Listener{
	public static final int PlayerNameMaxLength = 16; 
	private EntityPlayer entity;
	
	public PlayerNPC(Player p, String playerNPCName) {
		System.out.println("Contructing PlayerNPC");
		//Check the maximum length of the name
		if(playerNPCName.length() > PlayerNameMaxLength) {
			playerNPCName = playerNPCName.substring(0, PlayerNameMaxLength);
		} 
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
	public int getID() {
		return entity.getId();
	}
	public String getName() {
		return entity.getName();
	}
	/*public EntityPlayer getEntity() {
		return this.entity;
	}*/
	public void setVelocity(Vector vel) {
		entity.motX = vel.getX();
		entity.motY = vel.getY();
		entity.motZ = vel.getZ();
	}
	public void sendPacketsToOnlinePlayers() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			sendPacketsToOnlinePlayer(p);
		}
	}
	public void sendPacketsToOnlinePlayer(Player player) {
		//Get Player Connection
		PlayerConnection connection = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
		//Send Packets
		connection.sendPacket(new PacketPlayOutPlayerInfo(
				PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.entity));
		connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.entity));
		connection.sendPacket(new PacketPlayOutPlayerInfo(
				PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entity));
	}
	@EventHandler
	public void sendPackets(PlayerJoinEvent e) {
		if (this.entity != null) {
			//if(this.entity.valid) {
				System.out.println("Sending PlayerNPC Packets to new player.");
				sendPacketsToOnlinePlayer(e.getPlayer());
			/*} else {
				System.err.println("Entity is not valid on PlayerJoinEvent.");
			}*/
		} else {
			System.out.print("Class entity is null.");
		}
	}
	public boolean equals(PlayerNPC rhs) {
		return this.entity.equals(rhs.entity);
	}
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == entity ? 0 : entity.hashCode());
		return hash;
	}
}