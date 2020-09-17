package com.Spoilers.arcaneimbuement;

import com.ma.api.blocks.ISpellInteractible;
import com.ma.api.capabilities.IPlayerMagic;
import com.ma.api.capabilities.IPlayerProgression;
import com.ma.api.events.SpellCastEvent;
import com.ma.api.sound.SFX;
import com.ma.api.spells.Component;
import com.ma.api.spells.Shape;
import com.ma.api.spells.SpellTarget;
import com.ma.api.spells.base.ISpellDefinition;
import com.ma.effects.EffectInit;
import com.ma.entities.EntityInit;
import com.ma.entities.sorcery.SpellCastingResult;
import com.ma.entities.utility.EntityResidualMagic;
import com.ma.spells.NameProcessors;
import com.ma.spells.SpellCaster;
import com.ma.spells.crafting.ModifiedSpellPart;
import com.ma.spells.crafting.SpellRecipe;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

public class ImbuedSpellCaster extends SpellCaster {
	
	public static SpellCastingResult Cast(ItemStack imbuedItem, LivingEntity caster, Vector3d casterPosition, Vector3d casterLook, World world, boolean consumeMana) {
		BlockState state;
        EntityResidualMagic e;
        
        if (caster.getActivePotionEffect((Effect)EffectInit.SILENCE.get()) != null) {
            return SpellCastingResult.SILENCED;
        }
        SpellRecipe recipe = SpellRecipe.fromNBT(imbuedItem.getOrCreateChildTag("spell"));
        if (!recipe.isValid()) {
            return SpellCastingResult.INVALID_RECIPE;
        }
        Shape shape = (Shape)recipe.getShape().getPart();
        if (shape == null) {
            return SpellCastingResult.INVALID_RECIPE;
        }
        Component component = (Component)recipe.getComponent().getPart();
        if (component == null) {
            return SpellCastingResult.INVALID_RECIPE;
        }
        if (!DispatchImbuedSpellCast(recipe, caster)) {
            return SpellCastingResult.CANCELED_BY_EVENT;
        }
        if(caster instanceof PlayerEntity) {
        	PlayerEntity playerCaster = (PlayerEntity)caster;
        	
            SpellCaster.applyAdjusters(imbuedItem, playerCaster, recipe);
            if (!world.isRemote && recipe.isMysterious()) {
                recipe.setMysterious(false);
                recipe.WriteToNBT(imbuedItem.getOrCreateTag());
            }
            SpellTarget target = shape.Target(playerCaster, casterPosition, casterLook, world, (ModifiedSpellPart<Shape>)recipe.getShape(), recipe);
            if (target == SpellTarget.NONE) {
            	return SpellCastingResult.NO_TARGET;
            }
            SoundEvent spellSound = NameProcessors.checkAndOverrideSound(recipe, imbuedItem.getDisplayName().getString(), SFX.Spell.Cast.ForAffinity(recipe.getAffinity()));
            world.playSound(playerCaster, (double)caster.func_233580_cy_().getX(), (double)caster.func_233580_cy_().getY(), (double)caster.func_233580_cy_().getZ(), spellSound, SoundCategory.PLAYERS, 1.0f, 1.0f);
            
            if (!world.isRemote && (e = (EntityResidualMagic)((EntityType)EntityInit.RESIDUAL_MAGIC.get()).spawn((ServerWorld)world, imbuedItem, playerCaster, caster.func_233580_cy_().add(0, 2, 0), SpawnReason.TRIGGERED, true, true)) != null) {
                e.setResidualPower(recipe.getManaCost());
            }
            
            if (target.isBlock() && (state = world.getBlockState(target.getBlock())).getBlock() instanceof ISpellInteractible && ((ISpellInteractible)state.getBlock()).onHitBySpell(world, target.getBlock(), recipe)) {
                return SpellCastingResult.SPELL_INTERACTIBLE_BLOCK_HIT;
            }
            if (!world.isRemote) {
                component.ApplyEffect(playerCaster, (ServerWorld)world, target, (ModifiedSpellPart<Component>)recipe.getComponent(), recipe);
                SpellCaster.spawnClientFX((Component)recipe.getComponent().getPart(), world, target.getPosition(), (PlayerEntity) caster, recipe);
            }
        }
        if (shape.spawnsTargetEntity()) {
            if (shape.isChanneled()) {
                return SpellCastingResult.CHANNEL;
            }
            return SpellCastingResult.SUCCESS;
        }
        return SpellCastingResult.SUCCESS;
    }
	
    public static boolean DispatchImbuedSpellCast(ISpellDefinition spell, LivingEntity caster) {
        ImbuedSpellCastEvent evt = new ImbuedSpellCastEvent(spell, caster);
        MinecraftForge.EVENT_BUS.post((Event)evt);
        return !evt.isCanceled();
    }
    	
}

