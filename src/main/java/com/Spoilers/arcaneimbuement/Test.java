package com.Spoilers.arcaneimbuement;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.ma.api.guidebook.RegisterGuidebooksEvent;
import com.ma.api.items.ItemUtils;


@Mod.EventBusSubscriber(modid = ArcaneImbuement.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Test {
	
	@SubscribeEvent
	public static void onRegisterGuidebooks(RegisterGuidebooksEvent event) {
		event.getRegistry().AddGuidebook(new ResourceLocation(ArcaneImbuement.MOD_ID, "guide/guidebook_en_us.json"));
		ArcaneImbuement.LOGGER.info("Arcane Imbuement registered");
	}
	
	@SubscribeEvent
	public static void testattack(LivingDamageEvent event) {
		if(event.getEntityLiving() instanceof PlayerEntity && event.getEntityLiving().getHeldItemMainhand() != null) {
			ItemUtils.writeCharges(event.getEntityLiving().getHeldItemMainhand(), 2);
			
			ArcaneImbuement.LOGGER.info("Confirm" + ItemUtils.getCharges(event.getEntityLiving().getHeldItemMainhand()));
		}	
	}
	
	
}
