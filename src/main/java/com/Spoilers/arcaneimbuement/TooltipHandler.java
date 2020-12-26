package com.Spoilers.arcaneimbuement;

import java.util.List;

import com.Spoilers.arcaneimbuement.augments.Augmentation;
import com.Spoilers.arcaneimbuement.util.KeyboardUtil;
import com.ma.api.spells.parts.Component;
import com.ma.api.spells.parts.Shape;
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
		
		if(Augmentation.isAugmented(hoveredItem)) {
			
			addAugmentInformation(hoveredItem, event.getToolTip());
		}
		if(SpellRecipe.stackContainsSpell(hoveredItem) && !(hoveredItem.getItem() instanceof ItemSpellRecipe)) {
			
			addSpellInformation(hoveredItem, event.getToolTip());
			addArmorInformation(hoveredItem, event.getToolTip());
			
		}
	}
	public static void addAugmentInformation(ItemStack stack, List<ITextComponent> tooltip) {
		Augmentation augment = Augmentation.fromNBT(stack.getOrCreateChildTag(Augmentation.AUGMENTATION_TAG));
		short exp = augment.getExperience();
		byte level = augment.getLevel();
		int forlvl = augment.getExperienceForLevel(level);
		tooltip.add((ITextComponent)new TranslationTextComponent("Augmented").mergeStyle(TextFormatting.DARK_AQUA));
		tooltip.add((ITextComponent)new TranslationTextComponent("Exp: " + exp).mergeStyle(TextFormatting.DARK_AQUA));
		tooltip.add((ITextComponent)new TranslationTextComponent("Level: " + level).mergeStyle(TextFormatting.DARK_AQUA));
		if (KeyboardUtil.isShift() && forlvl != -1) {
			tooltip.add((ITextComponent)new TranslationTextComponent("Can lvl up at: " + forlvl + " Exp").mergeStyle(TextFormatting.DARK_AQUA));
		}
		else if (KeyboardUtil.isShift() && forlvl == -1) {
			tooltip.add((ITextComponent)new TranslationTextComponent("Lvl Max").mergeStyle(TextFormatting.DARK_AQUA));
		}	
	}
	public static void addArmorInformation(ItemStack stack, List<ITextComponent> tooltip) {
		if (stack.getItem() instanceof ArmorItem) {
			ArmorItem hoveredItem = (ArmorItem)stack.getItem();
			
			if (KeyboardUtil.isShift()) {
				if (hoveredItem.getEquipmentSlot() == EquipmentSlotType.HEAD) {
					tooltip.add((ITextComponent)new TranslationTextComponent("Spell Activated on ").mergeStyle(TextFormatting.GOLD));
				}
				if (hoveredItem.getEquipmentSlot() == EquipmentSlotType.CHEST) {
					tooltip.add((ITextComponent)new TranslationTextComponent("Spell Activated on Hit").mergeStyle(TextFormatting.GOLD));
				}
				if (hoveredItem.getEquipmentSlot() == EquipmentSlotType.LEGS) {
					tooltip.add((ITextComponent)new TranslationTextComponent("Spell Activated on Jump").mergeStyle(TextFormatting.GOLD));
				}
				if (hoveredItem.getEquipmentSlot() == EquipmentSlotType.FEET) {
					tooltip.add((ITextComponent)new TranslationTextComponent("Spell Activated on Fall").mergeStyle(TextFormatting.GOLD));
				}
			}	
		}
	}
	
    public static void addSpellInformation(ItemStack stack, List<ITextComponent> tooltip) {
    	SpellRecipe recipe = SpellRecipe.fromNBT(stack.getOrCreateChildTag("spell"));
		Minecraft mc = Minecraft.getInstance();
        if (recipe.isValid() && !recipe.isMysterious()) {
            SpellCaster.applyAdjusters(stack, (PlayerEntity)mc.player, recipe);
            IModifiedSpellPart shape = recipe.getShape();
            IModifiedSpellPart component = recipe.getComponent(0);
            TranslationTextComponent shapeText = new TranslationTextComponent(((Shape)recipe.getShape().getPart()).getRegistryName().toString());
            TranslationTextComponent componentText = new TranslationTextComponent(((Component)recipe.getComponent(0).getPart()).getRegistryName().toString());
            if (shape != null && component != null) {
            	tooltip.add((ITextComponent)new TranslationTextComponent("Spell: " + shapeText.getString() + " " + componentText.getString()).mergeStyle(TextFormatting.AQUA));
            }
            if (!KeyboardUtil.isShift()) {
            	tooltip.add((ITextComponent)new TranslationTextComponent("Shift for Spell details").mergeStyle(TextFormatting.AQUA));
            }
            if (KeyboardUtil.isShift()) {
            	if (shape != null && hasAttributes(shape)) {
                    tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.shape_attributes").mergeStyle(TextFormatting.GREEN));
                    for (AttributeValuePair attr : ((Shape)shape.getPart()).getModifiableAttributes()) {
                        tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.attribute_display", new Object[]{attr.getAttribute().name(), Float.valueOf(shape.getValue(attr.getAttribute()))}).mergeStyle(new TextFormatting[]{TextFormatting.GRAY, TextFormatting.ITALIC}));
                    }
                    tooltip.add((ITextComponent)new StringTextComponent(" "));
                }
                if (component != null && hasAttributes(component)) {
                    tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.component_attributes").mergeStyle(TextFormatting.GREEN));
                    for (AttributeValuePair attr : ((Component)component.getPart()).getModifiableAttributes()) {
                        tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.attribute_display", new Object[]{attr.getAttribute().name(), Float.valueOf(component.getValue(attr.getAttribute()))}).mergeStyle(new TextFormatting[]{TextFormatting.GRAY, TextFormatting.ITALIC}));
                    }
                    tooltip.add((ITextComponent)new StringTextComponent(" "));
                }
                if (recipe.isChanneled()) {
                    tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.channeled_mana_cost_display", new Object[]{String.format("%.2f", Float.valueOf(recipe.getManaCost() * 20.0f))}).mergeStyle(TextFormatting.GOLD));
                } else {
                    tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.mana_cost_display", new Object[]{String.format("%.2f", Float.valueOf(recipe.getManaCost()))}).mergeStyle(TextFormatting.GOLD));
                }
                tooltip.add((ITextComponent)new TranslationTextComponent("item.mana-and-artifice.spell.complexity_display", new Object[]{Float.valueOf(recipe.getComplexity())}).mergeStyle(TextFormatting.GOLD));
            }  
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
