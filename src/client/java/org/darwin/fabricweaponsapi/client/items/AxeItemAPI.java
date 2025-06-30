package org.darwin.fabricweaponsapi.client.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.apache.commons.lang3.function.TriFunction;
import org.darwin.fabricweaponsapi.client.items.components.WeaponComponents;

import java.util.ArrayList;
import java.util.List;

public class AxeItemAPI extends AxeItem {
    private List<TriFunction<World, PlayerEntity, Hand, ActionResult>> UsageCallbacks = new ArrayList<>();
    private List<TriFunction<ItemStack, LivingEntity, LivingEntity, Boolean>> AttackCallbacks = new ArrayList<>();

    public void AddUsageCallback(TriFunction<World, PlayerEntity, Hand, ActionResult> function) {
        UsageCallbacks.add(function);
    }

    public void AddAttackCallback(TriFunction<ItemStack, LivingEntity, LivingEntity, Boolean> function) {
        AttackCallbacks.add(function);
    }

    public AxeItemAPI(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
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
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        for (TriFunction<World, PlayerEntity, Hand, ActionResult> callback : UsageCallbacks) {
            return callback.apply(world, user, hand);
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        for (TriFunction<ItemStack, LivingEntity, LivingEntity, Boolean> callback : AttackCallbacks) {
            return callback.apply(stack, attacker, target);
        }
        return true;
    }
}
