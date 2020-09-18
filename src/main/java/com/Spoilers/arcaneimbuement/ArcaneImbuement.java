package com.Spoilers.arcaneimbuement;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	}
	
	@Mod.EventBusSubscriber(modid = ArcaneImbuement.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {
		
		@SubscribeEvent
		public static void onRegisterRituals(RegistryEvent.Register<RitualEffect> event) {
			event.getRegistry().register(new RitualEffectAugmentArmor(new ResourceLocation(ArcaneImbuement.MOD_ID, "rituals/augment_armor"))
					.setRegistryName(new ResourceLocation(ArcaneImbuement.MOD_ID, "ritual-effect-augment-armor")));
			ArcaneImbuement.LOGGER.info("Arcane Imbuement ritual registered");
		}
	}
	
}
