package com.Spoilers.arcaneimbuement.rituals;

import com.ma.api.rituals.IRitualContext;
import com.ma.api.rituals.RitualBlockPos;

import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class RitualUtils {

	public static void replaceReagents(IRitualContext context, ResourceLocation key, NonNullList<ResourceLocation> replacements) {
        if (context.getIndexedPositions().size() == 0 || replacements.size() == 0) {
            return;
        }
        int replaceIndex = 0;
        for (RitualBlockPos reagent : context.getIndexedPositions()) {
            if (!reagent.isPresent() || !reagent.getReagent().isDynamic() || reagent.getReagent().getResourceLocation().compareTo(key) != 0) continue;
            reagent.getReagent().setResourceLocation((ResourceLocation)replacements.get(replaceIndex));
            if (replaceIndex < (replacements.size()-1)) replaceIndex++;
        }
        return;
    }
}

/*private void replaceReagents(ResourceLocation key, NonNullList<RitualBlockPos> reagents, NonNullList<ResourceLocation> replacements) {
if (reagents.size() == 0 || replacements.size() == 0) {
	return;
}
int replaceIndex = 0;
for (RitualBlockPos reagent : reagents) {
	if (reagent.getReagent() == null) continue;
	if (reagent.getReagent().getResourceLocation().equals(key)) {
		reagent.getReagent().setResourceLocation((ResourceLocation) replacements.get(replaceIndex));
		if (replaceIndex < (replacements.size()-1)) replaceIndex++;
	} 
}
return;
}*/