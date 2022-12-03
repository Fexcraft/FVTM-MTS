package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.items.components.AItemPart;
import minecrafttransportsimulator.items.components.AItemPart.AItemPartCreator;
import minecrafttransportsimulator.jsondefs.JSONPart;
import minecrafttransportsimulator.jsondefs.JSONSubDefinition;
import net.fexcraft.mod.fvtm.compat.mts.MTSCompat;

public class ContainerPartItemCreator extends AItemPartCreator {

	public ContainerPartItemCreator(){}

	@Override
	public boolean isCreatorValid(JSONPart definition){
		return definition instanceof ContainerJsonPart;
	}

	@Override
	public AItemPart createItem(JSONPart definition, JSONSubDefinition subName, String sourcePackID){
		return definition.systemName.contains("single") ? MTSCompat.CON_ITEM_SINGLE : MTSCompat.CON_ITEM_DOUBLE;
	}
	
}
