package org.darwin.fabricweaponsapi.client.items;

import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.function.TriFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.darwin.fabricweaponsapi.client.items.components.WeaponComponents;

import java.util.ArrayList;
import java.util.List;

public class SwordItemAPI extends SwordItem {
    private static final Logger log = LogManager.getLogger(SwordItemAPI.class);
    private Vec3d sweepSize = new Vec3d(1f, 0.25f, 1f);
    private List<BlockStateVariantMap.QuadFunction<SwordItemAPI, World, PlayerEntity, Hand, ActionResult>> UsageCallbacks = new ArrayList<>();
    private List<TriFunction<ItemStack, LivingEntity, LivingEntity, Boolean>> HitCallbacks = new ArrayList<>();
    public SwordItemAPI(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (stack.getOrDefault(WeaponComponents.TOOLTIP_COMPONENT, 0) == 1) {
            String tooltipText = stack.getOrDefault(WeaponComponents.TOOLTIP, "");
            tooltip.add(Text.translatable(tooltipText));
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);

        for (TriFunction<ItemStack, LivingEntity, LivingEntity, Boolean> callback : HitCallbacks) {
            try {
                return callback.apply(stack, attacker, target);
            } catch (Exception e) {
                log.error("error occured while calling functions: ", e);
            }
        }

        return true;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        for (BlockStateVariantMap.QuadFunction<SwordItemAPI, World, PlayerEntity, Hand, ActionResult> callback : UsageCallbacks) {
            try {
                var returned = callback.apply((SwordItemAPI) (Object) this, world, user, hand);
                if (returned != null && returned != ActionResult.PASS) {
                    return returned;
                }
            } catch (Exception e) {
                log.error("error occured while calling functions: ", e);
                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
    }

    /**
     * Sets the size of the sweep attack.<br>
     * Default is (1f, 0.25f, 1f)<br>
     * <b>Disclaimer: The sweep attack only works if entities are less than 9 squared meters away.</b>
     * @param size
     */
    public void setDefaultSweepSize(Vec3d size) {
        sweepSize = size;
    }

    /**
     * @return The current sweep size.
     */
    public Vec3d getSweepSize() {return sweepSize;}

    /**
     * Runs the default sword sweep code.
     * @param player
     * @param damageSource
     * @param damage
     */
    public void sweep(PlayerEntity player, DamageSource damageSource, float damage) {
        player.getWorld().playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
        player.spawnSweepAttackParticles();

        for (LivingEntity livingEntity3 : player.getWorld().getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().expand((double)sweepSize.x, (double)sweepSize.y, (double)sweepSize.z))) {
            if (livingEntity3 != player && !player.isTeammate(livingEntity3) && (!(livingEntity3 instanceof ArmorStandEntity) || !((ArmorStandEntity)livingEntity3).isMarker()) && player.squaredDistanceTo(livingEntity3) < (double)9.0F) {
                livingEntity3.takeKnockback((double)0.4F, (double) MathHelper.sin(player.getYaw() * ((float)Math.PI / 180F)), (double)(-MathHelper.cos(player.getYaw() * ((float)Math.PI / 180F))));
                livingEntity3.serverDamage(damageSource, damage);
                World var22 = player.getWorld();
                if (var22 instanceof ServerWorld serverWorld) {
                    EnchantmentHelper.onTargetDamaged(serverWorld, livingEntity3, damageSource);
                }
            }
        }
    }

    /**
     * Damages the sword
     * @param player
     * @param amount
     */
    public void damage(PlayerEntity player, int amount) {
        player.getInventory().getMainHandStack().damage(amount, player, EquipmentSlot.MAINHAND);
    }

    /**
     * @param player
     * @return The max durability of the sword
     */
    public int maxDamage(PlayerEntity player) {
        return player.getInventory().getMainHandStack().getMaxDamage();
    }

    /**
     * Breaks the sword
     * @param player
     */
    public void breakItem(PlayerEntity player) {
        damage(player, maxDamage(player));
    }

    /**
     * Runs code whenever the item is right-clicked
     * @param function A lambda callback

     *                 Correct usage
     *                   <pre>
     *                    {@code
     *                               .durability(5)
     *                               .build()
     *                       )
     *                       .build().getSwordItem().AddUsageEvent((item, world, player, hand) -> {
     *                           // Your code here
     *                           return ActionResult.PASS / ActionResult.FAIL depending on if the usage should go through
     *                       });
     *                    }
     *                   </pre>
     */
    public void AddUsageEvent(BlockStateVariantMap.QuadFunction<SwordItemAPI, World, PlayerEntity, Hand, ActionResult> function) {
        UsageCallbacks.add(function);
    }

    /**
     * Runs code when an entity is hit
     * @param func A lambda callback
     * Correct usage
     *             <pre>
     *              {@code
     *                         .durability(5)
     *                         .build()
     *                 )
     *                 .build().getSwordItem().AddAttackingEvent((stack, target, attacking) -> {
     *                     // Your code here
     *                     return true / false depending on if the attack should go through
     *                 });
     *              }
     *             </pre>
     */
    public void AddAttackingEvent(TriFunction<ItemStack, LivingEntity, LivingEntity, Boolean> func) {
        HitCallbacks.add(func);
    }
}
