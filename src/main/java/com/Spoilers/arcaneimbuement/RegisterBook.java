package com.Spoilers.arcaneimbuement;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.ma.api.guidebook.RegisterGuidebooksEvent;
import com.ma.api.rituals.RitualEffect;


@Mod.EventBusSubscriber(modid = ArcaneImbuement.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RegisterBook {
	
	/*@SubscribeEvent 
	public static void onRegisterRituals(RegistryEvent.Register<RitualEffect> event) {
		event.getRegistry().register((RitualEffect)new RitualEffectAugmentArmor(new ResourceLocation(ArcaneImbuement.MOD_ID, "rituals/augment_armor"))
				.setRegistryName(new ResourceLocation(ArcaneImbuement.MOD_ID, "ritual-effect-augment-armor")));
		ArcaneImbuement.LOGGER.info("Arcane Imbuement ritual registered");
	}
	*/
	@SubscribeEvent
	public static void onRegisterGuidebooks(RegisterGuidebooksEvent event) {
		event.getRegistry().AddGuidebook(new ResourceLocation(ArcaneImbuement.MOD_ID, "guide/guidebook_en_us.json"));
		ArcaneImbuement.LOGGER.info("Arcane Imbuement registered");
	}
	
}
