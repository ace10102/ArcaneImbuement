package com.Spoilers.arcaneimbuement.augments;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class AugmentedArmorHandler {
	
	public static void grantAugmentEXP(LivingHurtEvent event) {
		LivingEntity entity = event.getEntityLiving();
		DamageSource source = event.getSource();
		float damage = event.getAmount();
		float armorDamage = 0;
		System.out.println(damage);
		if(!source.isUnblockable()) {
			if (!(damage <= 0.0F)) {
				armorDamage = damage / 4.0F;
				if (armorDamage < 1.0F) {
					armorDamage = 1.0F;
				}
			}
			if (entity instanceof PlayerEntity) {
				for(int i = 0; i < ((PlayerEntity)entity).inventory.armorInventory.size(); ++i) {
					ItemStack itemstack = ((PlayerEntity)entity).inventory.armorInventory.get(i);
					if ((!source.isFireDamage() || !itemstack.getItem().isImmuneToFire()) && itemstack.getItem() instanceof ArmorItem && Augmentation.isAugmented(itemstack)) {
					
						Augmentation augment = Augmentation.fromNBT(itemstack.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
						augment.grantExperience((short)armorDamage, itemstack.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
						System.out.println((short)armorDamage);
					}
				}
			}
		}
	}
}
