package com.Spoilers.arcaneimbuement.rituals;

import com.Spoilers.arcaneimbuement.Augmentation;
import com.ma.api.recipes.IRitualRecipe;
import com.ma.api.rituals.RitualBlockPos;
import com.ma.api.rituals.RitualEffect;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
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

public class RitualEffectImbueArmor extends RitualEffect {
	
	public RitualEffectImbueArmor(ResourceLocation ritualName) {
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
        BlockPos[] checkPositions = new BlockPos[] {ritualCenter.add(3, 0, 3), ritualCenter.add(-3, 0, 3), ritualCenter.add(-3, 0, -3), ritualCenter.add(3, 0, -3)};
		BlockState[] checkStates = new BlockState[]{world.getBlockState(checkPositions[0]), world.getBlockState(checkPositions[1]),
        		world.getBlockState(checkPositions[2]), world.getBlockState(checkPositions[3])};
		for(int i = 0; i < checkPositions.length; i++) {
			BlockPos indexPos = checkPositions[i];
			BlockState indexState = checkStates[i];
			if (indexState.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) return false;
			TileEntity tile = world.getTileEntity(indexPos);
			if (tile instanceof IInventory) {
				IInventory inventory = (IInventory)tile;
				if (inventory.getStackInSlot(0).getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation("mana-and-artifice", "mana_crystal_fragment"))) return false; 
				inventory.getStackInSlot(0).setCount(0);
				world.notifyBlockUpdate(indexPos, indexState, indexState, 2);	
			}	
		}	
		
