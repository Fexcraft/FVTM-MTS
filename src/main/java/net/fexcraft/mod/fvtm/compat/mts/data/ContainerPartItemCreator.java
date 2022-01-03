package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.items.components.AItemPart.AItemPartCreator;
import minecrafttransportsimulator.jsondefs.JSONPart;

public class ContainerPartItemCreator extends AItemPartCreator {

	private ContainerPartItem ipc;

	public ContainerPartItemCreator(ContainerPartItem con){
		this.ipc = con;
	}

	@Override
	public boolean isCreatorValid(JSONPart definition){
		return definition instanceof ContainerJsonPart;
	}

	@Override
	public AItemPart createItem(JSONPart definition, String subName, String sourcePackID){
		return ipc;
	}
	
}
