package org.darwin.fabricweaponsapi.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.darwin.fabricweaponsapi.client.items.components.WeaponComponents;
import org.darwin.fabricweaponsapi.client.registry.ItemGroupManager;
import org.darwin.fabricweaponsapi.client.registry.RegistrableItem;
import org.darwin.fabricweaponsapi.client.registry.WeaponBuilder;
import org.darwin.fabricweaponsapi.client.registry.WeaponTypes;

public class FabricweaponsapiClient implements ClientModInitializer {

    private static final Logger log = LogManager.getLogger(FabricweaponsapiClient.class);

    @Override
    public void onInitializeClient() {
        WeaponComponents.initialize();
        new ItemGroupManager();
        new WeaponBuilder
                .Builder()
                .addTooltip("Sword :D")
                .overrideTranslationKey("API sword")
                .register(new RegistrableItem
                        .Builder(
                                Identifier.of("fabricweaponsapi", "sword"),
                                WeaponTypes.SWORD,
                                ToolMaterial.NETHERITE
                        )
                        .damage(12f)
                        .attackSpeed(0.6f)
                        .durability(5)
                        .build()
                )
                .build();

        new WeaponBuilder
                .Builder()
                .addToGroup(ItemGroups.TOOLS)
                .overrideTranslationKey("Axe test")
                .register(new RegistrableItem
                        .Builder(
                                Identifier.of("fabricweaponsapi", "axe"),
                                WeaponTypes.AXE,
                                ToolMaterial.DIAMOND
                ).build())
                .build();
        ;

        WeaponBuilder maceItem = new WeaponBuilder
                .Builder()
                .addTooltip("The mace test")
                .addToGroup(ItemGroups.COMBAT)
                .register(new RegistrableItem.Builder(
                        Identifier.of("fabricweaponsapi", "better_mace"),
                        WeaponTypes.MACE,
                        ToolMaterial.GOLD )
                        .damage(100f)
                        .attackSpeed(1.2f)
                        .durability(1)
                        .build())
                .build();

        maceItem.getMaceItem().AddAttackCallback((stack, player, target, slammed) -> {
            System.out.println("Hit an animal " + (slammed ? "and slammed" : ""));
            return true;
        });
        maceItem.getMaceItem().AddUsageCallback(((one, two, three) -> {
            System.out.println("Used the mace");
            return ActionResult.PASS;
        }));

        ItemGroupManager.RegisterGroups();
    }
}
