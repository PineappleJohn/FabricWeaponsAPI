package org.darwin.fabricweaponsapi.client.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.darwin.fabricweaponsapi.client.items.AxeItemAPI;
import org.darwin.fabricweaponsapi.client.items.MaceItemAPI;
import org.darwin.fabricweaponsapi.client.items.SwordItemAPI;
import org.darwin.fabricweaponsapi.client.items.components.WeaponComponents;

import java.util.Objects;

/**
 * The weapon builder <br>
 * Correct usage:
 * <pre>
 * {@code new WeaponBuilder
 *                 .Builder()
 *                 .register(new RegistrableItem
 *                         .Builder(
 *                                 Identifier.of("fabricweaponsapi", "sword"),
 *                                 WeaponTypes.SWORD,
 *                                 ToolMaterial.NETHERITE
 *                         )
 *                         .damage(12f)
 *                         .attackSpeed(0.6f)
 *                         .durability(5)
 *                         .build()
 *                 )
 *                 .build();}</pre>
 */
public class WeaponBuilder {
    private static final Logger log = LogManager.getLogger(WeaponBuilder.class);
    private final WeaponTypes TYPE;
    private final Item item;

    public WeaponBuilder(Builder builder) {
        this.TYPE = builder.type;
        this.item = builder.registeredItem;
    }

    /**
     * @return The weapons type
     */
    public WeaponTypes GetWeaponType() {
        return TYPE;
    }

    /**
     * @return The registered item
     */
    public Item GetRegisteredItem() { return item; }

    /**
     * @return The item as a SwordItem if the weapon is tagged as a Sword. If the item is not tagged as a Sword it returns null.
     */
    public SwordItemAPI getSwordItem() {
        if (item instanceof SwordItemAPI)
            return (SwordItemAPI) item;
        else return null;
    }

    /**
     * @return The item as a MaceItem if the weapon is tagged as a Mace. If the item is not tagged as a Mace it returns null.
     */
    public MaceItemAPI getMaceItem() {
        if (item instanceof MaceItemAPI maceItem)
            return maceItem;
        else return null;
    }

    /**
     * @return The item as a AxeItem if the weapon is tagged as a Axe. If the item is not tagged as a Axe it returns null.
     */
    public AxeItemAPI getAxeItem() {
        if (item instanceof AxeItemAPI axeItem)
            return axeItem;
        else return null;
    }

    /**
     * A builder; allowing the api to create weapons with optional variables easier.
     */
    public static class Builder {
        private WeaponTypes type;
        private boolean registerthis = false;
        private static boolean addTooltip = false;
        private static Text tooltip = Text.empty();
        private RegistrableItem item;
        private Item registeredItem;
        private boolean addToGroup = false;
        private ItemGroup group;

        /**
         * Sets the weapon type, doesn't work when the item is registered.
         * @param type The item type
         * @return The builder
         */
        public Builder type(WeaponTypes type) {
            this.type = type;
            return this;
        }

        /**
         * Registers the item
         * @param item A registrable item
         * @return The builder
         */
        public Builder register(RegistrableItem item) {
            registerthis = true;
            this.item = item;
            return this;
        }

        /**
         * Adds the item to a group. <br>
         * Proper usage:
         * <pre>
         * {@code
         *     (WeaponBuilder).addToGroup(ItemGroups.COMBAT)
         * }
         * </pre>
         * @param groupRegistryKey The item group's registry key
         * @return The builder
         */
        public Builder addToGroup(RegistryKey<ItemGroup> groupRegistryKey) {
            return addToGroup(Registries.ITEM_GROUP.get(groupRegistryKey));
        }

        /**
         * Adds the item to a group. <br>
         * Proper usage:
         * <pre>
         * {@code
         *     (WeaponBuilder).addToGroup(ItemGroups.COMBAT)
         * }
         * </pre>
         * @param group The item group
         * @return The builder
         */
        public Builder addToGroup(ItemGroup group) {
            addToGroup = true;
            this.group = group;
            return this;
        }

