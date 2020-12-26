package com.Spoilers.arcaneimbuement.rituals;

import com.Spoilers.arcaneimbuement.augments.Augmentation;
import com.ma.api.rituals.IRitualContext;
import com.ma.api.rituals.RitualEffect;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

public class RitualEffectLevelUpArmor extends RitualEffect {
	
	private ResourceLocation pedestalFocus = new ResourceLocation("mana-and-artifice", "ritual_focus_minor");
	
	public RitualEffectLevelUpArmor(ResourceLocation ritualName) {
		super(ritualName);
	}
	
	@Override
	protected boolean applyRitualEffect(IRitualContext context) {
		World world = context.getWorld();
		BlockPos center = context.getCenter();
		ItemStack targetItem = ItemStack.EMPTY;
		for (ItemStack stack : context.getCollectedReagents()) {
			if (!(stack.getItem() instanceof ArmorItem) || !Augmentation.isAugmented(stack)) continue;
			targetItem = stack;
			break;
		}
		if (targetItem == null) {
			return false;
		}
		BlockPos[] pedestalPos = new BlockPos[] { center.add(4, 0, 0), center.add(-4, 0, 0),
				center.add(0, 0, 4), center.add(0, 0, -4) };
		for(int i = 0; i< pedestalPos.length; i++) {
			BlockState pedestalState = world.getBlockState(pedestalPos[i]);
			if (pedestalState.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) return false;
			IInventory pedestalInventory = (IInventory) world.getTileEntity(pedestalPos[i]);
			if (!pedestalInventory.getStackInSlot(0).getItem().getRegistryName().equals(pedestalFocus)) return false;
			pedestalInventory.getStackInSlot(0).setCount(0);
			world.notifyBlockUpdate(pedestalPos[i], pedestalState, pedestalState, 2);	
		}
		
		ItemStack imbuedArmor = targetItem.copy();
		Augmentation aug = Augmentation.fromNBT(imbuedArmor.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
		aug.grantExperience((short) -50, imbuedArmor.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
		aug.increaseLevel(imbuedArmor.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
		ItemEntity item = new ItemEntity((World) world, (double) ((float) center.up().getX() + 0.5f), 
				(double) ((float) center.up().getY() + 0.5f), (double) ((float) center.up().getZ() + 0.5f), imbuedArmor);
		world.addEntity((Entity) item);
		return true;
	}
	
	@Override
	public boolean canRitualStart(IRitualContext context) {
		World world = context.getWorld();
		BlockPos center = context.getCenter();
		BlockPos[] shelvesPos = new BlockPos[] { center.add(3, 0, 4), center.add(4, 0, 4), center.add(4, 0, 3),
				center.add(-3, 0, 4), center.add(-4, 0, 4), center.add(-4, 0, 3),
				center.add(-3, 0, -4), center.add(-4, 0, -4), center.add(-4, 0, -3),
				center.add(3, 0, -4), center.add(4, 0, -4), center.add(4, 0, -3) };
		for(int i = 0; i < shelvesPos.length; i++) {
			BlockState shelvesState = world.getBlockState(shelvesPos[i]);
			if (shelvesState.getBlock() != Blocks.BOOKSHELF) return false;
		}
		
		BlockPos[] pedestalPos = new BlockPos[] { center.add(4, 0, 0), center.add(-4, 0, 0),
				center.add(0, 0, 4), center.add(0, 0, -4) };
		for(int i = 0; i< pedestalPos.length; i++) {
			BlockState pedestalState = world.getBlockState(pedestalPos[i]);
			if (pedestalState.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) return false;
			IInventory pedestalInventory = (IInventory) world.getTileEntity(pedestalPos[i]);
			if (!pedestalInventory.getStackInSlot(0).getItem().getRegistryName().equals(pedestalFocus)) return false;
			
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
		Augmentation aug = Augmentation.fromNBT(conditionStack.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
		byte level = aug.getLevel();
		if (level >= 5 || aug.getExperience() < aug.getExperienceForLevel(level)) {
			return false;
		}
			
		/*this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-armor"), reagents, getTargetArmor(conditionStack));
		if (level >= 2) this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-fabric"), reagents, getFabricReagents(conditionStack));
		if (level >= 1 && level < 4) this.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_1"), reagents, getExpBottle(conditionStack));
		if (level >= 2) this.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_2"), reagents, getExpBottle(conditionStack));
		if (level >= 3) this.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_3"), reagents, getExpBottle(conditionStack));
		if (level >= 4) this.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_4"), reagents, getExpBottle(conditionStack));*/
/*		context.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-armor"), getTargetArmor(conditionStack));
		if (level >= 2) context.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-fabric"), getFabricReagents(conditionStack));
		if (level >= 1 && level < 4) context.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_1"), getExpBottle(conditionStack));
		if (level >= 2) context.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_2"), getExpBottle(conditionStack));
		if (level >= 3) context.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_3"), getExpBottle(conditionStack));
		if (level >= 4) context.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_4"), getExpBottle(conditionStack));*/
		RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:dynamic-armor"), getTargetArmor(conditionStack));
		if (level >= 2) RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:dynamic-fabric"), getFabricReagents(conditionStack));
		if (level >= 1 && level < 4) RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:experience_bottle_1"), getExpBottle(conditionStack));
		if (level >= 2) RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:experience_bottle_2"), getExpBottle(conditionStack));
		if (level >= 3) RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:experience_bottle_3"), getExpBottle(conditionStack));
		if (level >= 4) RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:experience_bottle_4"), getExpBottle(conditionStack));
		
		this.setPedestalFocus(conditionStack);
		return true;
	}
	
	private NonNullList<ResourceLocation> getTargetArmor(ItemStack conditionStack) {
		NonNullList<ResourceLocation> chosenItem = NonNullList.create();
		chosenItem.add(conditionStack.getItem().getRegistryName());
		return chosenItem;
	}
	
	private NonNullList<ResourceLocation> getFabricReagents(ItemStack conditionStack) {
		NonNullList<ResourceLocation> possibleItems = NonNullList.create();
		byte level = Augmentation.fromNBT(conditionStack.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG)).getLevel();
		if (level >= 4) {
			possibleItems.add(new ResourceLocation("mana-and-artifice", "runic_silk"));
		}
		else if (level >= 2) {
			possibleItems.add(new ResourceLocation("mana-and-artifice", "infused_silk"));
		}
		return possibleItems;
	}
	
	private NonNullList<ResourceLocation> getExpBottle(ItemStack conditionStack) {
		NonNullList<ResourceLocation> possibleItems = NonNullList.create();
		possibleItems.add(new ResourceLocation("minecraft", "experience_bottle"));
		return possibleItems;
	}
	
	private void setPedestalFocus(ItemStack conditionStack) {
		byte level = Augmentation.fromNBT(conditionStack.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG)).getLevel();
		if (level >= 4) this.pedestalFocus = new ResourceLocation("mana-and-artifice", "ritual_focus_greater");
		else if (level >= 2) this.pedestalFocus = new ResourceLocation("mana-and-artifice", "ritual_focus_lesser");
	}
}
