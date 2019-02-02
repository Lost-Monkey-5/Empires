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
import net.minecraft.server.v1_13_R2.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityVelocity;
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

	public float getHeadRotation() {
		return this.entity.getHeadRotation();
	}

	public float getHeadRotationInDegrees() {
		return this.entity.getHeadRotation() * 360F / 256F;
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

	public Vector getVelocity() {
		return new Vector(entity.motX, entity.motY, entity.motZ);
	}

	public void setHeadRotation(float headYaw) {
		this.entity.setHeadRotation(headYaw);
	}

	public void setHeadRotationFromDegrees(float headYawDegrees) {
		this.entity.setHeadRotation(headYawDegrees);
	}

	public void setLocation(Location l) {
		this.entity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public void setVelocity(Vector vel) {
		// Units of packet are 1/8000 of a block per server tick (50ms)
		double converstionConstant = (1 / 8000) * 50E-3;
		entity.motX = vel.getX() * converstionConstant;
		entity.motY = vel.getY() * converstionConstant;
		entity.motZ = vel.getZ() * converstionConstant;

		PacketPlayOutEntityVelocity packet = new PacketPlayOutEntityVelocity(this.entity);
		sendPacketToOnlinePlayers(packet);
	}

	public void Destroy() {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.getID());
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendPacket(p, packet);
		}
	}

	public void sendPacketToOnlinePlayers(Packet<?> packet) {
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
		// sendPacket(p,
		// new
		// PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
		// this.entity));
	}

	public void face(float bodyYawDegrees) {
		this.orientation(this.getLocation().getPitch(), this.getHeadRotationInDegrees(), bodyYawDegrees);
	}

	public void look(float headPitchDegrees, float headYawDegrees) {
		this.orientation(headPitchDegrees, headYawDegrees, this.getLocation().getYaw());
	}

	public void move(float deltaX, float deltaY, float deltaZ) {
		// Calculate movement factor
		float movmentFactor = 32 * 128;
		// Calculate the change in each direction with moment factor
		long dx = (long) (deltaX * movmentFactor);
		long dy = (long) (deltaY * movmentFactor);
		long dz = (long) (deltaZ * movmentFactor);
		// Boolean for on ground argument of packet
		boolean onGround = true;
		// Construct the packet
		PacketPlayOutEntity.PacketPlayOutRelEntityMove packet = new PacketPlayOutRelEntityMove(this.getID(), dx, dy, dz,
				onGround);
		sendPacketToOnlinePlayers(packet);
		Location oldLocation = this.getLocation();
		// Save movement to the EntityPlayer
		this.setLocation(new Location(oldLocation.getWorld(), oldLocation.getX() + deltaX, oldLocation.getY() + deltaY,
				oldLocation.getZ() + deltaZ, oldLocation.getYaw(), oldLocation.getPitch()));
		this.setHeadRotation(this.getHeadRotation());
	}

	public void orientation(float headPitchDegrees, float headYawDegrees, float bodyYawDegrees) {
		// Packet arguments are not in degrees, but in 256 ticks per revolution for Yaw
		byte headYaw = (byte) (headYawDegrees * 256F / 360F);
		byte bodyYaw = (byte) (bodyYawDegrees);
		boolean onGround = true;

		// Construct the first packet, for head pitch and body yaw angles.
		PacketPlayOutEntity.PacketPlayOutEntityLook packet1 = new PacketPlayOutEntityLook(this.getID(),
				(byte) headPitchDegrees, bodyYaw, onGround);
		// Send out the first packet to players
		sendPacketToOnlinePlayers(packet1);
		// Construct the second packet, for head yaw angle.
		PacketPlayOutEntityHeadRotation packet2 = new PacketPlayOutEntityHeadRotation(this.entity, headYaw);
		// Send out the second packet to players
		sendPacketToOnlinePlayers(packet2);
		// Save movement to the EntityPlayer
		Location oldLocation = this.getLocation();
		this.setLocation(new Location(oldLocation.getWorld(), oldLocation.getX(), oldLocation.getY(),
				oldLocation.getZ(), bodyYawDegrees, headPitchDegrees));
		this.setHeadRotationFromDegrees(headYawDegrees);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		// Send packets to new player to show the NPC
		sendSpawnPacketsToPlayer(e.getPlayer());
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