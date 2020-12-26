package com.Spoilers.arcaneimbuement.rituals;

import com.Spoilers.arcaneimbuement.augments.Augmentation;
import com.ma.api.rituals.IRitualContext;
import com.ma.api.rituals.RitualEffect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualEffectTesting extends RitualEffect {
	
	public RitualEffectTesting(ResourceLocation ritualName) {
		super(ritualName);
	
	}

	@Override
	protected boolean applyRitualEffect(IRitualContext context) {
		World world = context.getWorld();
		BlockPos center = context.getCenter();
		ItemStack targetItem = ItemStack.EMPTY;
		for (ItemStack stack : context.getCollectedReagents()) {
            if (!(stack.getItem() instanceof ArmorItem)) continue;
            targetItem = stack;
            break;
        }
        if (targetItem == null) {
            return false;
        }
        ItemStack augmentedArmor = new ItemStack(targetItem.getItem());
        Augmentation.augment(augmentedArmor, new Augmentation());
        ItemEntity item = new ItemEntity((World)world, (double)((float)center.up().getX() + 0.5f),
        		(double)((float)center.up().getY() + 0.5f), (double)((float)center.up().getZ() + 0.5f), augmentedArmor);
        world.addEntity((Entity)item);
		return true;
	}

	@Override
	protected int getApplicationTicks(IRitualContext context) {
		return 10;
	}

}
