package net.fexcraft.mod.fvtm.compat.mts.data;

import minecrafttransportsimulator.entities.components.AEntityE_Multipart;
import minecrafttransportsimulator.entities.instances.APart;
import minecrafttransportsimulator.jsondefs.JSONPartDefinition;
import minecrafttransportsimulator.mcinterface.WrapperNBT;
import minecrafttransportsimulator.mcinterface.WrapperPlayer;

public class ContainerPart extends APart {

	public ContainerPart(AEntityE_Multipart<?> entityOn, WrapperPlayer player, JSONPartDefinition placementDefinition, WrapperNBT data, APart parentPart){
		super(entityOn, player, placementDefinition, data, parentPart);
	}

}
