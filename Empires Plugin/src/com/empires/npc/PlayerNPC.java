package com.empires.npc;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import net.minecraft.server.v1_13_R2.Packet;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntity;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_13_R2.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_13_R2.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_13_R2.PlayerInteractManager;
import net.minecraft.server.v1_13_R2.WorldServer;

public class PlayerNPC extends PlayerReflection implements Listener {
	public static final int PlayerNameMaxLength = 16;
	private EntityPlayer entity;
	private Player owner;

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
			System.err.println("PlayerNPC Constructor failed to create PlayerEntity.");
			ex.printStackTrace();
		}
		this.sendSpawnPacketsToOnlinePlayers();
	}

	public PlayerNPC(Location location, String playerNPCName, Player owner) {
		this(location, playerNPCName);
		this.setOwner(owner);
		owner.sendMessage("Contructed PlayerNPC: " + playerNPCName);
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
			information += "\n" + ChatColor.GRAY + "  Owner: " + ChatColor.DARK_GREEN + this.getOwner().getName();
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
		sendSpawnPacketsToOnlinePlayers();
	}

	public void sendPacketToOnlinePlayers(Packet<?> packet) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPacket(p, packet);
		}
	}

	public void Destroy() {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.getID());
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPacket(p, packet);
		}
	}

	public void sendSpawnPacketsToOnlinePlayers() {
		if (this.entity != null) {
			for (Player p : Bukkit.getOnlinePlayers())
				sendSpawnPacketsToPlayer(p);
		}
	}

	public void sendSpawnPacketsToPlayer(Player p) {
		sendPacket(p,
				new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.entity));
		sendPacket(p, new PacketPlayOutNamedEntitySpawn(this.entity));
		sendPacket(p,
				new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entity));
	}

	public void rotatePlayerHead(float headPitchDegrees, float headYawDegrees, float bodyYawDegrees) {

		byte headYaw = (byte) (headYawDegrees * 256F / 360F);
		byte bodyYaw = (byte) (bodyYawDegrees * 256F / 360F);
		boolean onGround = true;

		PacketPlayOutEntity.PacketPlayOutEntityLook packet1 = new PacketPlayOutEntityLook(this.getID(),
				(byte) headPitchDegrees, bodyYaw, onGround);
		sendPacketToOnlinePlayers(packet1);

		PacketPlayOutEntityHeadRotation packet2 = new PacketPlayOutEntityHeadRotation(this.entity, headYaw);
		sendPacketToOnlinePlayers(packet2);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		sendSpawnPacketsToPlayer(e.getPlayer());
	}

	/*
	 * @EventHandler public void onInteract( e) {
	 * sendSpawnPacketsToPlayer(e.getPlayer()); }
	 */
	public boolean equals(PlayerNPC rhs) {
		return this.entity.equals(rhs.entity);
	}

	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == entity ? 0 : entity.hashCode());
		return hash;
	}
}