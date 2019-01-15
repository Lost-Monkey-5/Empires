package com.empires.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class ContainerNPC {
	private Map<Integer, PlayerNPC> npcMap = new HashMap<Integer, PlayerNPC>();
	ListMultimap<String, Integer> nameMap = ArrayListMultimap.create();

	public void addNPC(PlayerNPC npc) {
		this.nameMap.put(npc.getName(), npc.getID());
		this.npcMap.put(npc.getID(), npc);
	}

	public PlayerNPC getNPC(Integer id) {
		return npcMap.get(id);
	}

	public PlayerNPC getNPC(String name) {
		if (this.nameMap.containsKey(name)) {
			List<Integer> ids = this.nameMap.get(name);
			if (ids.size() == 1 && npcMap.containsKey(ids.get(0))) {
				return getNPC(ids.get(1));
			}
		}
	    return null;
	}

	public List<PlayerNPC> getAllNPCS() {
		return new ArrayList<PlayerNPC>(this.npcMap.values());
	}
}
