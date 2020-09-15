package com.Spoilers.arcaneimbuement;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("arcaneimbuement")
public class ArcaneImbuement {
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "arcaneimbuement";
	final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
	 
	public ArcaneImbuement() {
		
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::doClientStuff);
		
		MinecraftForge.EVENT_BUS.register(this);

	}
	
	 private void setup(final FMLCommonSetupEvent event) {

	 }

	 private void doClientStuff(final FMLClientSetupEvent event) {
	    	
	 }

}
