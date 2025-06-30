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
### More methods
yeah im going in a pool im on vacation rn sorry
