package com.Spoilers.arcaneimbuement.imbuements;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ImbuementManaBoost extends Imbuement {

	public ImbuementManaBoost(String name) {
		super(name);
	}
	
	@Override 
	public void onTick(World world, PlayerEntity player, ItemStack armorStack){
	}
}
