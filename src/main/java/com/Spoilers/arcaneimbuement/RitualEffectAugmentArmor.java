package com.Spoilers.arcaneimbuement;

import com.ma.api.recipes.IRitualRecipe;
import com.ma.api.rituals.RitualBlockPos;
import com.ma.api.rituals.RitualEffect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;	
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
            if (!(stack.getItem() instanceof ArmorItem)) continue;
            targetItem = stack;
            
            break;
        }
        if (targetItem == null) {
            return false;
        }
        ItemStack augmentedArmor = new ItemStack(targetItem.getItem());
        Augmentation.augment(augmentedArmor, new Augmentation());
        ItemEntity item = new ItemEntity((World)world, (double)((float)ritualCenter.up().getX() + 0.5f), (double)((float)ritualCenter.up().getY() + 0.5f), (double)((float)ritualCenter.up().getZ() + 0.5f), augmentedArmor);
        world.addEntity((Entity)item);
		return true;
	}

	@Override
	protected int getApplicationTicks(ServerWorld var1, BlockPos var2, IRitualRecipe var3, NonNullList<ItemStack> var4) {
		return 10;
	}
	@Override
    protected boolean modifyRitualReagentsAndPatterns(ItemStack conditionStack, NonNullList<ResourceLocation> patternIDs, NonNullList<RitualBlockPos> reagents) {
        if(!(conditionStack.getItem() instanceof ArmorItem)) {
        	return false;
        }//minecraft:diamond_chestplate
        this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-armor"), reagents, getTargetArmor(conditionStack));
        this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-fabric"), reagents, getFabricReagents(conditionStack));
        this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-focus"), reagents, getFocusReagents(conditionStack));
        return true;
    }

    private void replaceReagents(ResourceLocation key, NonNullList<RitualBlockPos> reagents, NonNullList<ResourceLocation> replacements) {
        if (reagents.size() == 0 || replacements.size() == 0) {
            return;
        }
        int replaceIndex = 0;
        for (RitualBlockPos reagent : reagents) {
            if (reagent.getReagent() == null || !reagent.getReagent().isDynamic() || reagent.getReagent().getResourceLocation().compareTo(key) != 0) continue;
            reagent.getReagent().setResourceLocation((ResourceLocation)replacements.get(replaceIndex));
            if (++replaceIndex < replacements.size()) continue;
            return;
        }
    }
    private NonNullList<ResourceLocation> getTargetArmor(ItemStack conditionStack) {
    	NonNullList<ResourceLocation> chosenItem = NonNullList.create();
    	chosenItem.add(conditionStack.getItem().getRegistryName());
    	return chosenItem;
    }
    
    private NonNullList<ResourceLocation> getFabricReagents(ItemStack conditionStack) {
    	NonNullList<ResourceLocation> possibleItems = NonNullList.create();
    	ArmorItem armorItem = (ArmorItem)conditionStack.getItem();
    	IArmorMaterial material = armorItem.getArmorMaterial();
    	EquipmentSlotType type = EquipmentSlotType.CHEST;
    	if (material.getToughness() > 0 || material.getDamageReductionAmount(type) > 6) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "runic_silk"));
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "runic_silk"));
    	}
    	else if (material.getDamageReductionAmount(type) >= 5 && material.getEnchantability() < 20 && !(material.getName().equals("turtle"))) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "infused_silk"));
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "infused_silk"));
    	}
    	else if (material.getDamageReductionAmount(type) < 5 || material.getEnchantability() > 20 || material.getName().equals("turtle")) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "vellum"));
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "vellum"));
    	}
    	return possibleItems;
    }
    
    private NonNullList<ResourceLocation> getFocusReagents(ItemStack conditionStack) {
    	NonNullList<ResourceLocation> possibleItems = NonNullList.create();
    	ArmorItem armorItem = (ArmorItem)conditionStack.getItem();
    	IArmorMaterial material = armorItem.getArmorMaterial();
    	EquipmentSlotType type = EquipmentSlotType.CHEST;
    	
    	if (material.getToughness() > 0 || material.getDamageReductionAmount(type) > 6) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_greater"));
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_greater"));
    	}
    	else if (material.getDamageReductionAmount(type) >= 5 && material.getEnchantability() < 20 && !(material.getName().equals("turtle"))) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_lesser"));
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_lesser"));
    	}
    	else if (material.getDamageReductionAmount(type) < 5 || material.getEnchantability() > 20 || material.getName().equals("turtle")) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_minor"));
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_minor"));
    	}
    	return possibleItems;
    }
}
