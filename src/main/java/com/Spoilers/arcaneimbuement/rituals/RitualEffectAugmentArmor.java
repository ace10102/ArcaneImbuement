package com.Spoilers.arcaneimbuement.rituals;

import com.Spoilers.arcaneimbuement.augments.Augmentation;
import com.ma.api.rituals.IRitualContext;
import com.ma.api.rituals.RitualEffect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualEffectAugmentArmor extends RitualEffect {
	
	public RitualEffectAugmentArmor(ResourceLocation ritualName) {
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
	@Override
    protected boolean modifyRitualReagentsAndPatterns(ItemStack conditionStack, IRitualContext context) {
        if(!(conditionStack.getItem() instanceof ArmorItem)) {
        	return false;
        }
        RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:dynamic-armor"), getTargetArmor(conditionStack));
        RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:dynamic-fabric"), getFabricReagents(conditionStack));
        RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:dynamic-focus"), getFocusReagents(conditionStack));
        /*context.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-armor"), getTargetArmor(conditionStack));
        context.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-fabric"), getFabricReagents(conditionStack));
        context.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-focus"), getFocusReagents(conditionStack));*/
        /*this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-armor"), reagents, getTargetArmor(conditionStack));
        this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-fabric"), reagents, getFabricReagents(conditionStack));
        this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-focus"), reagents, getFocusReagents(conditionStack));*/
        return true;
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
    	}
    	else if (material.getDamageReductionAmount(type) >= 5 && material.getEnchantability() < 20 && !(material.getName().equals("turtle"))) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "infused_silk"));
    	}
    	else if (material.getDamageReductionAmount(type) < 5 || material.getEnchantability() > 20 || material.getName().equals("turtle")) {
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
    	}
    	else if (material.getDamageReductionAmount(type) >= 5 && material.getEnchantability() < 20 && !(material.getName().equals("turtle"))) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_lesser"));
    	}
    	else if (material.getDamageReductionAmount(type) < 5 || material.getEnchantability() > 20 || material.getName().equals("turtle")) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_minor"));
    	}
    	return possibleItems;
    }
}
