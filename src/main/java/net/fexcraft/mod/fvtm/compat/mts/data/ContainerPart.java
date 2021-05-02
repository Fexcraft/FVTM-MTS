package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.entities.components.AEntityE_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.WrapperNBT;

public class ContainerPart extends APart {

	public ContainerPart(AEntityE_Multipart<?> entityOn, JSONPartDefinition placementDefinition, WrapperNBT data, APart parentPart){
		super(entityOn, placementDefinition, data, parentPart);
	}

}
