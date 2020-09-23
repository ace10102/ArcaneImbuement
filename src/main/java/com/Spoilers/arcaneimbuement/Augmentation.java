package com.Spoilers.arcaneimbuement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class Augmentation {
	
	public static final String AUGMENTATION_TAG = "augmentation";
	private short experience;
	private byte level;
	
	public Augmentation() {
		this.experience = 0;
		this.level = 0;
	}
	
	public static void augment(ItemStack item, Augmentation imbuement) {
		imbuement.writeToNBT(item.getOrCreateTag());
	}
	
	public void writeToNBT(CompoundNBT nbt) {
		CompoundNBT imbuement = new CompoundNBT();
		imbuement.putShort("experience", this.getExperience());
		imbuement.putByte("level", this.getLevel());
		nbt.put(AUGMENTATION_TAG, (INBT)imbuement);
	}
	
	public static Augmentation fromNBT(CompoundNBT nbt) {
		Augmentation imbuement = new Augmentation();
        if (nbt == null) {
            return imbuement;
        }
        if (nbt.contains(AUGMENTATION_TAG)) {
            nbt = nbt.getCompound(AUGMENTATION_TAG);
        }
        if (nbt.contains("experience")) {
        	imbuement.experience = nbt.getShort("experience");
        }
        if (nbt.contains("level")) {
        	imbuement.level = nbt.getByte("level");
        }
        return imbuement;
	}
	
	public short getExperience() {
		return this.experience;
	}
	
	public byte getLevel() {
		return this.level;
	}
	
	public static boolean isAugmented(ItemStack stack) {
		if (!stack.hasTag()) {
            return false;
        }
        CompoundNBT tag = stack.getTag();
        if (tag.contains(AUGMENTATION_TAG)) {
            CompoundNBT subTag = tag.getCompound(AUGMENTATION_TAG);
            return subTag.contains("experience") && subTag.contains("level");
        }
        return false;
	}
	
	public void grantExperience(short exp, CompoundNBT nbt) {
		this.experience += exp;
		nbt.putShort("experience", this.experience);
	}
	
	public void increaseLevel(CompoundNBT nbt) {
		this.level ++;
		nbt.putByte("level", this.level);
	}
}
