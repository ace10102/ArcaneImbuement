package com.Spoilers.arcaneimbuement.imbuements;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
public class Imbuement {
	
	private static final String IMBUEMENT_TAG = "imbuement";
	private String name;
	
	
	public Imbuement(String name) {
		this.name = name;
	}
	
	public Multimap<String, AttributeModifier> getAttributeModifiers() {
		return HashMultimap.create();
	}
	
	public void onTick(World world, PlayerEntity player, ItemStack armorStack) {
	}
	
}
