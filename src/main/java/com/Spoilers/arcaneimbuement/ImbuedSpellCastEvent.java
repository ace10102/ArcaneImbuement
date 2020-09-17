package com.Spoilers.arcaneimbuement;

import com.ma.api.spells.base.ISpellDefinition;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class ImbuedSpellCastEvent extends Event {

    ISpellDefinition spell;
    LivingEntity caster;
    
	public ImbuedSpellCastEvent(ISpellDefinition spell, LivingEntity caster) {
        this.spell = spell;
        this.caster = caster;
		
	}
	
    public ISpellDefinition getSpell() {
        return this.spell;
    }
	
    public LivingEntity getCaster() {
        return this.caster;
    }
}
