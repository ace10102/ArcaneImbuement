package com.Spoilers.arcaneimbuement;

import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.ma.api.ManaAndArtificeMod;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;


@Mod.EventBusSubscriber(modid = ArcaneImbuement.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpellTriggerHandler {
	
	@SubscribeEvent
	public static void castOnTotem(LivingAttackEvent event) {
		
		LivingEntity entity = event.getEntityLiving();
						
		if(event.getAmount() > entity.getHealth()) {	
			for(Hand hand : Hand.values()) {
				ItemStack handItem = entity.getHeldItem(hand);
				if (handItem.getItem() == Items.TOTEM_OF_UNDYING && handItem.getTag().contains("spell")) {
					if(customTotemDeathProtection(event.getSource(), entity, handItem)) {
						
						if(entity instanceof PlayerEntity) {
//							SpellCaster.PlayerCast(handItem, (PlayerEntity) entity, entity.getPositionVec().add(0.0, (double)entity.getEyeHeight(), 0.0), entity.getLookVec(), entity.world, false);
							ManaAndArtificeMod.getSpellHelper().playerCast(handItem, (PlayerEntity) entity, false);
						}	
					}
					event.setCanceled(true);
					break;
				}
			}
		}
	}
	
	private static boolean customTotemDeathProtection(DamageSource damage, LivingEntity entity, ItemStack item) {
		if (damage.canHarmInCreative()) {
			return false;
		} 
		else {
			ItemStack itemstack = item.copy();
			item.shrink(1);

			if (entity instanceof ServerPlayerEntity) {
				ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entity;
				serverplayerentity.addStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
				CriteriaTriggers.USED_TOTEM.trigger(serverplayerentity, itemstack);
			}
				
			entity.setHealth(1.0F);
			entity.clearActivePotions();
			entity.addPotionEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
			entity.addPotionEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
			entity.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
			entity.world.setEntityState(entity, (byte)35);			

			return itemstack != null;
		}
	}
	
	@SubscribeEvent
	public static void castOnLegs(LivingJumpEvent event) {
		LivingEntity entity = event.getEntityLiving();
		ItemStack legs = entity.getItemStackFromSlot(EquipmentSlotType.LEGS);
		if(legs != null && legs.getTag().contains("spell")) {
			if (entity instanceof PlayerEntity) {
//				SpellCaster.PlayerCast(legs, (PlayerEntity) entity, entity.getPositionVec().add(0.0, (double)entity.getEyeHeight(), 0.0), entity.getLookVec(), entity.world, false);
				ManaAndArtificeMod.getSpellHelper().playerCast(legs, (PlayerEntity) entity, false);
			}
		}
	}
	@SubscribeEvent
	public static void castOnBoots(LivingFallEvent event) {
		LivingEntity entity = event.getEntityLiving();
		ItemStack boots = entity.getItemStackFromSlot(EquipmentSlotType.FEET);
		float fallDistance = event.getDistance();
		if(fallDistance > 4) {
			if(boots != null && boots.getTag().contains("spell")) {
				if (entity instanceof PlayerEntity) {
//					SpellCaster.PlayerCast(boots, (PlayerEntity) entity, entity.getPositionVec().add(0.0, (double)entity.getEyeHeight(), 0.0), entity.getLookVec(), entity.world, false);
					ManaAndArtificeMod.getSpellHelper().playerCast(boots, (PlayerEntity) entity, false);
				}
			}
		}
	}
}