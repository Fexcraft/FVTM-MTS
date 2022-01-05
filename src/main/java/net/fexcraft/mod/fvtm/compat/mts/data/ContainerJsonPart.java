package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.jsondefs.JSONPart;
import minecrafttransportsimulator.packloading.PackResourceLoader.ItemClassification;
import net.fexcraft.lib.mc.utils.Formatter;

public class ContainerJsonPart extends JSONPart {
	
	public ContainerJsonPart(String type){
		generic = new JSONPartGeneric();
		generic.type = "interactable_container_" + type;
		generic.customType = null;
		generic.width = generic.height = 1;
		interactable = new JSONPartInteractable();
		interactable.interactionType = InteractableComponentType.CRATE;
		general = new General();
		general.description = Formatter.PARAGRAPH_SIGN + "6FVTM <-> MTS (Immersive Vehicles) Compat\n"
							+ Formatter.PARAGRAPH_SIGN + "eContainer Slot Provider Part for IV Vehicles\n"
							+ Formatter.PARAGRAPH_SIGN + "7Size: " + (type.equals("single") ? 6 : 12) + " / " + type;
		general.name = (type.equals("single") ? "Single" : "Double") + " Container";
		general.stackSize = 1;
		classification = ItemClassification.PART;
		systemName = "container_holder_" + type;
		packID = "fvtm_mts";
	}

}
