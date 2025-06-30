package org.darwin.fabricweaponsapi.client.items.components;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WeaponComponents {
    public static final ComponentType<Integer> TOOLTIP_COMPONENT = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of("fabricweaponsapi", "has_tooltip"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<String> TOOLTIP = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of("fabricweaponsapi", "tooltip"),
            ComponentType.<String>builder().codec(Codec.STRING).build()
    );

    public static void initialize() {

    }
}
