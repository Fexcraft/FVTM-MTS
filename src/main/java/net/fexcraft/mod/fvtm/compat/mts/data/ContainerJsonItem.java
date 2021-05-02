package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.items.instances.ItemItem.ItemComponentType;
import minecrafttransportsimulator.jsondefs.JSONItem;
import minecrafttransportsimulator.packloading.PackResourceLoader.ItemClassification;
import net.fexcraft.mod.fvtm.compat.mts.MTSCompat;

public class ContainerJsonItem extends JSONItem {
	
	public ContainerJsonItem(){
		this.prefixFolders = "";
		this.packID = MTSCompat.MODID;
		this.systemName = "none";
		classification = ItemClassification.ITEM;
		general = new General();
		general.description = "A Generic FVTM Compat Item.";
		general.name = "container_small";
		general.stackSize = 1;
		item = new JSONItemGeneric();
		item.type = ItemComponentType.NONE;
	}
	
}
