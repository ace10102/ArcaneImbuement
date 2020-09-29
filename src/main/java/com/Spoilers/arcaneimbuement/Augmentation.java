package com.Spoilers.arcaneimbuement;

//import com.Spoilers.arcaneimbuement.imbuements.Imbuement;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public class Augmentation {
	
	public static final String AUGMENTATION_TAG = "augmentation";
	private short experience;
	private byte level;
	private int[] expPerLevel;
	//private Imbuement[] imbuements;
	
	
	public Augmentation() {
		this.experience = 0;
		this.level = 0;
		this.expPerLevel = new int[5];
		//this.imbuements = new Imbuement[4];
		
	}
	
	public static void augment(ItemStack item, Augmentation augment) {
		augment.determineExpPerLevel(item);
		augment.writeToNBT(item.getOrCreateTag());
	}
	
	public static boolean isAugmented(ItemStack stack) {
		if (!stack.hasTag()) {
            return false;
        }
        CompoundNBT tag = stack.getTag();
        if (tag.contains(AUGMENTATION_TAG)) {
            CompoundNBT subTag = tag.getCompound(AUGMENTATION_TAG);
            return subTag.contains("experience") && subTag.contains("level") && subTag.contains("levelreqs");
        }
        return false;
	}
	
	public void writeToNBT(CompoundNBT nbt) {
		CompoundNBT augment = new CompoundNBT();
		augment.putShort("experience", this.experience);
		augment.putByte("level", this.level);
		augment.putIntArray("levelreqs", this.expPerLevel);
		nbt.put(AUGMENTATION_TAG, (INBT)augment);
	}
	
	public static Augmentation fromNBT(CompoundNBT nbt) {
		Augmentation augment = new Augmentation();
        if (nbt == null) {
            return augment;
        }
        if (nbt.contains(AUGMENTATION_TAG)) {
            nbt = nbt.getCompound(AUGMENTATION_TAG);
        }
        if (nbt.contains("experience")) {
        	augment.experience = nbt.getShort("experience");
        }
        if (nbt.contains("level")) {
        	augment.level = nbt.getByte("level");
        }
        if (nbt.contains("levelreqs")) {
        	augment.expPerLevel = nbt.getIntArray("levelreqs");
        }
        return augment;
	}
	
	public short getExperience() {
		return this.experience;
	}
	
	public byte getLevel() {
		return this.level;
	}
	
	public int getExperienceForLevel(byte level) {
		if(level <= 4)
			return this.expPerLevel[level];
		else return -1;
	}
	
	
	public void grantExperience(short exp, CompoundNBT nbt) {
		if ((this.experience += exp) > (this.expPerLevel[4] * 2))
			this.experience = (short)(this.expPerLevel[4] * 2);
		else this.experience += exp;
		nbt.putShort("experience", this.experience);
	}
	
	public void increaseLevel(CompoundNBT nbt) {
		this.level ++;
		nbt.putByte("level", this.level);
	}
	
	public void determineExpPerLevel(ItemStack item) {
		double maxExp = item.getMaxDamage() * 1.5;
		for(int i = 4; i >= 0; i--)
		{
			this.expPerLevel[i] = (int)maxExp;
			maxExp /= 2;
		}
	}
}
