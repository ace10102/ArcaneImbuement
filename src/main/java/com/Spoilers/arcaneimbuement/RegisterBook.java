package com.Spoilers.arcaneimbuement;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.ma.api.guidebook.RegisterGuidebooksEvent;


@Mod.EventBusSubscriber(modid = ArcaneImbuement.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RegisterBook {

	@SubscribeEvent
	public static void onRegisterGuidebooks(RegisterGuidebooksEvent event) {
		event.getRegistry().addGuidebookPath(new ResourceLocation(ArcaneImbuement.MOD_ID, "guide"));
		ArcaneImbuement.LOGGER.info("Arcane Imbuement registered");
	}
	
}
