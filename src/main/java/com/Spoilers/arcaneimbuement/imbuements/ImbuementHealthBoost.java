package com.Spoilers.arcaneimbuement.imbuements;

import java.util.UUID;

import org.apache.commons.codec.binary.StringUtils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ImbuementHealthBoost extends Imbuement {
	private static final String name = "HealthBoost";
	
	public ImbuementHealthBoost(String name) {
		super(name);
	}
	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers() {
		Multimap<String, AttributeModifier> modifierMap = HashMultimap.create();
		String name = "HealthBoost";
		modifierMap.put(Attributes.MAX_HEALTH.getAttributeName(), new AttributeModifier(UUID.nameUUIDFromBytes(StringUtils.getBytesUtf8(name)), "HealthBoost", 10.0, Operation.ADDITION));
		return modifierMap;
	}
	@Override 
	public void onTick(World world, PlayerEntity player, ItemStack armorStack){
	}
}
