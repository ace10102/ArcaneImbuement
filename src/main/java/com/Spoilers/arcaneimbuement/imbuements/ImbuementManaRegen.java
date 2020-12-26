package com.Spoilers.arcaneimbuement.imbuements;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ImbuementManaRegen extends Imbuement {

	public ImbuementManaRegen(String name) {
		super(name);
	}

	@Override 
	public void onTick(World world, PlayerEntity player, ItemStack armorStack){
	}
}
