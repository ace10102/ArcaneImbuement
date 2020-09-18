package com.Spoilers.arcaneimbuement;

import com.ma.api.recipes.IRitualRecipe;
//import com.ma.api.rituals.RitualBlockPos;
import com.ma.api.rituals.RitualEffect;
//import com.ma.spells.crafting.SpellRecipe;

import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class RitualEffectAugmentArmor extends RitualEffect {
	
	public RitualEffectAugmentArmor(ResourceLocation ritualName) {
		super(ritualName);
	}

	@Override
	protected boolean applyRitualEffect(PlayerEntity ritualCaster, ServerWorld world, BlockPos ritualCenter, IRitualRecipe completedRecipe, NonNullList<ItemStack> reagentList) {
		ItemStack targetItem = ItemStack.EMPTY;
		for (ItemStack stack : reagentList) {
            if (stack.getItem() != Items.DIAMOND_CHESTPLATE) continue;
            targetItem = stack;
            break;
        }
        if (targetItem == null) {
            return false;
        }
        ItemStack augmentedArmor = new ItemStack((IItemProvider)Items.DIAMOND_CHESTPLATE);
        CompoundNBT test = new CompoundNBT();
        test.putString("test", "test");
        augmentedArmor.getOrCreateTag().put("test",(INBT)test);
        ItemEntity item = new ItemEntity((World)world, (double)((float)ritualCenter.up().getX() + 0.5f), (double)((float)ritualCenter.up().getY() + 0.5f), (double)((float)ritualCenter.up().getZ() + 0.5f), augmentedArmor);
        world.addEntity((Entity)item);
		return true;
	}

	@Override
	protected int getApplicationTicks(ServerWorld var1, BlockPos var2, IRitualRecipe var3, NonNullList<ItemStack> var4) {
		return 0;
	}
/*	@Override
    protected boolean modifyRitualReagentsAndPatterns(ItemStack dataStack, NonNullList<ResourceLocation> patternIDs, NonNullList<RitualBlockPos> reagents) {
        if (!SpellRecipe.isReagentContainer(dataStack)) {
            return false;
        }
        this.replaceReagents(new ResourceLocation("mana-and-artifice:dynamic-shape"), reagents, SpellRecipe.getShapeReagents(dataStack));
        this.replaceReagents(new ResourceLocation("mana-and-artifice:dynamic-component"), reagents, SpellRecipe.getComponentReagents(dataStack));
        this.replaceReagents(new ResourceLocation("mana-and-artifice:dynamic-modifier-1"), reagents, SpellRecipe.getModifierReagents(dataStack, 0));
        this.replaceReagents(new ResourceLocation("mana-and-artifice:dynamic-modifier-2"), reagents, SpellRecipe.getModifierReagents(dataStack, 1));
        this.replaceReagents(new ResourceLocation("mana-and-artifice:dynamic-modifier-3"), reagents, SpellRecipe.getModifierReagents(dataStack, 2));
        this.replaceReagents(new ResourceLocation("mana-and-artifice:dynamic-complexity"), reagents, SpellRecipe.getComplexityReagents(dataStack));
        patternIDs.clear();
        patternIDs.addAll(SpellRecipe.getPatterns(dataStack));
        return true;
    }

    private void replaceReagents(ResourceLocation key, NonNullList<RitualBlockPos> reagents, NonNullList<ResourceLocation> replacements) {
        if (reagents.size() == 0 || replacements.size() == 0) {
            return;
        }
        int replaceIndex = 0;
        for (RitualBlockPos reagent : reagents) {
            if (!reagent.getReagent().isDynamic() || reagent.getReagent().getResourceLocation().compareTo(key) != 0) continue;
            reagent.getReagent().setResourceLocation((ResourceLocation)replacements.get(replaceIndex));
            if (++replaceIndex < replacements.size()) continue;
            return;
        }
    }*/
}
