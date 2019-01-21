package com.empires.npc;

import org.bukkit.event.Listener;

import com.empires.core.Main;

public class NPCMain implements Listener {
	private static NPCMain instance;
	private ContainerNPC container = new ContainerNPC();

	private NPCMain() {
	}

	// static method to create instance of Singleton class
	public static NPCMain getInstance() {
		if (instance == null)
			instance = new NPCMain();

		return instance;
	}

	public void setNPCCommandExecutors(Main main) {
		// Events

		// Commands
		main.getCommand("spawn").setExecutor(new CommandSpawn(this, main));
		main.getCommand("list").setExecutor(new CommandList(this));
		main.getCommand("velocity").setExecutor(new CommandVelocity(this));
		
	}

	public ContainerNPC getContainer() {
		return container;
	}

	public void setContainer(ContainerNPC container) {
		this.container = container;
	}
}
