package com.empires.npc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerReflection {

	public Class<?> getNMSClass(String className) {
		try {
			return Class.forName("net.minecraft.server." + getVersion() + "." + className);
		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return null;
	}

	public Class<?> getCBClass(String className) {
		try {
			return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + className);
		} catch (Exception ex) {
			System.err.println("Faild to get class  " + className + " from org.bukkit.craftbukkit." 
					+ getVersion() + ".");
			ex.printStackTrace();
		}
		return null;
	}

	public String getVersion() {
		return Bukkit.getServer().getClass().getName().split("\\.")[3];
	}

	public Method getMethod(Object obj, String methodName) {
		try {
			return obj.getClass().getMethod(methodName);
		} catch (Exception ex) {
			System.err.println("Faild to get method " + methodName + " in class " + obj.getClass().getName());
			ex.printStackTrace();
		}
		return null;
	}

	public Method getMethod(Object obj, String methodName, Class<?> parameterType) {
		try {
			return obj.getClass().getMethod(methodName, parameterType);
		} catch (Exception ex) {
			System.err.println("Faild to get method " + methodName + " in class " + obj.getClass().getName());
			ex.printStackTrace();
		}
		return null;
	}

	public Field getField(Object obj, String fieldName) {
		try {
			return obj.getClass().getField(fieldName);
		} catch (Exception ex) {
			System.err.println("Faild to get field " + fieldName + " in class " + obj.getClass().getName());
			ex.printStackTrace();
		}
		return null;
	}

	public Object getValue(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception ex) {
			System.err.println("Faild to get value " + name + " in class " + obj.getClass().getName());
			ex.printStackTrace();
		}
		return null;
	}

	public void setValue(Object obj, String name, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception ex) {
			System.err.println("Faild to set value " + name + " in class " + obj.getClass().getName());
			ex.printStackTrace();
		}
	}
	
	public void setField(Object obj, String name, Object value) {
		try {
			Field field = obj.getClass().getField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception ex) {
			System.err.println("Faild to set field " + name + " in class " + obj.getClass().getName());
			ex.printStackTrace();
		}
	}

	public void sendPacket(Player player, Object packet) {
		try {
			Object playerHandle = this.getMethod(player, "getHandle").invoke(player);
			Object playerConnectionHandle = this.getField(playerHandle, "playerConnection").get(playerHandle);

			this.getMethod(playerConnectionHandle, "sendPacket", getNMSClass("Packet")).invoke(playerConnectionHandle,
					packet);
		} catch (Exception ex) {
			System.err.println("Faild to send packet " + packet.getClass().getName() + " to " + player.getName());
			ex.printStackTrace();
		}
	}
}
