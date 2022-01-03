package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.jsondefs.JSONPart;
import minecrafttransportsimulator.packloading.PackResourceLoader.ItemClassification;

public class ContainerJsonPart extends JSONPart {
	
	public ContainerJsonPart(String type){
		generic = new JSONPartGeneric();
		generic.type = "interactable_container_" + type;
		generic.customType = null;
		generic.width = generic.height = 1;
		general = new General();
		general.description = "FVTM Compat Container Slot Provider\nSize: " + (type.equals("single") ? 6 : 12);
		general.name = (type.equals("single") ? "Single" : "Double") + " Container";
		general.stackSize = 1;
		classification = ItemClassification.ITEM;
		systemName = "fvtm_compat_container_" + type;
		packID = "fvtm_mts";
	}

}
