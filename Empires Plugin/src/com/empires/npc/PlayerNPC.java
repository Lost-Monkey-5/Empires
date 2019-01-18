package com.empires.npc;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

public class PlayerNPC implements Listener {
	public static final int PlayerNameMaxLength = 16;
	private EntityPlayer entity;
	private Player owner = null;

	public PlayerNPC(Location l, String playerNPCName) {
		// Check the maximum length of the name
		if (playerNPCName.length() > PlayerNameMaxLength) {
			playerNPCName = playerNPCName.substring(0, PlayerNameMaxLength);
		}
		try {
			// Get the NMS Server Class
			MinecraftServer serverNMS = ((CraftServer) Bukkit.getServer()).getServer();
			// Get the NMS World Class
			WorldServer worldNMS = ((CraftWorld) l.getWorld()).getHandle();
			// Create a GameProfile
			GameProfile gameProfile = new GameProfile(UUID.randomUUID(), playerNPCName);
			// Create Player Interact Manager
			PlayerInteractManager playerIM = new PlayerInteractManager(worldNMS);
			// set the new Entity
			this.entity = new EntityPlayer(serverNMS, worldNMS, gameProfile, playerIM);
			// Set the new Entity location
			this.entity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		sendPacketsToOnlinePlayers();
	}

	public PlayerNPC(Player p, String playerNPCName) {
		this(p.getLocation(), playerNPCName, null);
	}

	public PlayerNPC(Location location, String playerNPCName, Player p) {
		this(location, playerNPCName);
		this.setOwner(p);
		p.sendMessage("Contructed PlayerNPC: " + playerNPCName);
	}

	public int getID() {
		return entity.getId();
	}

	private Location getLocation() {
		return entity.getBukkitEntity().getLocation();
	}

	public String getName() {
		return entity.getName();
	}

	public Player getOwner() {
		return owner;
	}

	public String getPrintInfo() {
		String information = ChatColor.WHITE + "\nNPC:\n";
		try {
			information += ChatColor.GRAY + "  Name: " + ChatColor.DARK_GREEN + this.getName();
			information += "\n" + ChatColor.GRAY + "  ID: " + ChatColor.DARK_GREEN + this.getID();
			information += "\n" + ChatColor.GRAY + "  Location: " + ChatColor.DARK_GREEN + this.getLocation();
			information += "\n" + ChatColor.GRAY + "  Owner: " + ChatColor.DARK_GREEN + this.getOwner();
		} catch (Exception ex) {
			ex.printStackTrace();
			information = ex.toString();
		}
		return information;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void setVelocity(Vector vel) {
		entity.motX = vel.getX();
		entity.motY = vel.getY();
		entity.motZ = vel.getZ();
		sendPacketsToOnlinePlayers();
	}

	public void sendPacketsToOnlinePlayers() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPacketsToOnlinePlayer(p);
		}
	}

	public void sendPacketsToOnlinePlayer(Player player) {
		try {
		// Get Player Connection
		PlayerConnection connection = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
		// Send Packets
		connection.sendPacket(
				new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.entity));
		connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.entity));
		connection.sendPacket(
				new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entity));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@EventHandler
	public void sendPackets(PlayerJoinEvent e) {
		if (this.entity != null) {
			sendPacketsToOnlinePlayer(e.getPlayer());			
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