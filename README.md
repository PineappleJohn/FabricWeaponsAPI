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
I personally do not reccomend using overrides
### Override translation key
```java
WeaponBuilder weapon = new WeaponBuilder.Builder()
  .register(item)
  .overrideTranslationKey("FabricWeaponsAPI Sword")
  .build();
```
### Override settings
