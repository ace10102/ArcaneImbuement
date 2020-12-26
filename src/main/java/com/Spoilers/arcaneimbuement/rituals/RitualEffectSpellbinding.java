package com.Spoilers.arcaneimbuement.rituals;

import com.Spoilers.arcaneimbuement.augments.Augmentation;
import com.ma.api.rituals.IRitualContext;
import com.ma.api.rituals.RitualBlockPos;
import com.ma.api.rituals.RitualEffect;
import com.ma.spells.crafting.SpellRecipe;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualEffectSpellbinding extends RitualEffect {
	
	private NonNullList<ResourceLocation> triggerReagents = NonNullList.create();
	
	public RitualEffectSpellbinding(ResourceLocation ritualName) {
		super(ritualName);
	}

	@Override
	protected boolean applyRitualEffect(IRitualContext context) {
		World world = context.getWorld();
		BlockPos center = context.getCenter();
		ItemStack armorItem = null;
		ItemStack spellItem = null;
		
		for (ItemStack stack : context.getCollectedReagents()) {
			if (stack.getItem() instanceof ArmorItem || Augmentation.isAugmented(stack)) {
				armorItem = stack;
			}
			if (stack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("mana-and-artifice", "spell"))) {
				spellItem = stack;
			}
		}
		if (armorItem == null || spellItem == null /*|| Augmentation.fromNBT(armorItem.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG)).getLevel() < 5*/) return false;
		
		ItemStack spellboundArmor = armorItem.copy();
		SpellRecipe spellRecipe = SpellRecipe.fromNBT(spellItem.getOrCreateTag());
		spellRecipe.setMysterious(false);
		spellRecipe.writeToNBT(spellboundArmor.getOrCreateTag());
		ItemEntity item = new ItemEntity((World) world, (double) ((float) center.up().getX() + 0.5f), 
				(double) ((float) center.up().getY() + 0.5f), (double) ((float) center.up().getZ() + 0.5f), spellboundArmor);
		world.addEntity((Entity) item);
		return true;
	}
	
	@Override
	public boolean canRitualStart(IRitualContext context) {
		World world = context.getWorld();
		BlockPos center = context.getCenter();
		BlockPos[] armorRune = new BlockPos[] { center.add(-2, 0, 0), center.add(0, 0, 2) };
		for(int i = 0; i < armorRune.length; i++) {
			BlockState runeState = world.getBlockState(armorRune[i]);
			if (!runeState.hasTileEntity()) continue;
			IInventory runeInv = (IInventory) world.getTileEntity(armorRune[i]);
			if (!(runeInv.getStackInSlot(0).getItem() instanceof ArmorItem)) return false;
			Augmentation aug = Augmentation.fromNBT(runeInv.getStackInSlot(0).getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
			if (aug.getLevel() < 5) return false;
		}
		BlockPos[] pedestalPos = new BlockPos[] { center.add(-3, 0, 3), center.add(3, 0, 3),
				center.add(3, 0, -3), center.add(-3, 0, -3) };
		for(int i = 0; i< pedestalPos.length; i++) {
			BlockState pedestalState = world.getBlockState(pedestalPos[i]);
			if (pedestalState.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) return false;
			IInventory pedestalInventory = (IInventory) world.getTileEntity(pedestalPos[i]);
			byte matchCount = 0;
			for(ResourceLocation rloc : triggerReagents) {
				if (pedestalInventory.getStackInSlot(0).getItem().getRegistryName().equals(rloc)) {
					matchCount++;
					break;
				}
			}
			if (matchCount == 0) return false;	
		}
		return true;
	}
	
	@Override
	protected int getApplicationTicks(IRitualContext context) {
		return 10;
	}
	
	@Override
	protected boolean modifyRitualReagentsAndPatterns(ItemStack conditionStack, IRitualContext context) {
		if (!(conditionStack.getItem() instanceof ArmorItem) || !Augmentation.isAugmented(conditionStack)) {
			return false;
		}
		byte level = Augmentation.fromNBT(conditionStack.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG)).getLevel();
		if (level < 5) return false;
		
		ItemStack triggerStack = null;
		for(RitualBlockPos rbp : context.getIndexedPositions()) {
			if (rbp.getIndex() == 3) 
				triggerStack = new ItemStack(ForgeRegistries.ITEMS.getValue(rbp.getReagent().getResourceLocation()));
		}
		if (triggerStack == null) return false;
		
		/*ItemStack spellStack = null;
		for(RitualBlockPos rbp : reagents) {
			if (rbp.getIndex() == 2)
				spellStack = blablabla
		}*/
		RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:dynamic-armor"), getTargetArmor(conditionStack));
		RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:dynamic-trigger"),getTriggerReagents(triggerStack));
		/*context.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-armor"), getTargetArmor(conditionStack));
		context.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-trigger"),getTriggerReagents(triggerStack));*/
		/*this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-armor"), reagents, getTargetArmor(conditionStack));
		this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-trigger"), reagents, getTriggerReagents(triggerStack));*/
//		this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-spell-reagents"), reagents, getSpellReagents(spellStack));
		return true;
	}
	
	private NonNullList<ResourceLocation> getTargetArmor(ItemStack conditionStack) {
		NonNullList<ResourceLocation> chosenItem = NonNullList.create();
		chosenItem.add(conditionStack.getItem().getRegistryName());
		return chosenItem;
	}
	
	private NonNullList<ResourceLocation> getTriggerReagents(ItemStack conditionStack) {
		NonNullList<ResourceLocation> triggerReagents = NonNullList.create();
		
		return triggerReagents;
	}
}