		BlockPos[] targetPosition = new BlockPos[] {ritualCenter.add(2, 0, 0), ritualCenter.add(-2, 0, 0), ritualCenter.add(0, 0, 2), ritualCenter.add(0, 0, -2)};
		BlockState[] targetStates = new BlockState[]{world.getBlockState(targetPosition[0]), world.getBlockState(targetPosition[1]),
        		world.getBlockState(targetPosition[2]), world.getBlockState(targetPosition[3])};
		for(int i = 0; i < targetPosition.length; i++) {
			BlockPos indexPos = targetPosition[i];
			BlockState indexState = targetStates[i];
				if (indexState.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) continue;
				TileEntity tile = world.getTileEntity(indexPos);
				if (tile instanceof IInventory) {
					IInventory inventory = (IInventory)tile;
					if (inventory.getStackInSlot(0).getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation("mana-and-artifice", "mana_crystal_fragment"))) return false;
					inventory.getStackInSlot(0).setCount(0); //todo replace mana crystal with imbuement focus
					world.notifyBlockUpdate(indexPos, indexState, indexState, 2);			
				}	
        	}
	
        ItemStack imbuedArmor = targetItem.copy();
        Augmentation aug = Augmentation.fromNBT(imbuedArmor.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
        aug.grantExperience((short)50, imbuedArmor.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG)); //todo invent imbuements
        ItemEntity item = new ItemEntity((World)world, (double)((float)ritualCenter.up().getX() + 0.5f), (double)((float)ritualCenter.up().getY() + 0.5f), (double)((float)ritualCenter.up().getZ() + 0.5f), imbuedArmor);
        world.addEntity((Entity)item);
		return true;
	}
	
	@Override
    public boolean canRitualStart(PlayerEntity caster, World world, BlockPos ritualCenter, IRitualRecipe ritual) {
		BlockPos[] checkPositions = new BlockPos[] {ritualCenter.add(3, 0, 3), ritualCenter.add(-3, 0, 3), ritualCenter.add(-3, 0, -3), ritualCenter.add(3, 0, -3)};
		BlockState[] states = new BlockState[]{world.getBlockState(checkPositions[0]), world.getBlockState(checkPositions[1]),
        		world.getBlockState(checkPositions[2]), world.getBlockState(checkPositions[3])};
		for(int i = 0; i < checkPositions.length; i++) {
			BlockPos pos = checkPositions[i];
			BlockState state = states[i];
				if (state.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) return false;
				TileEntity tile = world.getTileEntity(pos);
				if (tile instanceof IInventory) {
					IInventory inventory = (IInventory)tile;
					if (inventory.getStackInSlot(0).getItem() != ForgeRegistries.ITEMS.getValue(new ResourceLocation("mana-and-artifice", "mana_crystal_fragment"))) {
						return false;
					}
				}	
        	}
		BlockPos[] targetPosition = new BlockPos[] {ritualCenter.add(2, 0, 0), ritualCenter.add(-2, 0, 0), ritualCenter.add(0, 0, 2), ritualCenter.add(0, 0, -2)};
		BlockState[] targetStates = new BlockState[]{world.getBlockState(targetPosition[0]), world.getBlockState(targetPosition[1]),
        		world.getBlockState(targetPosition[2]), world.getBlockState(targetPosition[3])};
		for (BlockState state : targetStates) {
 			if (state.getBlock() == ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) return true;
		}	//todo add check for tbd imbuement foci
        return true;
	}
	
	@Override
	protected int getApplicationTicks(ServerWorld world, BlockPos pos, IRitualRecipe recipe, NonNullList<ItemStack> reagents) {
		return 10;
	}
	
	@Override
    protected boolean modifyRitualReagentsAndPatterns(ItemStack conditionStack, NonNullList<ResourceLocation> patternIDs, NonNullList<RitualBlockPos> reagents) {
		if(!(conditionStack.getItem() instanceof ArmorItem) || !Augmentation.isAugmented(conditionStack)) {
        	return false;
		}
        BlockPos pos = reagents.get(41).getBlockPos(); //throws error in console if attempt to start ritual directly after activating it, might crash server idk
        ItemStack imbueStack = this.getImbuePedestal(pos); 
       
        if(imbueStack == null) return false;        
        this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-armor"), reagents, getTargetArmor(conditionStack));
        this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-focus"), reagents, getFocusReagents(conditionStack));
        this.replaceReagents(new ResourceLocation("arcaneimbuement:dynamic-reagent"), reagents, getImbueReagents(imbueStack));
        return true;
    }
	
	private ItemStack getImbuePedestal(BlockPos pos) {
		World world = Minecraft.getInstance().world;
		ItemStack imbueItem = null;
		BlockPos[] targetPosition = new BlockPos[] {pos.add(2, 0, 0), pos.add(-2, 0, 0), pos.add(0, 0, 2), pos.add(0, 0, -2)};
		BlockState[] targetStates = new BlockState[]{world.getBlockState(targetPosition[0]), world.getBlockState(targetPosition[1]),
        		world.getBlockState(targetPosition[2]), world.getBlockState(targetPosition[3])};
		for(int i = 0; i < targetPosition.length; i++) {
			BlockPos indexPos = targetPosition[i];
			BlockState state = targetStates[i];
				if (state.getBlock() != ForgeRegistries.BLOCKS.getValue(new ResourceLocation("mana-and-artifice", "pedestal"))) continue;
				TileEntity tile = world.getTileEntity(indexPos);
				if (tile instanceof IInventory) {
					IInventory inventory = (IInventory)tile;
					if (inventory.getStackInSlot(0).getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("mana-and-artifice", "mana_crystal_fragment"))) {
						imbueItem = inventory.getStackInSlot(0); //todo update for imbuement foci
					}
				}	
        	}
		return imbueItem;
	}
	
    private void replaceReagents(ResourceLocation key, NonNullList<RitualBlockPos> reagents, NonNullList<ResourceLocation> replacements) {
        if (reagents.size() == 0 || replacements.size() == 0) {
            return;
        }
        int replaceIndex = 0;
        for (RitualBlockPos reagent : reagents) {
        	if (reagent.getReagent() == null) continue;
            if (reagent.getReagent().getResourceLocation().compareTo(key) == 0) {
            	reagent.getReagent().setResourceLocation((ResourceLocation)replacements.get(replaceIndex));
            }//todo will need logic update for larger replacement lists
        }
        return;
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
    	}
    	else if (level >= 3) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_lesser"));
    	}
    	else if (level >= 0) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_minor"));
    	}
    	return possibleItems;
    }
    
    private NonNullList<ResourceLocation> getImbueReagents(ItemStack conditionStack) {
    	NonNullList<ResourceLocation> possibleItems = NonNullList.create();
    	if(conditionStack.getItem() == ForgeRegistries.ITEMS.getValue(new ResourceLocation("mana-and-artifice", "mana_crystal_fragment"))) {
    		possibleItems.add(new ResourceLocation("mana-and-artifice", "ritual_focus_greater"));
    	}	
    	return possibleItems; //todo imbuement reagent lists and foci
    }
}