        /**
         * Adds a tooltip to the item
         * @param value The tooltip description
         * @return The builder
         */
        public Builder addTooltip(String value) {
            addTooltip = true;
            tooltip = Text.of(value);
            return this;
        }
        private static String translationKey = "";

        /**
         * <h2> <b> Overriding the translation key is generally bad practice! </b> </h2> <br>
         * Overrides the translation key for the item.
         * @param key The override translation key
         * @return The builder
         */
        public Builder overrideTranslationKey(String key) {
            log.warn("""
                    
                    Dear mod developer,\s
                    It's commonly bad practice to override the translation key\s
                    We suggest adding a translation\
                     json instead--Refer to the fabric docs for more info.""");
            translationKey = key;
            return this;
        }

        /**
         * Overrides the default item settings.
         * @param settings Settings
         * @return The builder
         */
        public Builder overrideSettings(Item.Settings settings) {
            item.builder().overrideSettings(settings);
            return this;
        }


        /**
         * Builds and registers the item if you used the Register() tag.
         * @return The weapon builder
         */
        public WeaponBuilder build() {
            if (registerthis) registeredItem = registerItem(item);
            if (addToGroup) {
                ItemGroupManager.RegisterItem(registeredItem, group);
            }
            translationKey = "";
            return new WeaponBuilder(this);
        }

        private static Item registerItem(RegistrableItem item) {
            return switch (item.types()) {
                case SWORD -> registerSword(item);
                case AXE -> registerAxe(item);
                case MACE -> registerMace(item);
                case NONE -> throw new NullPointerException("Weapon type cannot be NONE. Please specify a valid weapon type.");
            };
        }

        private static Item registerMace(RegistrableItem item) {
            RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, item.id());
            Item.Settings settings = new Item.Settings().registryKey(key);
            if (item.fireproof()) settings.fireproof();
            settings.maxCount(1);
            settings.maxDamage(item.durability());
            settings.component(WeaponComponents.TOOLTIP_COMPONENT, addTooltip ? 1 : 0);
            settings.component(WeaponComponents.TOOLTIP, tooltip.toString());
            if (!Objects.equals(translationKey, "")) settings.translationKey(translationKey);

            if (item.shouldOverrideSettings()) {
                settings = item.overrideSettings().registryKey(key);
            }

            MaceItemAPI item1 = new MaceItemAPI(
                    settings
            );
            return Registry.register(Registries.ITEM, key, item1);
        }

        private static Item registerAxe(RegistrableItem item) {
            RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, item.id());
            Item.Settings settings = new Item.Settings().registryKey(key);
            if (item.fireproof()) settings.fireproof();
            settings.maxCount(1);
            settings.maxDamage(item.durability());
            settings.component(WeaponComponents.TOOLTIP_COMPONENT, addTooltip ? 1 : 0);
            settings.component(WeaponComponents.TOOLTIP, tooltip.toString());
            if (!Objects.equals(translationKey, "")) settings.translationKey(translationKey);

            if (item.shouldOverrideSettings()) {
                settings = item.overrideSettings().registryKey(key);
            }

            AxeItemAPI item1 = new AxeItemAPI(
                    item.toolMaterial(),
                    item.attackSpeed(),
                    item.damage(),
                    settings
            );
            return Registry.register(Registries.ITEM, key, item1);
        }

        private static Item registerSword(RegistrableItem item) {
            RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, item.id());
            Item.Settings settings = new Item.Settings().registryKey(key);
            if (item.fireproof()) settings.fireproof();
            settings.maxCount(1);
            settings.maxDamage(item.durability());
            settings.component(WeaponComponents.TOOLTIP_COMPONENT, addTooltip ? 1 : 0);
            settings.component(WeaponComponents.TOOLTIP, tooltip.toString());
            if (!Objects.equals(translationKey, "")) settings.translationKey(translationKey);


            if (item.shouldOverrideSettings()) {
                settings = item.overrideSettings().registryKey(key);
            }

            SwordItemAPI item1 = new SwordItemAPI(
                    item.toolMaterial(),
                    item.attackSpeed(),
                    item.damage(),
                    settings
            );
            return Registry.register(Registries.ITEM, key, item1);
        }

    }
}
