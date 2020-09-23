package com.Spoilers.arcaneimbuement.rituals;

import com.Spoilers.arcaneimbuement.Augmentation;
import com.ma.api.recipes.IRitualRecipe;
import com.ma.api.rituals.RitualBlockPos;
import com.ma.api.rituals.RitualEffect;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

public class RitualEffectLevelUpArmor extends RitualEffect {
	
	private ResourceLocation pedestalFocus = new ResourceLocation("mana-and-artifice", "ritual_focus_minor");
	
	public RitualEffectLevelUpArmor(ResourceLocation ritualName) {
		super(ritualName);
	}
	@Override
	protected boolean applyRitualEffect(PlayerEntity ritualCaster, ServerWorld world, BlockPos ritualCenter, IRitualRecipe completedRecipe, NonNullList<ItemStack> reagentList) {
		ItemStack targetItem = ItemStack.EMPTY;
		for (ItemStack stack : reagentList) {
			if (!(stack.getItem() instanceof ArmorItem) || !Augmentation.isAugmented(stack)) continue;
			targetItem = stack;
			break;
		}
		if (targetItem == null) {
			return false;
		}
		BlockPos[] pedestalPos = new BlockPos[] { ritualCenter.add(4, 0, 0), ritualCenter.add(-4, 0, 0),
				ritualCenter.add(0, 0, 4), ritualCenter.add(0, 0, -4) };
		for(int i = 0; i< pedestalPos.length; i++) {
			BlockState pedestalState = world.getBlockState(pedestalPos[i]);
			if (pedestalState.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) return false;
			TileEntity pedestalTile = world.getTileEntity(pedestalPos[i]);
			if (pedestalTile instanceof IInventory) {
				IInventory inventory = (IInventory) pedestalTile;
				if (inventory.getStackInSlot(0).getItem() != ForgeRegistries.ITEMS.getValue(pedestalFocus)) return false;
				inventory.getStackInSlot(0).setCount(0);
				world.notifyBlockUpdate(pedestalPos[i], pedestalState, pedestalState, 2);
			}
		}
		
		ItemStack imbuedArmor = targetItem.copy();
		Augmentation aug = Augmentation.fromNBT(imbuedArmor.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
		aug.grantExperience((short) -50, imbuedArmor.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
		aug.increaseLevel(imbuedArmor.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
		ItemEntity item = new ItemEntity((World) world, (double) ((float) ritualCenter.up().getX() + 0.5f), 
				(double) ((float) ritualCenter.up().getY() + 0.5f), (double) ((float) ritualCenter.up().getZ() + 0.5f), imbuedArmor);
		world.addEntity((Entity) item);
		return true;
	}
	
	@Override
	public boolean canRitualStart(PlayerEntity caster, World world, BlockPos ritualCenter, IRitualRecipe ritual) {
		BlockPos[] shelvesPos = new BlockPos[] { ritualCenter.add(3, 0, 4), ritualCenter.add(4, 0, 4), ritualCenter.add(4, 0, 3),
				ritualCenter.add(-3, 0, 4), ritualCenter.add(-4, 0, 4), ritualCenter.add(-4, 0, 3),
				ritualCenter.add(-3, 0, -4), ritualCenter.add(-4, 0, -4), ritualCenter.add(-4, 0, -3),
				ritualCenter.add(3, 0, -4), ritualCenter.add(4, 0, -4), ritualCenter.add(4, 0, -3) };
		for(int i = 0; i < shelvesPos.length; i++) {
			BlockState shelvesState = world.getBlockState(shelvesPos[i]);
			if (shelvesState.getBlock() != Blocks.BOOKSHELF) return false;
		}
		
		BlockPos[] pedestalPos = new BlockPos[] { ritualCenter.add(4, 0, 0), ritualCenter.add(-4, 0, 0),
				ritualCenter.add(0, 0, 4), ritualCenter.add(0, 0, -4) };
		for(int i = 0; i< pedestalPos.length; i++) {
			BlockState pedestalState = world.getBlockState(pedestalPos[i]);
			if (pedestalState.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) return false;
			TileEntity pedestalTile = world.getTileEntity(pedestalPos[i]);
			if (pedestalTile instanceof IInventory) {
				IInventory inventory = (IInventory) pedestalTile;
				if (inventory.getStackInSlot(0).getItem() != ForgeRegistries.ITEMS.getValue(pedestalFocus)) return false;
			}
		}
		
		return true;
	}
	
	@Override
	protected int getApplicationTicks(ServerWorld world, BlockPos pos, IRitualRecipe recipe, NonNullList<ItemStack> reagents) {
		return 10;
	}
	
	@Override
	protected boolean modifyRitualReagentsAndPatterns(ItemStack conditionStack, NonNullList<ResourceLocation> patternIDs, NonNullList<RitualBlockPos> reagents) {
		if (!(conditionStack.getItem() instanceof ArmorItem) || !Augmentation.isAugmented(conditionStack)) {
			return false;
		}
		byte level = Augmentation.fromNBT(conditionStack.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG)).getLevel();
		if (level >= 5) { //add experience for level up code
			return false;
		}
			
		this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-armor"), reagents, getTargetArmor(conditionStack));
		if (level >= 2) this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-fabric"), reagents, getFabricReagents(conditionStack));
		if (level >= 1 && level < 4) this.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_1"), reagents, getExpBottle(conditionStack));
		if (level >= 2) this.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_2"), reagents, getExpBottle(conditionStack));
		if (level >= 3) this.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_3"), reagents, getExpBottle(conditionStack));
		if (level >= 4) this.replaceReagents(new ResourceLocation("arcaneimbuement:experience_bottle_4"), reagents, getExpBottle(conditionStack));
		
		this.setPedestalFocus(conditionStack);
		return true;
	}
	
	private void replaceReagents(ResourceLocation key, NonNullList<RitualBlockPos> reagents, NonNullList<ResourceLocation> replacements) {
		if (reagents.size() == 0 || replacements.size() == 0) {
			return;
		}
		int replaceIndex = 0;
		for (RitualBlockPos reagent : reagents) {
			if (reagent.getReagent() == null) continue;
			if (reagent.getReagent().getResourceLocation().compareTo(key) == 0) {
				reagent.getReagent().setResourceLocation((ResourceLocation) replacements.get(replaceIndex));
			} 
		}
		return;
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
