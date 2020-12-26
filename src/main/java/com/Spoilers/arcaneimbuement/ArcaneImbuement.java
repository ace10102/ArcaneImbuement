package com.Spoilers.arcaneimbuement;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Spoilers.arcaneimbuement.augments.AugmentedArmorHandler;
import com.Spoilers.arcaneimbuement.rituals.RitualEffectAugmentArmor;
import com.Spoilers.arcaneimbuement.rituals.RitualEffectImbueArmor;
import com.Spoilers.arcaneimbuement.rituals.RitualEffectLevelUpArmor;
import com.Spoilers.arcaneimbuement.rituals.RitualEffectSpellbinding;
import com.Spoilers.arcaneimbuement.rituals.RitualEffectTesting;
import com.ma.api.rituals.RitualEffect;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("arcaneimbuement")
public class ArcaneImbuement {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "arcaneimbuement";
	
	public ArcaneImbuement() {
		
		/*FMLJavaModLoadingContext.get()).register(RegisterBook.onRegisterRituals);
		MinecraftForge.EVENT_BUS.register(RegisterBook.class :: onRegisterGuidebooks);*/
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.addListener(AugmentedArmorHandler::grantAugmentEXP);

	}
	
	@Mod.EventBusSubscriber(modid = ArcaneImbuement.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		
		@SubscribeEvent
		public static void onRegisterRituals(RegistryEvent.Register<RitualEffect> event) {
			event.getRegistry().register(new RitualEffectAugmentArmor(new ResourceLocation(ArcaneImbuement.MOD_ID, "rituals/augment_armor"))
					.setRegistryName(new ResourceLocation(ArcaneImbuement.MOD_ID, "ritual-effect-augment-armor")));
			event.getRegistry().register(new RitualEffectImbueArmor(new ResourceLocation(ArcaneImbuement.MOD_ID, "rituals/imbue_armor"))
					.setRegistryName(new ResourceLocation(ArcaneImbuement.MOD_ID, "ritual-effect-imbue-armor")));
			event.getRegistry().register(new RitualEffectLevelUpArmor(new ResourceLocation(ArcaneImbuement.MOD_ID, "rituals/level_up_armor"))
					.setRegistryName(new ResourceLocation(ArcaneImbuement.MOD_ID, "ritual-effect-level-up-armor")));
			event.getRegistry().register(new RitualEffectSpellbinding(new ResourceLocation(ArcaneImbuement.MOD_ID, "rituals/spellbinding"))
					.setRegistryName(new ResourceLocation(ArcaneImbuement.MOD_ID, "ritual-effect-spellbinding")));
			event.getRegistry().register(new RitualEffectTesting(new ResourceLocation(ArcaneImbuement.MOD_ID, "rituals/testing_ritual"))
					.setRegistryName(new ResourceLocation(ArcaneImbuement.MOD_ID, "ritual-effect-testing")));
			ArcaneImbuement.LOGGER.info("Arcane Imbuement rituals registered");
		}
	}
	
}
