# Documentation
All methods are documented with Javadocs. <br>
### Creating a weapon
Here is the simple way to create a weapon--however, this doesn't register it to anything.
```java
WeaponBuilder weapon = new WeaponBuilder.Builder().build();
```
#### Registering your weapon
Registering your item is quite simple (or a lot more simple than creating your own class for it)
```java
RegistrableItem item = new RegistrableItem
                        .Builder(
                                Identifier.of("fabricweaponsapi", "sword"),
                                WeaponTypes.SWORD, // WeaponTypes currently include SWORD, AXE, and MACE
                                ToolMaterial.NETHERITE // Contains all tool materials
                        )
                        /* Optional methods include:
                        * .damage(float)
                        * .attackSpeed(float)
                        * .durability(int)
                        */
                        .build();
WeaponBuilder weapon = new WeaponBuilder.Builder()
  .register(item) // Registers your weapon as the item.
  .build();
```
## More methods
### Fireproof
```java
new RegistrableItem
                        .Builder(
                                Identifier.of("fabricweaponsapi", "sword"),
                                WeaponTypes.SWORD, // WeaponTypes currently include SWORD, AXE, and MACE
                                ToolMaterial.NETHERITE // Contains all tool materials
                        )
                        .fireproof() // Very simple and easy
                        .build();
```
### Tooltips
```java
WeaponBuilder weapon = new WeaponBuilder.Builder()
  .register(item)
  .addTooltip("A custom tooltip") // Check out Minecraft's built-in text colour coding system
  .build();
```
## Overrides
I personally do not recommend using overrides, (especially for translation keys) but they are options.
### Override translation key
```java
WeaponBuilder weapon = new WeaponBuilder.Builder()
  .register(item)
  .overrideTranslationKey("FabricWeaponsAPI Sword")
  .build();
```
### Override settings
```java
WeaponBuilder weapon = new WeaponBuilder.Builder()
  .register(item)
  .overrideSettings(new Item.Settings()) // This also adds the registry key
  .build();
```
## Advanced Usage
### Callbacks
There are a few callbacks implemented in the API. These include usage and attack callbacks.
```java
WeaponBuilder weapon = new WeaponBuilder.Builder()
        .register(item)
        .build();
    weapon.getSwordItem()
            .AddUsageCallback((player, world, hand) -> {
                // Do something when the weapon is used
                /*
                        ActionResult.FAIL - Cancels the usage
                        ActionResult.SUCCESS - Continues
                        ActionResult.PASS - Passes the usage to the next handler
                 */
                return ActionResult.SUCCESS; // Return the result of the usage
            });
    weapon.getSwordItem()
            .AddAttackCallback((stack, player, target) -> {
                // Do something when the weapon is used to attack
                /*
                        ActionResult.FAIL - Cancels the attack
                        ActionResult.SUCCESS - Continues
                        ActionResult.PASS - Passes the attack to the next handler
                 */
                return ActionResult.SUCCESS; // Return the result of the attack
            });
```
### Registering item groups
Item groups support two methods of registration, registering using a custom ItemGroup class (but that needs to registered to
the item group registry) or using a registry key from ItemGroups.(group).
```java

WeaponBuilder weapon = new WeaponBuilder.Builder()
  .register(item)
  .addToGroup(ItemGroups.COMBAT)
  .build();

```