package org.darwin.fabricweaponsapi.client.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;

import java.util.HashMap;
import java.util.Map;

public class ItemGroupManager {
    public static ItemGroupManager instance;
    boolean registered = false;

    public ItemGroupManager() {
        instance = this;
    }

    private static final Map<Item, ItemGroup> groups = new HashMap<>();

    /**
     * Please do not register items through this,
     * it is meant for the WeaponBuilder.
     * @param item The item to register
     * @param group The item group
     */
    public static void RegisterItem(Item item, ItemGroup group) {
        groups.put(item, group);
    }

    public static void RegisterGroups() {
        if (instance.registered) throw new RuntimeException("Attempted to register groups twice!");
        else {
            instance.registered = true;
            for (Map.Entry<Item, ItemGroup> entry : groups.entrySet()) {
                Item item = entry.getKey();
                ItemGroup group = entry.getValue();
                RegistryKey<ItemGroup> groupKey = Registries.ITEM_GROUP.getKey(group)
                        .orElseThrow(() -> new IllegalArgumentException("ItemGroup not found: " + group));
                ItemGroupEvents.modifyEntriesEvent(groupKey).register(entries -> entries.add(item));
            }
        }
    }
}