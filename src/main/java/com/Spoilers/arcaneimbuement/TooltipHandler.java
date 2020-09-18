package com.Spoilers.arcaneimbuement;

import java.util.List;

import com.ma.api.spells.Component;
import com.ma.api.spells.Shape;
import com.ma.api.spells.attributes.AttributeValuePair;
import com.ma.api.spells.base.IModifiedSpellPart;
import com.ma.items.sorcery.ItemSpellRecipe;
import com.ma.spells.SpellCaster;
import com.ma.spells.crafting.SpellRecipe;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArcaneImbuement.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TooltipHandler {
	
	@SubscribeEvent @OnlyIn(value=Dist.CLIENT)
	public static void ItemTooltipEvent(ItemTooltipEvent event) {
		
		ItemStack hoveredItem = event.getItemStack();
		
		if(SpellRecipe.stackContainsSpell(hoveredItem) && !(hoveredItem.getItem() instanceof ItemSpellRecipe)) {
			
			addArmorInformation(hoveredItem, event.getToolTip());
			SpellRecipe recipe = SpellRecipe.fromNBT(hoveredItem.getOrCreateChildTag("spell"));
			addSpellInformation(hoveredItem, event.getToolTip(), recipe);
			
		}
	}
	public static void addArmorInformation(ItemStack stack, List<ITextComponent> tooltip) {
		if (stack.getItem() instanceof ArmorItem) {
			ArmorItem hoveredItem = (ArmorItem)stack.getItem();
		
			if(hoveredItem.getEquipmentSlot() == EquipmentSlotType.HEAD) {
				tooltip.add((ITextComponent)new TranslationTextComponent("Spell Activated on ").func_240699_a_(TextFormatting.AQUA));
			}
			if(hoveredItem.getEquipmentSlot() == EquipmentSlotType.CHEST) {
				tooltip.add((ITextComponent)new TranslationTextComponent("Spell Activated on Hit").func_240699_a_(TextFormatting.AQUA));
			}
			if(hoveredItem.getEquipmentSlot() == EquipmentSlotType.LEGS) {
				tooltip.add((ITextComponent)new TranslationTextComponent("Spell Activated on Jump").func_240699_a_(TextFormatting.AQUA));
			}
			if(hoveredItem.getEquipmentSlot() == EquipmentSlotType.FEET) {
				tooltip.add((ITextComponent)new TranslationTextComponent("Spell Activated on Fall").func_240699_a_(TextFormatting.AQUA));
			}
		}
	}
	
    public static void addSpellInformation(ItemStack stack, List<ITextComponent> tooltip, SpellRecipe recipe) {
		Minecraft mc = Minecraft.getInstance();
        if (recipe.isValid() && !recipe.isMysterious()) {
            SpellCaster.applyAdjusters(stack, (PlayerEntity)mc.player, recipe);
            IModifiedSpellPart shape = recipe.getShape();
            IModifiedSpellPart component = recipe.getComponent();
            TranslationTextComponent shapeText = new TranslationTextComponent(((Shape)recipe.getShape().getPart()).getRegistryName().toString());
            TranslationTextComponent componentText = new TranslationTextComponent(((Component)recipe.getComponent().getPart()).getRegistryName().toString());
            if (shape!= null && component != null) {
            	tooltip.add((ITextComponent)new TranslationTextComponent("Spell: " + shapeText.getString() + " " + componentText.getString()).func_240699_a_(TextFormatting.AQUA));
            }
            if (shape != null && hasAttributes(shape)) {
                tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.shape_attributes").func_240699_a_(TextFormatting.GREEN));
                for (AttributeValuePair attr : ((Shape)shape.getPart()).getModifiableAttributes()) {
                    tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.attribute_display", new Object[]{attr.getAttribute().name(), Float.valueOf(shape.getValue(attr.getAttribute()))}).func_240701_a_(new TextFormatting[]{TextFormatting.GRAY, TextFormatting.ITALIC}));
                }
                tooltip.add((ITextComponent)new StringTextComponent(" "));
            }
            if (component != null && hasAttributes(component)) {
                tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.component_attributes").func_240699_a_(TextFormatting.GREEN));
                for (AttributeValuePair attr : ((Component)component.getPart()).getModifiableAttributes()) {
                    tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.attribute_display", new Object[]{attr.getAttribute().name(), Float.valueOf(component.getValue(attr.getAttribute()))}).func_240701_a_(new TextFormatting[]{TextFormatting.GRAY, TextFormatting.ITALIC}));
                }
                tooltip.add((ITextComponent)new StringTextComponent(" "));
            }
            if (recipe.isChanneled()) {
                tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.channeled_mana_cost_display", new Object[]{String.format("%.2f", Float.valueOf(recipe.getManaCost() * 20.0f))}).func_240699_a_(TextFormatting.GOLD));
            } else {
                tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.mana_cost_display", new Object[]{String.format("%.2f", Float.valueOf(recipe.getManaCost()))}).func_240699_a_(TextFormatting.GOLD));
            }
            tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.complexity_display", new Object[]{Float.valueOf(recipe.getComplexity())}).func_240699_a_(TextFormatting.GOLD));
        }
    }
	
	public static boolean hasAttributes(IModifiedSpellPart part) {
		
		if(part.getPart() instanceof Shape) {
			if(((Shape)part.getPart()).getModifiableAttributes().isEmpty()) {
				return false;
			}
		}
		if(part.getPart() instanceof Component) {
			if(((Component)part.getPart()).getModifiableAttributes().isEmpty()) {
				return false;
			}
		}
		return true;
    }
}
