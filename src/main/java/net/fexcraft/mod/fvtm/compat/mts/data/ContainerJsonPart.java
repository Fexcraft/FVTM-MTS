package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.jsondefs.JSONPart;

public class ContainerJsonPart extends JSONPart {
	
	public ContainerJsonPart(){
		generic = new JSONPartGeneric();
		generic.type = "fvtm.container";
		generic.customType = "container_single";
		generic.width = generic.height = 1;
	}

}
