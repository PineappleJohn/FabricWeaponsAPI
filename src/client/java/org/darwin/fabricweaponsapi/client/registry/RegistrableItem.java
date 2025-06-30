package org.darwin.fabricweaponsapi.client.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Identifier;
import org.darwin.fabricweaponsapi.client.items.components.WeaponComponents;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class RegistrableItem {
    private final Identifier id;
    private final WeaponTypes types;
    private final float damage;
    private final float attackSpeed;
    private final boolean fireproof;
    private final int durability;
    private final ToolMaterial toolMaterial;
    @NotNull
    private final Builder builder;
    private boolean shouldOverrideSettings = false;
    private Item.Settings settings;

    public Builder builder() {
        return builder;
    }
    public Identifier id() {return id;}
    public WeaponTypes types() {return types;}
    public float damage() {return damage;}
    public float attackSpeed() {return attackSpeed;}
    public boolean fireproof() {return fireproof;}
    public int durability() {return durability;}
    public ToolMaterial toolMaterial() {return toolMaterial;}
    public Item.Settings overrideSettings() {
        return settings;
    }
    public boolean shouldOverrideSettings() {
        return shouldOverrideSettings;
    }

    private RegistrableItem(Builder builder) {
        this.builder = builder;
        this.id = builder.id;
        this.types = builder.types;
        this.damage = builder.damage;
        this.attackSpeed = builder.attackSpeed;
        this.fireproof = builder.fireproof;
        this.durability = builder.durability;
        this.toolMaterial = builder.toolMaterial;
        this.settings = builder.settings;
        this.shouldOverrideSettings = builder.shouldOverrideSettings;
    }

    public static class Builder {
        private final Identifier id;
        private final WeaponTypes types;
        private ToolMaterial toolMaterial;
        private float damage = 7f;
        private float attackSpeed = 1.6f;
        private boolean fireproof = false;
        private int durability = 1000;
        private boolean shouldOverrideSettings = false;
        private Item.Settings settings = new Item.Settings();

        public Builder(Identifier id, WeaponTypes types, ToolMaterial toolMaterial) {
            this.id = id;
            this.types = types;
            this.toolMaterial = toolMaterial;
        }

        public Builder damage(float damage) {
            this.damage = damage;
            return this;
        }

        public Builder attackSpeed(float attackSpeed) {
            this.attackSpeed = attackSpeed;
            return this;
        }

        public Builder fireproof() {
            this.fireproof = true;
            return this;
        }

        public Builder durability(int durability) {
            this.durability = durability;
            return this;
        }

        public Builder overrideSettings(Item.Settings settings) {
            this.shouldOverrideSettings = true;
            this.settings = settings;
            return this;
        }

        public Builder addTooltipComponent(String value) {
            settings.component(WeaponComponents.TOOLTIP_COMPONENT, 1);
            settings.component(WeaponComponents.TOOLTIP, value);
            return this;
        }

        public RegistrableItem build() {
            return new RegistrableItem(this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RegistrableItem) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.types, that.types) &&
                Float.floatToIntBits(this.damage) == Float.floatToIntBits(that.damage) &&
                Float.floatToIntBits(this.attackSpeed) == Float.floatToIntBits(that.attackSpeed) &&
                this.fireproof == that.fireproof &&
                this.durability == that.durability;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, types, damage, attackSpeed, fireproof, durability);
    }

    @Override
    public String toString() {
        return "RegisterableItem[" +
                "id=" + id + ", " +
                "types=" + types + ", " +
                "damage=" + damage + ", " +
                "attackSpeed=" + attackSpeed + ", " +
                "fireproof=" + fireproof + ", " +
                "durability=" + durability + ']';
    }

}