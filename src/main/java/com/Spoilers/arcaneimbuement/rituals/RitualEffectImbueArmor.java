package com.Spoilers.arcaneimbuement.rituals;

import com.Spoilers.arcaneimbuement.augments.Augmentation;
import com.ma.api.rituals.IRitualContext;
import com.ma.api.rituals.RitualBlockPos;
import com.ma.api.rituals.RitualEffect;

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

public class RitualEffectImbueArmor extends RitualEffect {

	public RitualEffectImbueArmor(ResourceLocation ritualName) {
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
		BlockPos[] checkPositions = new BlockPos[] { center.add(3, 0, 3), center.add(-3, 0, 3),
				center.add(-3, 0, -3), center.add(3, 0, -3) };
		for (int i = 0; i < checkPositions.length; i++) {
			BlockState indexState = world.getBlockState(checkPositions[i]);
			if (indexState.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) return false;
			IInventory inventory = (IInventory) world.getTileEntity(checkPositions[i]);
			if (inventory.getStackInSlot(0).getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation("mana-and-artifice", "mana_crystal_fragment"))) return false;
			inventory.getStackInSlot(0).setCount(0);
			world.notifyBlockUpdate(checkPositions[i], indexState, indexState, 2);	
		}

		BlockPos[] targetPosition = new BlockPos[] { center.add(2, 0, 0), center.add(-2, 0, 0),
				center.add(0, 0, 2), center.add(0, 0, -2) };
		for (int i = 0; i < targetPosition.length; i++) {
			BlockState indexState = world.getBlockState(targetPosition[i]);
			if (indexState.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) continue;
			IInventory inventory = (IInventory) world.getTileEntity(targetPosition[i]);
			if (inventory.getStackInSlot(0).getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation("mana-and-artifice", "mana_crystal_fragment"))) return false;
			inventory.getStackInSlot(0).setCount(0); // todo replace mana crystal with imbuement focus
			world.notifyBlockUpdate(targetPosition[i], indexState, indexState, 2);		
		}

		ItemStack imbuedArmor = targetItem.copy();
		Augmentation aug = Augmentation.fromNBT(imbuedArmor.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
		aug.grantExperience((short) 50, imbuedArmor.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG)); // todo invent imbuements
		ItemEntity item = new ItemEntity((World) world, (double) ((float) center.up().getX() + 0.5f), 
				(double) ((float) center.up().getY() + 0.5f), (double) ((float) center.up().getZ() + 0.5f), imbuedArmor);
		world.addEntity((Entity) item);
		return true;
	}

	@Override
	public boolean canRitualStart(IRitualContext context) {
		World world = context.getWorld();
		BlockPos center = context.getCenter();
		BlockPos[] checkPositions = new BlockPos[] { center.add(3, 0, 3), center.add(-3, 0, 3),
				center.add(-3, 0, -3), center.add(3, 0, -3) };
		for (int i = 0; i < checkPositions.length; i++) {
			BlockState state = world.getBlockState(checkPositions[i]);
			if (state.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal")))return false;
			IInventory inventory = (IInventory) world.getTileEntity(checkPositions[i]);
			if (inventory.getStackInSlot(0).getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation("mana-and-artifice", "mana_crystal_fragment"))) {
				return false;		
			}
		}//probably a better way to do this v
		BlockPos[] targetPosition = new BlockPos[] { center.add(2, 0, 0), center.add(-2, 0, 0), center.add(0, 0, 2), center.add(0, 0, -2) };
		BlockState[] targetStates = new BlockState[] { world.getBlockState(targetPosition[0]), world.getBlockState(targetPosition[1]),
				world.getBlockState(targetPosition[2]), world.getBlockState(targetPosition[3]) };
		for (BlockState state : targetStates) {
			if (state.getBlock() == ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal")))return true;
		} // todo add check for tbd imbuement foci
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
//		BlockPos posit = null;
//		reagents.stream().filter(r -> r.getIndex() == 18).findFirst().ifPresent(pos -> {posit = pos.getBlockPos();}); //i will figure out how streams work someday, but not today
		BlockPos pos = null;
		for (RitualBlockPos rbp : context.getIndexedPositions()) {
			if (rbp.getIndex() == 18)
				pos = rbp.getBlockPos();
		}
		ItemStack imbueStack = null;
		if (pos != null) {
			imbueStack = this.getImbuePedestal(pos, context.getWorld());
		}

		if (imbueStack == null)return false;
		RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:dynamic-armor"), getTargetArmor(conditionStack));
		RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:dynamic-focus"), getFocusReagents(conditionStack));
		RitualUtils.replaceReagents(context, new ResourceLocation("arcaneimbuement:dynamic-reagent"), getImbueReagents(imbueStack));
		/*context.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-armor"), getTargetArmor(conditionStack));
		context.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-focus"), getFocusReagents(conditionStack));
		context.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-reagent"), getImbueReagents(imbueStack));*/
		/*this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-armor"), reagents,getTargetArmor(conditionStack));
		this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-focus"), reagents,getFocusReagents(conditionStack));
		this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-reagent"), reagents,getImbueReagents(imbueStack));*/
		return true;
	}

	private ItemStack getImbuePedestal(BlockPos pos, World world) {
//		World world = Minecraft.getInstance().world; //finally can get rid of this hacky shit
		ItemStack imbueItem = null;
		BlockPos[] targetPosition = new BlockPos[] { pos.add(2, 0, 0), pos.add(-2, 0, 0), pos.add(0, 0, 2), pos.add(0, 0, -2) };
		for (int i = 0; i < targetPosition.length; i++) {
			BlockState state = world.getBlockState(targetPosition[i]);
			if (state.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) continue;
			IInventory inventory = (IInventory) world.getTileEntity(targetPosition[i]);
			if (inventory.getStackInSlot(0).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("mana-and-artifice", "mana_crystal_fragment"))) {
				imbueItem = inventory.getStackInSlot(0); // todo update for imbuement foci
			}
			
		}
		return imbueItem;
	}

	private NonNullList<ResourceLocation> getTargetArmor(ItemStack conditionStack) {
		NonNullList<ResourceLocation> chosenItem = NonNullList.create();
		chosenItem.add(conditionStack.getItem().getRegistryName());
		return chosenItem;
	}

	private NonNullList<ResourceLocation> getFocusReagents(ItemStack conditionStack) {
		NonNullList<ResourceLocation> possibleItems = NonNullList.create();
		Augmentation aug = Augmentation.fromNBT(conditionStack.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
		byte level = aug.getLevel();

		if (level > 4) {
			possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_greater"));
		} else if (level >= 3) {
			possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_lesser"));
		} else if (level >= 0) {
			possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_minor"));
		}
		return possibleItems;
	}

	private NonNullList<ResourceLocation> getImbueReagents(ItemStack conditionStack) {
		NonNullList<ResourceLocation> possibleItems = NonNullList.create();
		if (conditionStack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("mana-and-artifice", "mana_crystal_fragment"))) {
			possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_greater"));
		}
		return possibleItems; // todo imbuement reagent lists and foci
	}
}
